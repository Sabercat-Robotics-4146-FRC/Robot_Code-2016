package org.usfirst.frc.team4146.robot;
import edu.wpi.first.wpilibj.Talon;
//import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.DigitalInput;
// Timer for controll delays.
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Encoder;

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
	private Encoder flywheel_encoder;
	private static double wheel_intake = 100;
	// Main Events
	Event shoot;
	Event eject;
	Event pick_up;
	// Helper Events
	Event lower_arm;
	Event stop_arm;
	Event raise_arm;
	Event spin_wheel;
	Event stop_wheel;
	Event reverse_wheel;
	Event intake;
	/**
	 * Constructor for shooter. 
	 * @param driver Controller. The contorller of the main driver. 
	 */
	public Shooter( Controller driver, EventLoop main_loop ){
		flywheel_encoder = new Encoder(4,5, true);
		this.driver = driver;
		shooter_arm_motor    = new Talon( 4 );
		outer_flywheel_motor = new Talon( 1 );
		inner_flywheel_motor = new Talon( 8 );
		
		outer_flywheel_motor.setInverted(true);
		inner_flywheel_motor.setInverted(false);
		shooter_arm_motor.setInverted(true);
		
		arm_up = new DigitalInput( 7 );
		arm_down = new DigitalInput( 8 );
		/* Controller bindings */
		shoot = driver.bind( Controller.right_trigger );
		eject = driver.bind( Controller.left_bumper );
		pick_up = driver.bind( Controller.right_bumper );
		/* Helper Events */
		stop_arm = new Event( new attr(){
			public void callback(){
				shooter_arm_motor.set( 0.0 );
			}
			public boolean poll(){ return true; }
			public boolean complete(){ return true; }
		});
		lower_arm = new Event( new attr() {
			public boolean poll(){ return true; }
			public void callback(){
				shooter_arm_motor.set( -1.0 );
			}
			public boolean complete(){
				return arm_down.get();
			}
		});
		raise_arm = new Event( new attr(){
			public void callback(){
				shooter_arm_motor.set( 1.0 );
			}
			public boolean poll(){ return true; }
			public boolean complete(){
				return arm_up.get();
			}
		});
		spin_wheel = new Event( new attr(){
			public void callback(){
				spin_flywheel();
			}
			public boolean poll(){ return true; }
			public boolean complete(){
				if ( flywheel_encoder.getRate() >= 19000 ) {
					flywheel_encoder.reset();
					return true;
				} else {
					return false;
				}
			}
		});
		stop_wheel = new Event( new attr() {
			public void callback(){
				stop_flywheel();
			}
			public boolean poll(){ return true; }
			public boolean complete(){ return true; }
		});
		reverse_wheel = new Event( new attr() {
			public void callback(){
				reverse_flywheel();
			}
			public boolean poll(){ return true; }
			public boolean complete(){ return true; }
		});
		Timer t = new Timer();
		intake = new Event( new attr(){
			public boolean poll(){ t.reset(); return true; }
			public void callback(){
				spin_flywheel();
			}
			public boolean complete(){
				if ( t.get() >= 0.5 && flywheel_encoder.getRate() < wheel_intake ) {
					return true;
				} else {
					return false;
				}
			}
		} );
		
		/* Event Chains */
		shoot
		.then( raise_arm )
		.then( stop_arm )
		.then( spin_wheel )
		.then( lower_arm )
		.then( stop_arm )
		.then( stop_wheel );
		
		eject
		.then( reverse_wheel )
		.then( raise_arm )
		.then( stop_wheel );
		
		pick_up
		.then( intake );
		
		main_loop.on( eject );
		main_loop.on( shoot );
		main_loop.on( pick_up );
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
}
