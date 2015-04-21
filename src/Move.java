import java.awt.Image;


public class Move extends Feature {
	
	// attributes
	private String name;
	private float xSpeed;
	private float ySpeed;
	private float thetaSpeed;
	private float time;
	protected final float[] startVector = new float[]
	{
	    200.0f, 0.0f
	};

	// getters
	
	public String getName() {
		return name;
	}
	
	public float getxSpeed() {
		return xSpeed;
	}
	
	public float getySpeed() {
		return ySpeed;
	}

	public float getThetaSpeed() {
		return thetaSpeed;
	}
	
	public float getTime() {
		return time;
	}
	
	// setters
	
	public void setxSpeed(float xSpeed) {
		this.xSpeed = xSpeed;
	}

	public void setySpeed(float ySpeed) {
		this.ySpeed = ySpeed;
	}

	public void setThetaSpeed(float thetaSpeed) {
		this.thetaSpeed = thetaSpeed;
	}

	public void setTime(float time) {
		this.time = time;
	}
	
	public void setParameters(float xSpeed, float ySpeed, float thetaSpeed, float time){
		setxSpeed(xSpeed);
		setySpeed(ySpeed);
		setThetaSpeed(thetaSpeed);
		setTime(time);
	}
	
	// constructor
	/**
     * to create new ApplicationRobotino object 
     */
	public Move(float xSpeedInt, float ySpeedInt, float thetaSpeedInt, float timeInt){
		this.name="Move";
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
			robotino.setBusy(true);
	        long startTime = System.currentTimeMillis();
	        long elapsedTime=0;
	        
	        while (!Thread.interrupted() 
	        		&& robotino.com.isConnected() 
	        		&& false == robotino.bumper.value()
	        		&&(elapsedTime<time))
	        		//&&(robotino.dist.voltage()<0.5))
	        {
				elapsedTime = System.currentTimeMillis() - startTime;
	            robotino.omniDrive.setVelocity(xSpeed, ySpeed, thetaSpeed);
	            robotino.com.waitForUpdate();
	            
	            
	        }
	        if (!Thread.interrupted() 
	        		&& robotino.com.isConnected()){
	        	robotino.omniDrive.setVelocity(0, 0, 0);
	        }
			robotino.setBusy(false);

	}
}
