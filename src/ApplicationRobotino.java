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
	private String GestCom_IP="193.48.125.64";
	private static int GestCom_PORT = 6030;
	public Client client;
	private String ipHost;
	private String ipRobot="193.48.125.37";
	
	
	// constructor
	/**
     * to create new ApplicationRobotino object 
     */
	public ApplicationRobotino(){
		
		features=new ArrayList<Feature>();
		robots=new ArrayList<Robotino>();
	
		Feature move = new Move(100,0,60,3000);
		features.add(move);
		Feature init = new Init();
		features.add(init);
		Feature walk = new Walk(this);
		features.add(walk);
		Feature stop = new Stop();
		features.add(stop);
		
		String hostname1 = System.getProperty("hostname", ipRobot);
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
	
	// getClient
	/**
     * to get the Client
     * 
     * @return the client of applicationRobotino
     */
	public Client getClient(){
		return client;
	}
	
	// getIpRobot
	/**
     * to get the ip of the robot we want to use
     * 
     * @return the ip of the robot we want to use
     */
	public String getIpRobot(){
		return ipRobot;
	}
	
	// getFeatures
	/**
     * to get the list of the features avalaible on the robot
     * 
     * @return the list of the features avalaible on the robot
     */
	public ArrayList<Feature> getFeatures(){
		return features;
	}
	
	// getRobots
	/**
     * to get the list of the robots avalaible to be used by applicationRobotino
     * 
     * @return the list of the robots avalaible to be used by applicationRobotino
     */
	public ArrayList<Robotino> getRobots(){
		return robots;
	}
	
	// getIpHost
	/**
     * to get the ip of the computer on which is the applicationRobotino
     * 
     * @return the ip of the computer on which is the applicationRobotino
     */
	public String getIpHost(){
		return ipHost;
	}
	
	// getIpGestCom
	/**
     * to get the ip of the ComManager
     * 
     * @return the ip of the ComManager
     */
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
			while (robot.getBusy()){
				System.out.println("wait ...");
			}
			feature.runFeature(robot);
			}
		catch (Exception e)
	        {
	            e.printStackTrace();
	        }
	}
	
	// interpretFeature
	/**
     * to do a particular action because of the Json object the client received
     * 
     * @param json the message that has been received
     */
	public void interpretFeature(JSONObject json){
		String to = (String)json.get("To");
		String from = (String)json.get("from");
		String msgType = (String)json.get("MsgType");
		if (to.equals(this.ipHost)){
			if (msgType.equals("Order")){
				
				String word = "error";

				switch ((String)json.get("OrderName")){
				
				case "Move" :
					if (!(json.containsKey("ParamX"))){
						System.out.println("Default move");
					}
					else {
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
					
				case "ConnectTo":
					word="Init";
					break;
					
				default : System.out.println("it must have not happened ...");
				}
				
				if (word.equals("Stop")){
					client.deco();
				}
				
				else {
					// execution of the feature
					for (Robotino rob : this.robots){
			        	if (rob.getIpAdress().equals(ipRobot)){
			        		for (Feature feat : this.features){
			                	if (feat.getClass().getName().equals(word)){
			                		System.out.println(json.toString());
			                		runFeature(feat,rob);
			                	}	
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
    	
    	int iTestCo = applicationTest.client.connexion(applicationTest.GestCom_IP);
		
		if(iTestCo == 1){
			System.out.println("Connected");
			es.execute(applicationTest.client);
			
			
			
			/*
			// creation d'un move en particulier
	        for (Feature feat : applicationTest.features){
	        	if (feat.getClass().getName().equals("Move")){
	        		((Move)(feat)).setParameters(0,0,60,3000);
	        	}
	        }
	        
	     // pour un robot
	        String word = "Walk";
	        for (Robotino rob : applicationTest.robots){
	        	if (rob.getIpAdress().equals(applicationTest.ipRobot)){
	        		for (Feature feat : applicationTest.features){
	                	if (feat.getClass().getName().equals(word)){
	                		runFeature(feat,rob);
	                	}	
	                }
	        	}	
	        }
	     */
	      
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