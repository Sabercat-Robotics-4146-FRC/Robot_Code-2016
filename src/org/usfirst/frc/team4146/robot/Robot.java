
package org.usfirst.frc.team4146.robot;


import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

/**
 * Main Robot class used for defining the auto and operator controlled methods
 * @author GowanR
 * @version 8/21/2016
 */
public class Robot extends SampleRobot{
	NetworkTable network_table;
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
    	network_table = NetworkTable.getTable("SmartDashboard");
    	//Debug angle pid
    	network_table.putNumber( "set angle", 0.0 );
    	network_table.putNumber( "Kp", 0.2 );
    	network_table.putNumber( "Ki", 0.0 );
    	network_table.putNumber( "Kd", 0.0 );
    	network_table.putNumber( "Kf", 0.0 );
        drive_controller = new Controller( 0 );
        drive = new DriveTrain( drive_controller, this );
        shooter = new Shooter( drive_controller );
    }
    /**
     * Starts the Robot ( for Driver Station and deployment purposes. )
     */
    public void robotInit() {
        /*chooser = new SendableChooser();
        chooser.addDefault("Default Auto", defaultAuto);
        chooser.addObject("My Auto", customAuto);
        SmartDashboard.putData("Auto modes", chooser);*/
    }

	/**
	 * Autonomous mode. This will run when autonomous mode is initialized.
	 * ( Not implemented yet. )
	 */
    public void autonomous() {
    	
    	/*String autoSelected = (String) chooser.getSelected();
		System.out.println("Auto selected: " + autoSelected);
    	
    	switch(autoSelected) {
    	case customAuto:
            break;
    	case defaultAuto:
    	default:
            break;
    	}*/
    }

    /**
     * Runs Robot updating all subsystems.
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
