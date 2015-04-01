
import rec.robotino.com.Bumper;
import rec.robotino.com.Camera;
import rec.robotino.com.Com;
import rec.robotino.com.Motor;
import rec.robotino.com.OmniDrive;
import rec.robotino.com.DistanceSensor;

public class Robotino implements Runnable
{
	// attributes
    protected final String hostname;
    protected final Com com;
    protected final Motor motor1;
    protected final Motor motor2;
    protected final Motor motor3;
    protected final OmniDrive omniDrive;
    protected final Bumper bumper;
    protected final float[] startVector = new float[]
    {
        200.0f, 0.0f
    };
    private String ipAdress;
    private State robotState;
    private State videoState;
    private final RobotType type=RobotType.Robotino;
    protected final DistanceSensor dist;
    boolean busy;
    protected final Camera cam;
    
    // getters
    
    public State getRobotState(){
    	return(robotState);
    }
    
    public State getVideoState(){
    	return(videoState);
    }
    
    public String getIpAdress(){
    	return(ipAdress);
    }
    
    public RobotType getRobotType(){
    	return(type);
    }
    
    // setters
    
    public void setRobotState(State state){
    	this.robotState=state;
    }

    public void setVideoState(State state){
    	this.videoState=state;
    }

    // constructor
	/**
     * to create and initialize new Robotino object 
     */
    public Robotino(String hostname)
    {
        this.hostname = hostname;
        com = new Com();
        motor1 = new Motor();
        motor2 = new Motor();
        motor3 = new Motor();
        omniDrive = new OmniDrive();
        bumper = new Bumper();
        dist=new DistanceSensor();
        this.ipAdress=hostname;
        this.robotState=State.Free;
        this.videoState=State.Free;
        busy=false;
        cam=new Camera();
        
        // initialisation
        init();
        
        // connexion
        connect(this.hostname);
    }

    // init
	/**
     * to initialize a Robotino object 
     */
    protected void init()
    {
        motor1.setComId(com.id());
        motor1.setMotorNumber(0);

        motor2.setComId(com.id());
        motor2.setMotorNumber(1);

        motor3.setComId(com.id());
        motor3.setMotorNumber(2);

        omniDrive.setComId(com.id());

        bumper.setComId(com.id());
        
        dist.setComId(com.id());
        dist.setSensorNumber(0);
        cam.setComId(com.id());
    } 
    
    // setBusy
 	/**
      * to modify the state of robotino
      * @param buzy the new state of the robotino
      */
    public void setBusy(boolean buzy){
    	busy=buzy;
    }
    
    // getBusy
  	/**
       * to modify the state of robotino
       */
     public boolean getBusy(){
     	return(busy);
     }

    // connect
	/**
     * to connect a Robotino object 
     * @param hostname the ip adress of the robot we want to connect to
     */
    protected void connect(String hostname)
    {
        com.setAddress(hostname);
        com.connect();
    }

    // disconnect
	/**
     * to disconnect a Robotino object 
     */
    protected void disconnect()
    {
        com.disconnect();
    }
    
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
