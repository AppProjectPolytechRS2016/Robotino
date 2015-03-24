import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class Client implements Runnable
{
	//Client attributes
	private int Socketport = 6030;
	private DataInputStream in;
	private DataOutputStream out;
	private boolean bRun;
	private ExecutorService es;
	private Socket sockcli = null;
	private ApplicationRobotino app;
	
	public Client (ExecutorService es,ApplicationRobotino app)
	{
		this.es = es;
		this.app=app;
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
		System.out.println("Message recu par robotino : "+sMessage);
		Object obj = JSONValue.parse(sMessage);
		JSONObject objJson = (JSONObject) obj;
		this.app.interpretFeature(objJson);
		
		// ACK
		JSONObject json = new JSONObject();
		json.put("From", "tata");
		this.writeMessage(json.toString());
	}
	
	public void writeMessage(String sLeMessage){
		try {
			NetworkFlow.writeMessage(out, sLeMessage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void deco(){
		try {
			System.out.println("1");
			this.sockcli.close();
			System.out.println("2");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void run ()
	{	
		
		bRun  = true;
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
		while(sockcli.isConnected() && bRun)
		{
			
			System.out.println("try");
			//bRun=false;
			try { 
				traitementReception(NetworkFlow.readMessage(in)); //reception du message
				JSONObject json2 = new JSONObject();
				json2.put("From", "tata");
				this.writeMessage(json2.toString());
				System.out.println("Fin de l ecoute"+json2.toString());
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 
		
		try {
			sockcli.close();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main (String args[]) throws Exception
	{
		int iTestCo;
		ExecutorService es = Executors.newFixedThreadPool(3);
		Client client = new Client(es,new ApplicationRobotino());
		
		iTestCo = client.connexion("127.0.0.1");
		
		if(iTestCo == 1){
			System.out.println("Connected");
			es.execute(client);
		}
	}
}
