import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// port of the c++ example
// without callbacks, as java does not support them directly

public class ApplicationRobotino
{
	// attributes
	private ArrayList<Feature> features;
	private ArrayList<Robotino> robots;
	private String GestCom_IP;
	private static int GestCom_PORT = 6030;
	private Client client;
	
	
	// constructor
	/**
     * to create new ApplicationRobotino object 
     */
	public ApplicationRobotino(){
		
		features=new ArrayList<Feature>();
		robots=new ArrayList<Robotino>();
	
		Feature move = new Move(0,0,0,0);
		features.add(move);
		Feature init = new InitRobot();
		features.add(init);
		Feature walk = new Walk();
		features.add(walk);
		Feature stop = new StopRobot();
		features.add(stop);
		
		String hostname1 = System.getProperty("hostname", "193.48.125.37");
		Robotino robotino1 = new Robotino(hostname1);
		robots.add(robotino1);
		
		/*String hostname2 = System.getProperty("hostname", "193.48.125.38");
		Robotino robotino2 = new Robotino(hostname2);
		robots.add(robotino2);*/
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
	
	public String translateFeature(String mot){
		// parametres du Json a recuperer
		switch (mot){
		case "Move" :
			break;
		case "Walk" :
			break;
		case "Video" :
			break;
		case "InitRobot" :
			break;
		case "StopRobot" :
			break;
		case "ServerConnexion" :
			break;
		case "ServerDisconnexion" :
			break;
		default : System.out.println("it must have not happened ...");
		}
		return mot;
	}
	
	
	
    public static void main(String[] args)
    {
    	//try {
    	ApplicationRobotino applicationTest = new ApplicationRobotino();
    	
    	/*ExecutorService es = Executors.newFixedThreadPool(13); //Allow 10 connections (devices and robots mingled)
    	applicationTest.client=new Client(es);
    	
    	int iTestCo = applicationTest.client.connexion("193.48.125.68");
		
		if(iTestCo == 1){
			System.out.println("Connected");
			es.execute(applicationTest.client);
		} else System.out.println("Not Connected");*/
    	
    	// initialisation du Scanner pour les entrees au clavier 
    	/*Scanner sc = new Scanner(System.in); 
    	System.out.println("Please enter the Ip Adress of the Com' Manager (193.48.125.68)"); 
    	String str = sc.nextLine(); 
    	System.out.println("You said : " + str);
    	*/
    	
        String ip ="193.48.125.37";
        
    	
        
        // creation d'un move en particulier
        for (Feature feat : applicationTest.features){
        	if (feat.getClass().getName().equals("Move")){
        		((Move)(feat)).setParameters(60,60,0,3000);
        	}
        }
        
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
        
        
        String word = "Walk";
        for (Robotino rob : applicationTest.robots){
        	if (rob.getIpAdress().equals(ip)){
        		for (Feature feat : applicationTest.features){
                	if (feat.getClass().getName().equals(word)){
                		runFeature(feat,rob);
                	}	
                }
        	}	
        }
        
        
        
        
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