public class Stop extends Feature {
	
	private String name;
	
	// getters
	
	public String getName(){
		return name;
	}
	
	// constructor
	/**
     * to create new ApplicationRobotino object 
     */
	public Stop() {
		name="stop";
	}
	
	// runFeature
	/**
     * to execute the feature on a particular robot
     * 
     * @param robot the robot on which we want to run the feature
     */
	public void runFeature (Robotino robotino) {
		robotino.disconnect();
	}
}
