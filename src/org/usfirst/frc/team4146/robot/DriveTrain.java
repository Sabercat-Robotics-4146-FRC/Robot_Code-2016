package org.usfirst.frc.team4146.robot;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;

// set angle test
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;

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
	private Talon front_left;
	private Talon rear_left;
	private Talon front_right;
	private Talon rear_right;
	public RobotDrive myRobot;
	private Encoder right_drive_encoder;
	private Encoder left_drive_encoder;
	private Controller drive_controller;
	// Arcade drive data
	private double x_axis;
	private double y_axis;
	private static double speed_coef = 0.5;
	private ImageTracker tracker;
	private Robot robot;
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
		
		// Event registering.
		// drive_tracker Event is just a strait up loop. No need to trigger. Tracks raspberry pi output.
		Event drive_tracker = new Event( new attr() {
			public boolean poll(){
				x_axis = cerp( x_axis, drive_controller.get_right_x_axis() * speed_coef, 1.0 );
				y_axis = cerp( y_axis, drive_controller.get_left_y_axis() * speed_coef, 1.0 );
				robot.network_table.putNumber( "imager", tracker.get_x_axis() );
				if ( drive_controller.get_left_trigger() ){
					//double n = tracker.get_x_axis();
					//x_axis = motor_compensate( n );
					x_axis = robot.drive_angle.get();
					System.out.println( x_axis );
				}
				return false;
			}
			public void callback(){}
			public boolean complete(){return true;}
		});
		// drive_loop Event is just a strait up loop. No need to trigger. Used for controller -> drive interface
		Event drive_loop = new Event( new attr(){
			public boolean poll(){
				if ( drive_controller.get_left_stick_press() ){
					speed_coef = 1.0;
				} else {
					speed_coef = 0.5;
				}
				myRobot.arcadeDrive( y_axis, x_axis, true );
				return false;
			}
			public boolean complete(){return true;}
			public void callback(){}
		});
		// Register Events
		main_loop.on( drive_tracker );
		main_loop.on( drive_loop );
	}
	/**
	 *	One on the smoothing methods that may be used for a ramp drive style control. Linear interpolation.
	 *	Usage: double a = lerp( a, 15, dt ) // getting a to get to value 15
	 *	@param a double current value of variable being set.
	 *	@param b double value that a is linearly interpolating to.
	 *	@param t double change in time
	 *	@return next value of a
	 */
	static double lerp( double a, double b, double t ){
		return a + (b - a) * t;
	}
	/**
	 *	One on the smoothing methods that may be used for a ramp drive style control. Linear interpolation.
	 *	A separate implementation of lerp. Very different!
	 *	Usage: double a = lerp2( a, 15, dt ) // getting a to get to value 15
	 *	@param a double current value of variable being set.
	 *	@param b double value that a is linearly interpolating to.
	 *	@param t double change in time
	 *	@return next value of a
	 */
	static double lerp2( double a, double b, double t ){
		return ( 1.0 - t ) * a + t*b;
	}
	/**
	 *	One on the smoothing methods that may be used for a ramp drive style control. Cosine interpolation.
	 *	Usage: double a = cerp( a, 15, dt ) // getting a to get to value 15
	 *	@param a double current value of variable being set.
	 *	@param b double value that a is linearly interpolating to.
	 *	@param t double change in time
	 *	@return next value of a
	 */
	static double cerp( double a, double b, double t ){
		double c = (1-Math.cos( t * Math.PI )) * 0.5;
		return 	a*(1-c) + b*c;
	}
	double motor_compensate( double n ){
		if ( n > 0.5 || n < -0.5 ){
			n *= 0.5;
		}
		return n;
		//return -Math.pow( ( robot.network_table.getNumber( "st_coef", 0.5 ) * n ), 2 ) + robot.network_table.getNumber( "st_bias", 0.5 );
	}
}
