import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.InputStreamReader;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class Client implements Runnable
{
	//Client attributes
	private int Socketport = 6030;
	private DataInputStream in;
	private DataOutputStream out;
	private volatile boolean bRun;
	private ExecutorService es;
	private Socket sockcli = null;
	private ApplicationRobotino app;
	private BufferedReader inBuffer;
	private Robotino currentRobot;
	
	// constructor
	/**
     * to create new Client object 
     */
	public Client (ExecutorService es,ApplicationRobotino app)
	{
		this.es = es;
		this.app=app;
		for (Robotino rob : app.getRobots()){
        	if (rob.getIpAdress().equals(app.getIpRobot())){
        		currentRobot=rob;
        	}	
        }
	}

	// connection
	/**
     * to connect a robotino to the ComManager
     * 
     * @param url the url of the ComManager
     * @return -1 if there is a problem, 1 if all is good and 0 if there is not connection
     */
	public int connection(String url)
	{
		try{
			sockcli = new Socket (url, Socketport);
		}
		catch (IOException ex){ 
			return -1; 
		}

		if(sockcli.isConnected()){
			try {
				this.in = new DataInputStream(sockcli.getInputStream());
				this.out = new DataOutputStream(sockcli.getOutputStream());
				this.inBuffer = new BufferedReader(new InputStreamReader(
						this.sockcli.getInputStream()));
			} 
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return 1;
		}
		else{
			return 0;
		}
	}
	
	// receptionTreatment
	/**
     * to make the correct action when the robotino receive a message
     * 
     * @param sMessage the message that has been received
     */
	public void receptionTreatment(String sMessage){
		if(sMessage.length() != 0){
			System.out.println("Message recu par robotino : "+sMessage);
			Object obj = JSONValue.parse(sMessage);
			JSONObject objJson = (JSONObject) obj;
			
			String from = (String) objJson.get("From");
			String to = (String) objJson.get("To");
			String msgType = (String)objJson.get("MsgType");
			String order = (String)objJson.get("OrderName");
			
			if (to.equals(app.getIpHost())){
				// if the message is for this computer
				if (msgType.equals("Order")){
					
					// construction of the ACK execution of the feature
					
					JSONObject json = new JSONObject();
					json.put("From", app.getIpHost());
					json.put("To", from);
					json.put("MsgType", "Ack");
					
					if (order.equals("ConnectTo")){
						if (currentRobot.getIpUser().equals("error")){
							// if not device is connected to the robot
							currentRobot.setIpUser(from);
							
						}
						if (currentRobot.getIpUser().equals(from)){
							// if the device that want to connect is the device that has the priority
							ArrayList<String> feat=new ArrayList<String>();
							for (Feature ft : app.getFeatures()){
								feat.add(ft.getName());
							}
							json.put("FeatureList", feat);
							json.put("OrderAccepted", true);
							this.app.interpretFeature(objJson);
						}
						else {
							// if another device is already connected to the robot
							json.put("OrderAccepted", false);
						}
					}
					else {
						if (currentRobot.getIpUser().equals(from)){
							// if the device that want to use the robot is the device that has reserved the robot
							if (order.equals("Stop")){
								// the order sent is Stop
								json.put("OrderAccepted", true);
								json.put("End", true);
							}
							
							else if (order.equals("Disconnect")){
								// the order sent is Disconnect : the robot is not more reserved
								json.put("Disconnected", true);
								currentRobot.setIpUser("error");
							}
							
							else {
								json.put("OrderAccepted", true);
							}
						
							this.app.interpretFeature(objJson);
						}
						
						else {
							json.put("OrderAccepted", false);
						}
					}
					this.writeMessage(json.toString());
					// if the order is Stop, a message End is sent and the connection with the robot is closed
					if (order.equals("Stop")){
						app.client.deco();
						app.runFeature(new Stop(), currentRobot);
					}	
				}
			}
		}	
	}
	
	// writeMessage
	/**
     * to sent a message in the correct NetworkFlow
     * 
     * @param sLeMessage the message that has to be sent
     */
	public void writeMessage(String sLeMessage){
		try {
			NetworkFlow.writeMessage(out, sLeMessage+"\r\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// deco
	/**
     * to send a Logout message to the ComManager (disconnect)
     *
     */
	public void deco(){
		
			JSONObject json = new JSONObject();
			json.put("From", app.getIpHost());
			json.put("To", app.getIpGestCom());
			json.put("MsgType", "Logout");
			this.writeMessage(json.toString());
			
		try {	
			System.out.println("1");
			this.sockcli.close();
			System.out.println("2");
			bRun=false;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// run
	/**
     * Override of the run method, execution of the code
     *
     */
	public void run ()
	{	
		bRun  = true;
		String sChaine="";
		
		if (sockcli.isConnected() && bRun) {
			// the robot send an ident message to the ComManager
			JSONObject json = new JSONObject();
			json.put("From", app.getIpHost());
			json.put("To", app.getIpGestCom());
			json.put("MsgType", "Ident");
			json.put("EquipmentType", "Robot");
			this.writeMessage(json.toString());
		}
		while(bRun && sockcli.isConnected())
		{
			try { 
				while (currentRobot.getBusy()){}
				// read the message that arrive from the network flow
				sChaine = NetworkFlow.readMessage(in);
				receptionTreatment(sChaine);
				
			}
			catch(EOFException a){
				
				bRun = false;
				try {
					this.sockcli.close();
				} 
				catch (IOException e) {
					e.printStackTrace();
				}
			}
			catch (IOException e){
				e.printStackTrace();
			}
			Thread.yield();
		}
	}
}
