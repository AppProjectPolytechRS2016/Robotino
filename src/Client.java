import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
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
		//System.out.println("Message recu par robotino : "+sMessage);
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
					
					// construction de l'ACK interpretation de la feature
					
					JSONObject json = new JSONObject();
					json.put("From", app.getIpHost());
					json.put("To", from);
					json.put("MsgType", "Ack");
					json.put("Received", true);
					
					this.app.interpretFeature(objJson);
					json.put("OrderAccepted", true);
					
					if (order.equals("ConnectTo")){
						json.put("FeatureList", app.getFeatures());
					}
					this.writeMessage(json.toString());
						
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
		try {
			JSONObject json = new JSONObject();
			json.put("From", app.getIpHost());
			json.put("To", app.getIpGestCom());
			json.put("MsgType", "LogOut");
			json.put("EquipmentType", "Robot");
			this.writeMessage(json.toString());
			
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
		/*String sChaine;
		while(bRun){
			try {
				sChaine = NetworkFlow.readMessage(in); //Lecture des messages venant du client
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
				System.exit(0);	
			}	
			Thread.yield();
		}*/
		
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
			//bRun=false;
			try { 
				while (currentRobot.getBusy()){}
				sChaine = NetworkFlow.readMessage(in); //Lecture des messages venant du client
				//sChaine = NetworkFlow.readMessageBis(this.inBuffer); //Lecture des messages venant du client
				traitementReception(sChaine);
				//JSONObject json2 = new JSONObject();
				//json2.put("From", "tata");
				//this.writeMessage(json2.toString());
				//System.out.println("Fin de l ecoute"+json2.toString());
			
			}
			catch (IOException e) {
				bRun=false;
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//Thread.yield();
		
		try {
			sockcli.close();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
