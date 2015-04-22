import java.util.ArrayList;


public class Walk extends Feature {
	
	
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
     * to create new walk object 
     */
	public Walk(ApplicationRobotino appli) {
		name="Walk";	
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
        
        Feature rotate = new Move(0,0,0,3000);
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
            System.out.println(robotino.dist.voltage());
            while((robotino.dist.voltage()>0.5)&&(elapsedTime2<timelimit)){
            	
            	elapsedTime2 = System.currentTimeMillis() - startTime2;
            	if (!RR && !RR){
            		nb=Math.random();
            		
                	if (nb<(double)(0.5)){
                		angle=-90;
                		RL=true;
                	}
                	else {
                		angle=90;
                		RR=true;
                	}
            	}
            	else if (RL){
            		
            		angle=-90;   
            		RL=true;
            	}
            	else {
            		
            		angle=90;
            		RR=true;
            	}
            	((Move)rotate).setParameters(0,0,(int)(angle/1.5),1500);
            	ApplicationRobotino.runFeature(rotate,robotino);
            	
            }
            
        }
        
        ((Move)rotate).setParameters(0,0,0,10);
    	ApplicationRobotino.runFeature(rotate,robotino);
	}

}