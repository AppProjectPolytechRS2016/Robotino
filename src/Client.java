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

	public int connexion(String url)
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
	
	public void traitementReception(String sMessage){
		if(sMessage.length() != 0){
			System.out.println(sMessage.length());
			System.out.println("Message recu par robotino : "+sMessage);
			Object obj = JSONValue.parse(sMessage);
			JSONObject objJson = (JSONObject) obj;
			
			String from = (String) objJson.get("From");
			String to = (String) objJson.get("To");
			String msgType = (String)objJson.get("MsgType");
			String order = (String)objJson.get("OrderName");
			
			if (to.equals(app.getIpHost())){
				System.out.println("C'est pour moi !");
				if (msgType.equals("Order")){
					
					// construction of the ACK execution of the feature
					
					JSONObject json = new JSONObject();
					json.put("From", app.getIpHost());
					json.put("To", from);
					json.put("MsgType", "Ack");
					
					if (order.equals("ConnectTo")){
						if (currentRobot.getIpUser().equals("error")){
							currentRobot.setIpUser(from);
							
						}
						if (currentRobot.getIpUser().equals(from)){
							ArrayList<String> feat=new ArrayList<String>();
							for (Feature ft : app.getFeatures()){
								feat.add(ft.getName());
							}
							json.put("FeatureList", feat);
							this.app.interpretFeature(objJson);
						}
						else {
							json.put("OrderAccepted", false);
						}
					}
					else {
						if (currentRobot.getIpUser().equals(from)){
							if (order.equals("Stop")){
								json.put("OrderAccepted", true);
								json.put("End", true);
							}
							else if (order.equals("Disconnect")){
								json.put("Disconnected", true);
								currentRobot.setIpUser("error");
							}
							else {
								//json.put("Received", true);
								json.put("OrderAccepted", true);
							}
						
							
							this.app.interpretFeature(objJson);
						}
						else {
							json.put("OrderAccepted", false);
						}
					}
					this.writeMessage(json.toString());
					if (order.equals("Stop")){
						app.client.deco();
					}
						
				}
			}
		}	
	}
	
	public void writeMessage(String sLeMessage){
		try {
			NetworkFlow.writeMessage(out, sLeMessage+"\r\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

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
	
	public void run ()
	{	
		
		bRun  = true;
		String sChaine="";
		
		
		if (sockcli.isConnected() && bRun) {
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
				sChaine = NetworkFlow.readMessage(in); //Lecture des messages venant du client
				//sChaine = NetworkFlow.readMessageBis(this.inBuffer); //Lecture des messages venant du client
				traitementReception(sChaine);
				
			}
			catch(EOFException a){
				
				bRun = false;
				try {
					this.sockcli.close();
				} 
				catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			catch (IOException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
				//System.exit(0);
			}
			Thread.yield();
		}
	}

	/*public static void main (String args[]) throws Exception
	{
		int iTestCo;
		ExecutorService es = Executors.newFixedThreadPool(3);
		Client client = new Client(es,new ApplicationRobotino());
		
		iTestCo = client.connexion("127.0.0.1");
		
		if(iTestCo == 1){
			System.out.println("Connected");
			es.execute(client);
		}
	}*/
}
