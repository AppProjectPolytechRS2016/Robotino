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

public class GraphiqueInterface extends JFrame implements ActionListener{
	
	// attributs et elements graphiques
	
	JPanel panneau;
    JButton b1;
    JButton b2;
    JButton b3;
    JButton b4;
    JButton b5;
    
    ApplicationRobotino app;
	
	// constructeurs
	
	/**
	 * constructeur de gestionnaire par defaut
	 */
	public GraphiqueInterface (ApplicationRobotino appli){
		
		// initialisation de la fenetre graphique

        super();
        setTitle("Application Robotino");
        setSize(400, 200);
        Container contentPane = getContentPane();
        panneau = new JPanel();
		
		// creation du bouton move + actions liees
        
        b1 = new JButton("Move");
        b1.addActionListener(this);
        b1.setActionCommand("move");
        b1.setToolTipText("run the default move feature on robotino");
        panneau.add(b1);
    
        // creation du bouton membre + actions liees
    
        b2 = new JButton("Init");
        b2.addActionListener(this);
        b2.setActionCommand("init");
        b2.setToolTipText("run the init feature on robotino");
        panneau.add(b2);
    
        // creation du bouton employe + actions liees
    
        b3 = new JButton("Walk");
        b3.addActionListener(this);
        b3.setActionCommand("walk");
        b3.setToolTipText("run the walk feature on robotino");
        panneau.add(b3);
        
        // creation du bouton employe + actions liees
        
        b5 = new JButton("Stop");
        b5.addActionListener(this);
        b5.setActionCommand("stop");
        b5.setToolTipText("run the stop feature on robotino");
        panneau.add(b5);
        
        // creation du bouton quitter + actions liees
        
        b4 = new JButton("Quit");
        b4.addActionListener(this);
        b4.setActionCommand("quit");
        b4.setToolTipText("To close the application");
        panneau.add(b4);
		
        contentPane.add(panneau, BorderLayout.CENTER);
        
        app= appli;
        
        setVisible(true);
	}
	
    
    // quitter
 	/**
 	 * permet d'ouvrir une fenetre qui demande la confirmation avant de quitter l'application
 	 */
     public void quitter(){
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
 	 * permet de traiter les actions engendrees par le clic sur un bouton
 	 * @param e l'evenement cree par un clic sur un bouton
 	 */
	public void actionPerformed(ActionEvent e) {
		String word="";
		System.out.println(e.getActionCommand());
		switch (e.getActionCommand()){
         
		// connection en tant qu'utilisateur
		case "move" :
            word="Move";
            action(word);
            System.out.println("fini l'action");
            break;
            
		// connection d'un membre
        case "init" :
            word="Init";
            action(word);
            break;
        
        // connection d'un employe
        case "stop" :
        	word="Stop";
        	action(word);
            break;
            
         // affichage de la liste d'emprunts
        case "walk" :
        	word="Walk";
        	action(word);
            break;
            
        // quitter
        case "quit":
            quitter();
            break;
            
        default : System.out.println("it must not have happened ");
		}
		
	}

}

