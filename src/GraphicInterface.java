// imports
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class GraphicInterface extends JFrame implements ActionListener{
	
	// attributes and graphic elements
	
	JPanel panel;
    JButton b1;
    JButton b2;
    JButton b3;
    JButton b4;
    JButton b5;
    
    ApplicationRobotino app;
	
	// constructor
	/**
	 * Graphic interface constructor
	 */
	public GraphicInterface (ApplicationRobotino appli){

        super();
        setTitle("Application Robotino");
        setSize(400, 200);
        Container contentPane = getContentPane();
        panel = new JPanel();
		
		// creation of the move button + actions
        
        b1 = new JButton("Move");
        b1.addActionListener(this);
        b1.setActionCommand("move");
        b1.setToolTipText("run the default move feature on robotino");
        panel.add(b1);
    
        // creation of the init button + actions
        
        b2 = new JButton("Init");
        b2.addActionListener(this);
        b2.setActionCommand("init");
        b2.setToolTipText("run the init feature on robotino");
        panel.add(b2);
    
        // creation of the walk button + actions
    
        b3 = new JButton("Walk");
        b3.addActionListener(this);
        b3.setActionCommand("walk");
        b3.setToolTipText("run the walk feature on robotino");
        panel.add(b3);
        
        // creation of the stop button + actions
        
        b5 = new JButton("Stop");
        b5.addActionListener(this);
        b5.setActionCommand("stop");
        b5.setToolTipText("run the stop feature on robotino");
        panel.add(b5);
        
        // creation of the quit button + actions
        
        b4 = new JButton("Quit");
        b4.addActionListener(this);
        b4.setActionCommand("quit");
        b4.setToolTipText("To close the application");
        panel.add(b4);
		
        contentPane.add(panel, BorderLayout.CENTER);
        
        app= appli;
        
        setVisible(true);
	}
	
    
    // quit
 	/**
 	 * to quit the application
 	 */
     public void quit(){
         int reponse =
         JOptionPane.showConfirmDialog(
                                       this,
                                       "Do you really want to quit ?",
                                       "Quit",
                                       JOptionPane.YES_NO_OPTION);
         if (reponse == JOptionPane.YES_OPTION) {
             setVisible(false);
             System.exit(0);
         }
     }
     
     
     // action
 	/**
      * to execute the correct feature on the correct robot
      * 
      * @param word the name of the feature
      */
     public void action (String word){
    	 Feature detectedFeature=null;
    	 Robotino detectedRobot=null;
    	 for (Robotino rob : app.getRobots()){
         	if (rob.getIpAdress().equals(app.getIpRobot())){
         		detectedRobot=rob;
         		for (Feature feat : app.getFeatures()){
                 	if (feat.getClass().getName().equals(word)){
                 		detectedFeature = feat;
                 	}	
                 }
         	}	
         }
    	 app.runFeature(detectedFeature,detectedRobot);
     }
     
    // actionPerformed
 	/**
 	 * to make the link between the click on a button and the action we want
 	 * @param e the event created by the click
 	 */
	public void actionPerformed(ActionEvent e) {
		String word="";
		System.out.println(e.getActionCommand());
		switch (e.getActionCommand()){
         
		// move action
		case "move" :
            word="Move";
            action(word);
            System.out.println("fini l'action");
            break;
            
		// init action
        case "init" :
            word="Init";
            action(word);
            break;
        
        // stop action
        case "stop" :
        	word="Stop";
        	action(word);
            break;
            
        // walk action
        case "walk" :
        	word="Walk";
        	action(word);
            break;
            
        // quit action 
        case "quit":
            quit();
            break;
            
        default : System.out.println("it must not have happened ");
		}
		
	}

}

