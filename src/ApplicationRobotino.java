import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.simple.JSONObject;

// port of the c++ example
// without callbacks, as java does not support them directly

public class ApplicationRobotino
{
	// attributes
	private ArrayList<Feature> features;
	private ArrayList<Robotino> robots;
	private String GestCom_IP="193.48.125.68";
	private static int GestCom_PORT = 6030;
	private Client client;
	private String ipHost;
	
	// constructor
	/**
     * to create new ApplicationRobotino object 
     */
	public ApplicationRobotino(){
		
		features=new ArrayList<Feature>();
		robots=new ArrayList<Robotino>();
	
		Feature move = new Move(0,0,0,0);
		features.add(move);
		Feature init = new Init();
		features.add(init);
		Feature walk = new Walk();
		features.add(walk);
		Feature stop = new Stop();
		features.add(stop);
		
		String hostname1 = System.getProperty("hostname", "193.48.125.37");
		Robotino robotino1 = new Robotino(hostname1);
		robots.add(robotino1);
		
		/*String hostname2 = System.getProperty("hostname", "193.48.125.38");
		Robotino robotino2 = new Robotino(hostname2);
		robots.add(robotino2);*/
		
		try {
			ipHost=InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getIpHost(){
		return ipHost;
	}
	
	public String getIpGestCom(){
		return GestCom_IP;
	}
	
	// runFeature
	/**
     * to execute a particular feature on a particular robot
     * 
     * @param feature the feature we want to run
     * @param robot the robot on which we want to run the feature
     */
	public static void runFeature(Feature feature, Robotino robot){
		try {
			feature.runFeature(robot);
			}
		catch (Exception e)
	        {
	            e.printStackTrace();
	        }
	}
	// une fction de décodage qui renvoie un vecteur avec nom de feature et param
	// une fction de modif des features en questions
	// une fction d'appel de la feature
	
	public void interpretFeature(JSONObject json){
		String to = (String)json.get("To");
		String msgType = (String)json.get("MsgType");
		if (to.equals(this.ipHost)){
			if (msgType.equals("Order")){
				// parametres du Json a recuperer
				String word = "error";
				
				// modification of the feature we want to use
				switch ((String)json.get("OName")){
				case "Move" :
					// we fix the time at 3 seconds
					int time=3000;
					// we get the parameters from the JSON
					int xSpeed=(int)json.get("ParamX")/time;
					int ySpeed=(int)json.get("ParamY")/time;
					int thetaSpeed=(int)json.get("ParamTheta")/time;
					
					for (Feature feat : this.features){
			        	if (feat.getClass().getName().equals("Move")){
			        		((Move)(feat)).setParameters(xSpeed,ySpeed,thetaSpeed,time);
			        	}
			        }
		
					word="Move";
					break;
				case "Walk" :
					word="Walk";
					break;
				case "Video" :
					word="Video";
					break;
				case "Init" :
					word="Init";
					break;
				case "Stop" :
					word="Stop";
					break;
				default : System.out.println("it must have not happened ...");
				}
				
				// execution of the feature
				String ip="193.48.125.37";
				for (Robotino rob : this.robots){
		        	if (rob.getIpAdress().equals(ip)){
		        		for (Feature feat : this.features){
		                	if (feat.getClass().getName().equals(word)){
		                		runFeature(feat,rob);
		                	}	
		                }
		        	}	
		        }
			}
			else System.out.println("This message is not an order");
		}
		else System.out.println("This message is not for me");
	}
	
	
	
    public static void main(String[] args)
    {
    	//try {
    	ApplicationRobotino applicationTest = new ApplicationRobotino();
    	
    	ExecutorService es = Executors.newFixedThreadPool(13); //Allow 10 connections (devices and robots mingled)
    	applicationTest.client=new Client(es,applicationTest);
    	
    	int iTestCo = applicationTest.client.connexion("193.48.125.68");
		
		if(iTestCo == 1){
			System.out.println("Connected");
			es.execute(applicationTest.client);
			
			
			String ip ="193.48.125.37";
			
			
			// creation d'un move en particulier
	        for (Feature feat : applicationTest.features){
	        	if (feat.getClass().getName().equals("Move")){
	        		((Move)(feat)).setParameters(0,0,60,3000);
	        	}
	        }
	        
	     // pour un robot
	        String word = "Move";
	        for (Robotino rob : applicationTest.robots){
	        	if (rob.getIpAdress().equals(ip)){
	        		for (Feature feat : applicationTest.features){
	                	if (feat.getClass().getName().equals(word)){
	                		runFeature(feat,rob);
	                	}	
	                }
	        	}	
	        }
	        
	        
		} else System.out.println("Not Connected");
    	
    	// initialisation du Scanner pour les entrees au clavier 
    	/*Scanner sc = new Scanner(System.in); 
    	System.out.println("Please enter the Ip Adress of the Com' Manager (193.48.125.68)"); 
    	String str = sc.nextLine(); 
    	System.out.println("You said : " + str);
    	*/
    	
        
        
    	
        
        
        
        /*Feature m1 = new Move(60,0,0,500);
        Feature m2 = new Move(0,0,-90,500);
        Feature i1 = new InitRobot();
        Feature m3 = new Move (0,60,0,500);
        Feature m4 = new Move (0,0,60,3000);
        applicationTest.features.add(m1);
        applicationTest.features.add(m2);
        applicationTest.features.add(i1);
        applicationTest.features.add(m3);
        applicationTest.features.add(m4);*/
        //new Robotino(hostname).run();
        
        
        
        // pour deux robots
        /*String word = "Walk";
        for (Robotino rob : applicationTest.robots){
        		for (Feature feat : applicationTest.features){
                	if (feat.getClass().getName().equals(word)){
                		runFeature(feat,rob);
                	}	
                }
        		
        }*/
        
        
        
        
        //runFeature(m2,robotTest);
    	
    
    /*}
        
        // gestion des exceptions
        catch (UnknownHostException e) {	
            e.printStackTrace();
        }
        catch (IOException e) {
        	e.printStackTrace();
        }*/	
        
    }

}