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
	
	// getName
	/**
     * to get the name of the feature
     * 
     * @return the name of the feature
     */
	public String getName() {
		return name;
	}
	
	// getxSpeed
	/**
     * to get the xSpeed of the move
     * 
     * @return the xSpeed of the move
     */
	public float getxSpeed() {
		return xSpeed;
	}
	
	// getySpeed
	/**
     * to get the ySpeed of the move
     * 
     * @return the ySpeed of the move
     */
	public float getySpeed() {
		return ySpeed;
	}

	// getThetaSpeed
	/**
     * to get the thetaSpeed of the move
     * 
     * @return the thetaSpeed of the move
     */
	public float getThetaSpeed() {
		return thetaSpeed;
	}
	
	// getTime
	/**
     * to get the time of the move
     * 
     * @return the time of the move
     */
	public float getTime() {
		return time;
	}
	
	// setters
	
	// setxSpeed
	/**
     * to set the xSpeed of the move
     * 
     * @param xSpeed the xSpeed of the move
     */
	public void setxSpeed(float xSpeed) {
		this.xSpeed = xSpeed;
	}

	// setySpeed
	/**
     * to set the ySpeed of the move
     * 
     * @param ySpeed the ySpeed of the move
     */
	public void setySpeed(float ySpeed) {
		this.ySpeed = ySpeed;
	}

	// setThetaSpeed
	/**
     * to set the thetaSpeed of the move
     * 
     * @param thetaSpeed the thetaSpeed of the move
     */
	public void setThetaSpeed(float thetaSpeed) {
		this.thetaSpeed = thetaSpeed;
	}

	// setTime
	/**
     * to set the time of the move
     * 
     * @param time the time of the move
     */
	public void setTime(float time) {
		this.time = time;
	}
	
	// setParameters
	/**
     * to set all the parameters of the move
     * 
     * @param xSpeed the xSpeed of the move
     * @param ySpeed the ySpeed of the move
     * @param thetaSpeed the thetaSpeed of the move
     * @param time the time of the move
     */
	public void setParameters(float xSpeed, float ySpeed, float thetaSpeed, float time){
		setxSpeed(xSpeed);
		setySpeed(ySpeed);
		setThetaSpeed(thetaSpeed);
		setTime(time);
	}
	
	// constructor
	/**
     * to create new Move object 
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
