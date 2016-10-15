package org.usfirst.frc.team4146.robot;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;

// set angle test
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;
import org.usfirst.frc.team4146.robot.AsyncLoop;

import java.lang.Math;
/**
 * Drive Train class used for streamlined interface with the drivetrain.
 * @author GowanR
 * @version 8/20/2016
 */
public class DriveTrain {
	// Gyro test
	AHRS gyro;
	/*
	 *	Allocate Talons for all four drive motors. 
	 */
	public Talon front_left;
	public Talon rear_left;
	public Talon front_right;
	public Talon rear_right;
	public RobotDrive myRobot;
	public Encoder right_drive_encoder;
	public Encoder left_drive_encoder;
	private Controller drive_controller;
	// Arcade drive data
	private double x_axis;
	private double y_axis;
	private static double speed_coef = 0.5;
	public ImageTracker tracker;
	private Robot robot;
	public PID heading;
	/**
	 * Constructor for drive train. Only needs the driver's Controller.
	 * @param drive_controller Controller that the driver uses
	 */
	public DriveTrain( Controller drive_controller, EventLoop main_loop, Robot robot ) {
		this.robot = robot;
		robot.network_table.putNumber( "st_coef", 0.5 );
		robot.network_table.putNumber( "st_bias", 0.5 );
		tracker = new ImageTracker( robot.network_table );
		gyro = new AHRS( SPI.Port.kMXP );
		// Set driver's controller
		this.drive_controller = drive_controller;
		// Instantiate Talons with correct PWM value for 2016 robot
		front_left  = new Talon( 5 );
		rear_left   = new Talon( 6 );
		front_right = new Talon( 7 );
		rear_right  = new Talon( 0 );
		
		// Instantiate robot's drive with Talons
		myRobot     = new RobotDrive( front_left, rear_left, front_right, rear_right );
		myRobot.setExpiration( 0.1 );
		// Instantiate drive encoders
		right_drive_encoder = new Encoder( 2, 3, false ); // false so to not reverse direction
		left_drive_encoder  = new Encoder( 0, 1, true );
		
		// Invert all of the Talons
		front_left.setInverted( true );
		rear_left.setInverted( true );
		front_right.setInverted( true );
		rear_right.setInverted( true );
		// delta time Timer
		AsyncLoop track_goal = new AsyncLoop( new function () {
			public void fn(){
				if( drive_controller.get_left_trigger() ){
					x_axis = tracker.get_pid_vis();
				}
			}
		});
		
		AsyncLoop drive_loop = new AsyncLoop( new function(){
			public void fn(){
				x_axis = tolerate( drive_controller.get_right_x_axis() * speed_coef, 0.1 );
				if ( x_axis < 0 ) {
					x_axis -= 0.37;
				} else if ( x_axis > 0 ) { 
					x_axis += 0.37;
				}
				y_axis = tolerate( drive_controller.get_left_y_axis() * speed_coef, 0.1 );
				if ( y_axis < 0 ) {
					y_axis -= 0.37;
				} else if ( y_axis > 0 ) { 
					y_axis += 0.37;
				}
				//System.out.println( x_axis );
				robot.network_table.putNumber( "imager", tracker.get_x_axis() );
				//System.out.println( robot.drive_angle.get() );
				if ( drive_controller.get_left_trigger() ){
					double n = tracker.get_x_axis();
					x_axis = motor_compensate( n );
				}
				if ( drive_controller.get_left_stick_press() ){
					speed_coef = 1.0;
				} else {
					speed_coef = 0.5;
				}
				myRobot.arcadeDrive( y_axis, x_axis, true );
			}
		});
		heading = new PID( new signal(){
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
	    	heading.set_pid( 0.01, 0.00000000001, 0.0000001 );
	    	//drive_angle.set_pid( 0.01, 0.00000000001, 0.0000001 );
	    	heading.set_setpoint( 0.0 );
		
		// Register Events
		main_loop.on( drive_loop );
		main_loop.on( track_goal );
	}
	static double tolerate( double x, double tolerance ){
		if ( x >= tolerance || x <= -tolerance ) {
			return x;
		} else {
			return 0;
		}
	}
	public void reset_encoders(){
		right_drive_encoder.reset();
		left_drive_encoder.reset();
	}
	/*
	 * Returns the distance that the left side has traveled in feet.
	 */
	public double get_left_dist() {
		return ( left_drive_encoder.get() / 1440.0) * (2 * Math.PI * 8);
	}
	/*
	 * Returns the distance that the right side has traveled in feet.
	 */
	public double get_right_dist() {
		return ( right_drive_encoder.get() / 65.0) * (2 * Math.PI * 8);
	}
//	double motor_compensate( double n ){
//		if ( n > 0.5 || n < -0.5 ){
//			n *= 0.5;
//		}
//		return n;
//		//return -Math.pow( ( robot.network_table.getNumber( "st_coef", 0.5 ) * n ), 2 ) + robot.network_table.getNumber( "st_bias", 0.5 );
//	}
	public static double motor_compensate( double n ){
		n = ((n/(1+Math.abs(n)))*0.5);
		if( n < 0 ){
			n -= 0.20;
		}
		if( n > 0 ){
			n += 0.20;
		}
		if( n <= 0.35 && n >= -0.35 ){
			n *= 1.25;
		}
		
		System.out.println(n);
		return n;
		//return -Math.pow( ( robot.network_table.getNumber( "st_coef", 0.5 ) * n ), 2 ) + robot.network_table.getNumber( "st_bias", 0.5 );
	}
	/**
	 * Work in progress autonomous interface with drive train.
	 * @param double dist the distance that the robot will travel.
	 * @return Event that will drive the robot strait for double dist feet.
	 */
	public Event drive_strait( double dist ) {
		gyro.reset();
		heading.set_setpoint( 0.0 );
		PID drive_dist = new PID( new signal() {
			public double getValue(){
				return get_left_dist();
			}
		});
		drive_dist.set_pid( 1, 0, 0 );
		drive_dist.set_setpoint( dist );
		return new Event( new attr(){
			public boolean poll(){ return true; }
			public void callback(){
				x_axis = heading.get();
				y_axis = drive_dist.get();
				myRobot.arcadeDrive( y_axis, x_axis, true );
			}
			public boolean complete(){
				return ( Math.abs(get_left_dist() - dist) <= 0.1 ); // 1/10th of a foot tolerance.
			}
		} );
	}
	/**
	 * Work in progress will return an event which turns the robot to a given angle.
	 */
	public Event turn( double angle ) {
		gyro.reset();
		heading.set_setpoint( angle );
		return new Event( new attr(){
			public boolean poll(){ return true; }
			public void callback(){ 
				x_axis = heading.get();
				myRobot.arcadeDrive( 0, x_axis, true );
			}
			public boolean complete(){
				return ( Math.abs( heading.get_angle() - angle ) <= 5.0 ); //5 degree tolerance.
			}
		});
	}
}
