import java.util.ArrayList;


public class Init extends Feature {
	
	private String name;
	private ArrayList<Feature> movements;
	
	// getters
	
	public String getName(){
		return name;
	}
	
	// constructor
	/**
     * to create new ApplicationRobotino object 
     */
	public Init() {
		movements=new ArrayList<Feature>();
		movements.add(new Move(0,0,90,500));
		movements.add(new Move(0,0,-90,350));	
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
