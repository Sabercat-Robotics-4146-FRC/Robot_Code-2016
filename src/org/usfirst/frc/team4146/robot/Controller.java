package org.usfirst.frc.team4146.robot;
import edu.wpi.first.wpilibj.Joystick;

/**
 * The Controller class is a wrapper for the wpi Joystick class for the use of Logitech controllers
 * 
 * @author GowanR
 * @version 8/20/2016
 * 
 */
public class Controller {
	Joystick joy;
	// Define buttons
	public static final int X_button          = 1;
	public static final int A_button          = 2;
	public static final int B_button          = 3;
	public static final int Y_button          = 4;
	public static final int left_bumper       = 5;
	public static final int right_bumper      = 6;
	public static final int left_trigger      = 7;
	public static final int right_trigger     = 8;
	public static final int back_button       = 9;
	public static final int start_button      = 10;
	public static final int left_stick_press  = 11;
	public static final int right_stick_press = 12;
	// Define axes (pl. axis)
	private static final int left_x_axis  = 0;
	private static final int left_y_axis  = 1;
	private static final int right_x_axis = 2;
	private static final int right_y_axis = 3;
	/**
	 * Constructor takes the controller number (0 or 1 with two controllers)
	 * 
	 * @param number int controller number
	 */
	public Controller( int number ){
		joy = new Joystick( number );
	}
	/**
	 * Gets weather the "X" button is being pushed on the controller.
	 * @return boolean weather "X" button is pressed
	 */
	public boolean get_x_button( ){
		return joy.getRawButton( X_button );
	}
	/**
	 * Gets weather the "A" button is being pushed on the controller.
	 * @return boolean weather "A" button is pressed
	 */
	public boolean get_a_button( ){
		return joy.getRawButton( A_button );
	}
	/**
	 * Gets weather the "B" button is being pushed on the controller.
	 * @return boolean weather "B" button is pressed
	 */
	public boolean get_b_button( ){
		return joy.getRawButton( B_button );
	}
	/**
	 * Gets weather the "Y" button is being pushed on the controller.
	 * @return boolean weather "Y" button is pressed
	 */
	public boolean get_y_button( ){
		return joy.getRawButton( Y_button );
	}
	/**
	 * Gets weather the left bumper is being pushed on the controller.
	 * @return boolean weather left bumper is pressed
	 */
	public boolean get_left_bumper( ){
		return joy.getRawButton( left_bumper );
	}
	/**
	 * Gets weather the right bumper is being pushed on the controller.
	 * @return boolean weather right bumper button is pressed
	 */
	public boolean get_right_bumper( ){
		return joy.getRawButton( right_bumper );
	}
	/**
	 * Gets weather the left trigger is being pushed on the controller.
	 * @return boolean weather left trigger is pressed
	 */
	public boolean get_left_trigger( ){
		return joy.getRawButton( left_trigger );
	}
	/**
	 * Gets weather the right trigger is being pushed on the controller.
	 * @return boolean weather right trigger is pressed
	 */
	public boolean get_right_trigger( ){
		return joy.getRawButton( right_trigger );
	}
	/**
	 * Gets weather the back button is being pushed on the controller.
	 * @return boolean weather back button is pressed
	 */
	public boolean get_back_button( ){
		return joy.getRawButton( back_button );
	}
	/**
	 * Gets weather the start button is being pushed on the controller.
	 * @return boolean weather start button is pressed
	 */
	public boolean get_start_button( ){
		return joy.getRawButton( start_button );
	}
	/**
	 * Gets weather the left stick is being pushed on the controller.
	 * @return boolean weather the left stick is pressed
	 */
	public boolean get_left_stick_press( ){
		return joy.getRawButton( left_stick_press );
	}
	/**
	 * Gets weather the right stick is being pushed on the controller.
	 * @return boolean weather the right stick is pressed
	 */
	public boolean get_right_stick_press( ){
		return joy.getRawButton( right_stick_press );
	}
	/**
	 * Gets the value of the x axis of the left stick. ( -1.0 to 1.0, 0.0 is centered )
	 * @return double x axis position
	 */
	public double get_left_x_axis( ){
		return joy.getRawAxis( left_x_axis );
	}
	/**
	 * Gets the value of the y axis of the left stick. ( -1.0 to 1.0, 0.0 is centered )
	 * @return double y axis position
	 */
	public double get_left_y_axis( ){
		return joy.getRawAxis( left_y_axis );
	}
	/**
	 * Gets the value of the x axis of the right stick. ( -1.0 to 1.0, 0.0 is centered )
	 * @return double x axis position
	 */
	public double get_right_x_axis( ){
		return joy.getRawAxis( right_x_axis );
	}
	/**
	 * Gets the value of the y axis of the right stick. ( -1.0 to 1.0, 0.0 is centered )
	 * @return double y axis position
	 */
	public double get_right_y_axis( ){
		return joy.getRawAxis( right_y_axis );
	}
	/**
	 * Gets the value of the right stick. ( -1.0 to 1.0, 0.0 is centered )
	 * @return Vector2d for right stick
	 */
	public Vector2d get_right_axis( ){
		return new Vector2d( this.get_right_x_axis( ), this.get_right_y_axis( ) );
	}
	/**
	 * Gets the value of the left stick. ( -1.0 to 1.0, 0.0 is centered )
	 * @return Vector2d for left stick
	 */
	public Vector2d get_left_axis( ){
		return new Vector2d( this.get_left_x_axis( ), this.get_left_y_axis( ) );
	}
	/**
	 *	Used just in case we need the raw joystick.
	 *
	 * @return Joystick used for the contorller
	 */
	public Joystick get_joystick( ){
		return joy;
	}
	public Event bind( int button ){
		Event press = new Event( new attr() {
			public boolean poll(){ return joy.getRawButton( button ); }
			public void callback(){}
			public boolean complete(){ return !joy.getRawButton( button ); }
		});
		return press;
	}
}
