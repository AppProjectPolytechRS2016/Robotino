import java.util.ArrayList;


public class Init extends Feature {
	
	// attributes
	private String name;
	private ArrayList<Feature> movements;
	
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
     * to create new init object 
     */
	public Init() {
		this.name="Init";
		movements=new ArrayList<Feature>();
		movements.add(new Move(0,0,90,500));
		movements.add(new Move(0,0,-90,500));	
	}
	
	// runFeature
	/**
     * to execute the feature on a particular robot
     * 
     * @param robot the robot on which we want to run the feature
     */
	public void runFeature (Robotino robotino) {
		
		for (Feature feat : movements){
			ApplicationRobotino.runFeature(feat,robotino);
		}
		
	}

}
