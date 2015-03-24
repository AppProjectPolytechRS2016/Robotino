import java.awt.Image;


public class Move extends Feature {
	
	// attributes
	private String name;
	private int xSpeed;
	private int ySpeed;
	private int thetaSpeed;
	private int time;
	protected final float[] startVector = new float[]
	{
	    200.0f, 0.0f
	};

	// getters
	
	public String getName() {
		return name;
	}
	
	public int getxSpeed() {
		return xSpeed;
	}
	
	public int getySpeed() {
		return ySpeed;
	}

	public int getThetaSpeed() {
		return thetaSpeed;
	}
	
	public int getTime() {
		return time;
	}
	
	// setters
	
	public void setxSpeed(int xSpeed) {
		this.xSpeed = xSpeed;
	}

	public void setySpeed(int ySpeed) {
		this.ySpeed = ySpeed;
	}

	public void setThetaSpeed(int thetaSpeed) {
		this.thetaSpeed = thetaSpeed;
	}

	public void setTime(int time) {
		this.time = time;
	}
	
	public void setParameters(int xSpeed, int ySpeed, int thetaSpeed, int time){
		setxSpeed(xSpeed);
		setySpeed(ySpeed);
		setThetaSpeed(thetaSpeed);
		setTime(time);
	}
	
	// constructor
	/**
     * to create new ApplicationRobotino object 
     */
	public Move(int xSpeedInt, int ySpeedInt, int thetaSpeedInt, int timeInt){
		this.name="move";
		this.xSpeed=xSpeedInt;
		this.ySpeed=ySpeedInt;
		this.thetaSpeed=thetaSpeedInt;
		this.time=timeInt;
	} 
	
	// runFeature
	/**
     * to execute the feature on a particular robot
     * 
     * @param robot the robot on which we want to run the feature
     */
	
	public void runFeature (Robotino robotino) {
	    
	        long startTime = System.currentTimeMillis();
	        long elapsedTime=0;
	        
	        while (!Thread.interrupted() 
	        		&& robotino.com.isConnected() 
	        		&& false == robotino.bumper.value()
	        		&&(elapsedTime<time)
	        		&&(robotino.dist.voltage()<0.5))
	        {
				elapsedTime = System.currentTimeMillis() - startTime;
	            robotino.omniDrive.setVelocity(xSpeed, ySpeed, thetaSpeed);
	            robotino.com.waitForUpdate();
	            
	            
	        } 
	}
}
