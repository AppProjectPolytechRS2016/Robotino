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
		
		long startTime2 = System.currentTimeMillis();
        long elapsedTime2=0;
        
        // rotate right
        boolean RR=false;
        // rotate left
        boolean RL=false;
        
        Feature rotate = new Move(0,0,-0,3000);
        double nb;
        int angle=0;
        int timelimit=30000;

        while (elapsedTime2<timelimit)
        {
        	RR=false;
        	RL=false;
			elapsedTime2 = System.currentTimeMillis() - startTime2;
			System.out.println(elapsedTime2);
            robotino.omniDrive.setVelocity(100, 0, 0);
            robotino.com.waitForUpdate();
             
            while((robotino.dist.voltage()>1)&&(elapsedTime2<timelimit)){
            	elapsedTime2 = System.currentTimeMillis() - startTime2;
            	if (!RR && !RR){
            		nb=Math.random();
                	if (nb<0.5){
                		angle=-90;
                	}
                	else {
                		angle=90;
                	}
            	}
            	else if (RL){
            		angle=-90;            		
            	}
            	else {
            		angle=90;
            	}
            	((Move)rotate).setParameters(0,0,(int)(angle/1.5),1500);
            	ApplicationRobotino.runFeature(rotate,robotino);
            	RL=(angle==-90);
            	RR=(angle==90);
            }
            
        } 
		
//		for (Feature feat : movements){
//			ApplicationRobotino.runFeature(feat,robotino);
//		}
	}

}