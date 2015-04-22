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
	private GraphicInterface app;
	
	
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
		
		try {
			ipHost=InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		app=new GraphicInterface(this);
		
		
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
					if (!(json.containsKey("XValue"))){
						System.out.println("Default move");
					}
					else {
						// we fix the time at 3 seconds
						float time=3000;
						
						// we get the parameters from the JSON
						float xSpeed=(long)json.get("XValue")*10000/time;
						float ySpeed=(long)json.get("YValue")*10000/time;
						float thetaSpeed=(long)json.get("ThetaValue")*1000/time;
						
						for (Feature feat : this.features){
				        	if (feat.getClass().getName().equals("Move")){
				        		((Move)(feat)).setParameters(xSpeed,ySpeed,thetaSpeed,time);
				        		System.out.println("move modified : x ="+xSpeed+" y = "+ySpeed+" theta = "+thetaSpeed);
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
					
				case "Disconnect":
					word="Disconnect";
					break;
					
				default : System.out.println("it must have not happened ...");
				}
				
				if (word.equals("Stop")||word.equals("Disconnect")){
					//client.deco();
					// not correct to do it there because the Json message for the end is send after
					System.out.println("the order is : "+word);
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
    	ApplicationRobotino applicationTest = new ApplicationRobotino();
    	
    	ExecutorService es = Executors.newFixedThreadPool(100); //Allow 100 connections (devices and robots mingled)
    	applicationTest.client=new Client(es,applicationTest);
    	
    	int iTestCo = applicationTest.client.connection(applicationTest.GestCom_IP);
		
		if(iTestCo == 1){
			System.out.println("Connected");
			es.execute(applicationTest.client);
	      
		} else System.out.println("Not Connected");
    	
 
    }

}