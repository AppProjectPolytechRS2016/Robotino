import java.util.ArrayList;


public class Walk extends Feature {
	
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
	public Walk() {
		name="walk";
		movements=new ArrayList<Feature>();
		movements.add(new Move(0,0,90,500));
		movements.add(new Move(60,0,0,3000));
		movements.add(new Move(-60,60,0,3000));
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