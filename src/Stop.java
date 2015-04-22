public class Stop extends Feature {
	
	private String name;
	
	// getters
	
	// getName
	/**
     * to get the name of the feature
     * 
     * @return the name of the feature
     */
	public String getName(){
		return name;
	}
	
	// constructor
	/**
     * to create new stop object 
     */
	public Stop() {
		name="Stop";
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
