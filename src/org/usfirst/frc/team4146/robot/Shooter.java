package org.usfirst.frc.team4146.robot;
import edu.wpi.first.wpilibj.Talon;
//import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.DigitalInput;
// Timer for controll delays.
import edu.wpi.first.wpilibj.Timer;

/**
 * Shooter class controls the shooter ( Pivot arm and fly wheel )
 * @author GowanR
 * @version 8/21/2016
 */
public class Shooter {
	private Talon shooter_arm_motor;
	private Talon outer_flywheel_motor;
	private Talon inner_flywheel_motor;
	private DigitalInput arm_up;
	private DigitalInput arm_down;
	private Controller driver;
	/**
	 * Constructor for shooter. 
	 * @param driver Controller. The contorller of the main driver. 
	 */
	public Shooter( Controller driver ){
		this.driver = driver;
		shooter_arm_motor    = new Talon( 4 );
		outer_flywheel_motor = new Talon( 1 );
		inner_flywheel_motor = new Talon( 8 );
		
		outer_flywheel_motor.setInverted(true);
		inner_flywheel_motor.setInverted(false);
		shooter_arm_motor.setInverted(true);
		
		arm_up = new DigitalInput( 7 );
		arm_down = new DigitalInput( 8 );
	}
	/**
	 * Lifts the shooter pivot arm up to the binary limit switch.
	 * 
	 */
	public void lift_arm(){
		if ( ! arm_up.get() ){
			shooter_arm_motor.set( 1.0 );
		} else if( arm_up.get() ){
			shooter_arm_motor.set( 0.0 );
		}
	}
	/**
	 * Drops the shooter arm down to the binary limit switch. 
	 */
	public void drop_arm(){
		if ( ! arm_down.get() ){
			shooter_arm_motor.set( -1.0 );
		} else if( arm_down.get() ){
			shooter_arm_motor.set( 0.0 );
		}
	}
	/**
	 * Stops all shooter motors. Best practice: bind to a button when testing.
	 */
	public void abort_motors(){
		shooter_arm_motor.set( 0.0 );
		stop_flywheel();
	}
	/**
	 * Spins up the shooter fly wheel. 
	 */
	public void spin_flywheel(){
		outer_flywheel_motor.set( 1.0 );
		inner_flywheel_motor.set( 1.0 );
	}
	/**
	 * Spins fly wheel to specified motor proportion. 
	 * @param speed double proportion of motor. 0.25 == 25% motor 
	 */
	public void spin_flywheel( double speed ){
		outer_flywheel_motor.set( speed );
		inner_flywheel_motor.set( speed );
	}
	/**
	 * Stops the fly wheel motors. ( Sets motor controllers to 0.0 ) 
	 */
	public void stop_flywheel(){
		outer_flywheel_motor.set( 0.0 );
		inner_flywheel_motor.set( 0.0 );
	}
	/**
	 * Reverses the fly wheel rotation. ( Eject direction ) 
	 */
	public void reverse_flywheel(){
		outer_flywheel_motor.set( -1.0 );
		inner_flywheel_motor.set( -1.0 );
	}
	/**
	 * Reverses the fly wheel  
	 */
	public void reverse_flywheel( double speed ){
		outer_flywheel_motor.set( -speed );
		inner_flywheel_motor.set( -speed );
	}
	/**
	 * Puts the pivoting fly wheel arm into in-take position. 
	 */
	public void arm_intake_position(){
		lift_arm();
		Timer.delay( 0.5 );
		// Lower arm about half way.
		shooter_arm_motor.set( -0.25 );
		Timer.delay( 0.2 );
		shooter_arm_motor.set( 0.0 );
	}
	/**
	 * Puts the shooter mechanism into in-take position or in-take mode. ( pivot arm middle, fly wheel spinning ) 
	 */
	public void intake_position(){
		arm_intake_position();
		spin_flywheel( 0.35 );
	}
	/**
	 * Ejects the ball from the robot.
	 */
	public void eject(){
		lift_arm();
		reverse_flywheel();
	}
	/**
	 * Shoots the ball. ( Untested )
	 */
	public void shoot(){
		lift_arm();
		spin_flywheel();
		Timer.delay( 0.5 );
		drop_arm();
	}
	/**
	 * Update function should be called in main loop.
	 */
	public void update(){
		if ( driver.get_a_button() ){
			drop_arm();
		}
		if ( driver.get_x_button() ){
			intake_position();
		}
		if ( driver.get_y_button() ){
			abort_motors();
		}
		if( driver.get_b_button() ){
			spin_flywheel();
		}
	}
}
