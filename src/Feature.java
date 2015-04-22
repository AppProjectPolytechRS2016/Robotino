
public class Feature {
	
	// attributes
	protected String name;
	
	// getName
	/**
     * to get the name of the feature
     * 
     * @return the name of the feature
     */
	public String getName(){
		return name; 
	}
	
	// runFeature
	/**
     * to execute the feature on a particular robot
     * 
     * @param robotino the robot we want to execute the feature on
     */
	public void runFeature(Robotino robotino)throws InterruptedException{}
	
}
