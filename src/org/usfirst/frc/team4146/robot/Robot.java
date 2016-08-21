
package org.usfirst.frc.team4146.robot;


import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Main Robot class used for defining the auto and operator controlled methods
 * @author GowanR
 * @version 8/21/2016
 */
public class Robot extends SampleRobot{
    Controller drive_controller; // Driver's controller
    final String defaultAuto = "Default";
    final String customAuto = "My Auto";
    SendableChooser chooser;
    DriveTrain drive;
    Shooter shooter;
    /**
     *	Constructor for Robot will instantiate all subsystems and controllers. 
     */
    public Robot() {
        drive_controller = new Controller( 0 );
        drive = new DriveTrain( drive_controller, this );
        shooter = new Shooter( drive_controller );
    }
    /**
     * Starts the Robot ( for Driver Station and deployment purposes. )
     */
    public void robotInit() {
        chooser = new SendableChooser();
        chooser.addDefault("Default Auto", defaultAuto);
        chooser.addObject("My Auto", customAuto);
        SmartDashboard.putData("Auto modes", chooser);
    }

	/**
	 * Autonomous mode. This will run when autonomous mode is initialized.
	 * ( Not implemented yet. )
	 */
    public void autonomous() {
    	
    	String autoSelected = (String) chooser.getSelected();
		System.out.println("Auto selected: " + autoSelected);
    	
    	switch(autoSelected) {
    	case customAuto:
            break;
    	case defaultAuto:
    	default:
            break;
    	}
    }

    /**
     * Runs Robot updating all substsyems.
     */
    public void operatorControl() {
    	
        while (isOperatorControl() && isEnabled()) {
            drive.update();
            shooter.update();
            Timer.delay(0.005);		// wait for a motor update time
        }
        
    }

    /**
     * Runs during test mode
     */
    public void test() {
    }
}
