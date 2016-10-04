package org.usfirst.frc.team4146.robot;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;

public class Autonomous {
	// Gyro test
	AHRS gyro;
	
	//Declaring Talons for the four drive motors...
	private Talon front_left;
	private Talon rear_left;
	private Talon front_right;
	private Talon rear_right;
	public RobotDrive myRobot;
	//...and the encoders.
	private Encoder right_drive_encoder;
	private Encoder left_drive_encoder;
	
	//Drive Stuff.
	private ImageTracker tracker;
	private Robot robot;
	
	// Instantiate Talons with correct PWM value for 2016 robot (hopefully)
	front_left  = new Talon( 5 );
	rear_left   = new Talon( 6 );
	front_right = new Talon( 7 );
	rear_right  = new Talon( 0 );
			
	public static void high_goal_main(){
		while( inautonomous() ){
			
		}
	}
	
	public static void low_goal_main(){
		while( inautonomous() ){
			
		}
	}

}
