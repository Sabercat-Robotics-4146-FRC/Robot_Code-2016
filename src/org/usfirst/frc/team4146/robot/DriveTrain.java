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
	private RobotDrive myRobot;
	private Encoder right_drive_encoder;
	private Encoder left_drive_encoder;
	private Controller drive_controller;
	private Timer dt;
	// Arcade drive data
	private double x_axis;
	private double y_axis;
	private static double epsilon = 0.01;
	private static double lerp_coef = 100.0;
	
	//PID
	private DriveAnglePID heading;
	/**
	 * Constructor for drive train. Only needs the driver's Controller.
	 * @param drive_controller Controller that the driver uses
	 */
	public DriveTrain( Controller drive_controller, Robot robot ) {
		gyro = new AHRS( SPI.Port.kMXP );
		heading = new DriveAnglePID( gyro );
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
		dt = new Timer();
		dt.reset();
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
	/**
	 *	drivetrain update. Should call in telliop loop.  
	 */
	public void update() {
		dt.reset();
		dt.start();
		x_axis = cerp( x_axis, drive_controller.get_left_x_axis(), dt.get()*lerp_coef );
		y_axis = cerp( y_axis, drive_controller.get_left_y_axis(), dt.get()*lerp_coef );
		if ( drive_controller.get_left_x_axis() <= epsilon && drive_controller.get_left_x_axis() >= -epsilon){
			x_axis = 0.0;
		}
		if ( drive_controller.get_left_y_axis() <= epsilon && drive_controller.get_left_y_axis() >= -epsilon){
			y_axis = 0.0;
		}
		if ( drive_controller.get_left_trigger() ){
			x_axis = heading.get_axis();
		}
		myRobot.arcadeDrive( y_axis, x_axis, true );
	}
}
