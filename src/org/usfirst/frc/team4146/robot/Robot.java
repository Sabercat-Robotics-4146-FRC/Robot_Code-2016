
package org.usfirst.frc.team4146.robot;


import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

/**
 * Main Robot class used for defining the auto and operator controlled methods
 * @author GowanR
 * @version 8/21/2016
 */
public class Robot extends SampleRobot{
	EventLoop main_event_loop;
	NetworkTable network_table;
    Controller drive_controller; // Driver's controller
    Controller arm_controller;
    final String defaultAuto = "Default";
    final String customAuto = "My Auto";
    SendableChooser chooser;
    DriveTrain drive;
    Shooter shooter;
    LifterArm lifter_arm;
    /**
     *	Constructor for Robot will instantiate all subsystems and controllers. 
     */
    public Robot() {
    	network_table = NetworkTable.getTable("SmartDashboard");
    	main_event_loop = new EventLoop();
        drive_controller = new Controller( 0 );
        arm_controller = new Controller( 1 );
        Event drive_start = drive_controller.bind( Controller.start_button );
        Event arm_start = arm_controller.bind( Controller.start_button );
        drive_start.then( new Event( new attr() {
        	public boolean poll(){
        		System.out.println( "Driver Controller." );
        		return true;
        	}
        	public void callback(){}
        	public boolean complete(){ return true; }
        }));
        arm_start.then( new Event( new attr() {
        	public boolean poll(){
        		System.out.println( "Arm Controller." );
        		return true;
        	}
        	public void callback(){}
        	public boolean complete(){ return true; }
        }));
        main_event_loop.on( drive_start );
        main_event_loop.on( arm_start );
       // lifter_arm = new LifterArm( arm_controller, main_event_loop );
        drive = new DriveTrain( drive_controller, main_event_loop, this );
        shooter = new Shooter( drive_controller, main_event_loop );
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
    AHRS gyro;
    public PID drive_angle;
    /**
     * Runs Robot updating all subsystems.
     */
    public void operatorControl() {
    	drive_angle = new PID( new signal(){
    		public double getValue() {
    			double a = gyro.getAngle();
    			a = Math.abs( a );
    			while ( a >= 360 ) {
    				a -= 360;
    			}
    			double b = Math.abs(a) % 360;
    			if ( a > 180 ) {
    				a = -1 * ( 180 - ( a - 180 ) );
    			}
    			return a;
    		}
    	} );
    	drive_angle.set_pid( 0.01, 0.00000000001, 0.0000001 );
    	//drive_angle.set_pid( 0.01, 0.00000000001, 0.0000001 );
    	drive_angle.set_setpoint( 0.0 );
    	gyro = new AHRS( SPI.Port.kMXP );
    	(new Thread( main_event_loop )).start();
    	long startTime;    
    	double dt = 1e-3;
        while ( isOperatorControl() && isEnabled() ) {
        	startTime = System.nanoTime();   
        	drive_angle.update( dt );
            Timer.delay(0.005);		// wait for a motor update time
            dt = (double)( System.nanoTime() - startTime ) / 1e9;
        }
    }

    /**
     * Runs during test mode
     */
    public void test() {
    }
}
