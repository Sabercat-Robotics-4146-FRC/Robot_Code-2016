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
	private double shooter_time;
	private long startTime;
	private long storeTime;
	private double store_sec_timer;
	private static double light_reverse_speed = 0.4;
	private static double intake_speed = 0.4;
	private static double store_speed = 0.25;
	// Main Events
	Event shoot;
	Event eject;
	Event store;
	// Helper Events
	Event lower_arm;
	Event stop_arm;
	Event raise_arm;
	Event spin_wheel;
	Event stop_wheel;
	Event reverse_wheel;
	Event light_reverse_wheel;
	Event store_wheel;
	
	boolean intaking;
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
		store = driver.bind( Controller.A_button );
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
				shooter_arm_motor.set( 0.1 );
			}
			public boolean poll(){ flywheel_encoder.reset(); startTime = System.nanoTime(); shooter_time = 0; return true; }
			public boolean complete(){
				System.out.println( flywheel_encoder.getRate() );
				shooter_time = (double)( System.nanoTime() - startTime ) / 1e9;
				if ( shooter_time >= 3.0 ) {
					flywheel_encoder.reset();
					shooter_arm_motor.set( 0.0 );
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
		light_reverse_wheel = new Event( new attr(){
			public void callback(){
				reverse_flywheel( light_reverse_speed );
			}
			public boolean poll(){ return true; }
			public boolean complete(){ return true; }
		} );
		store_wheel = new Event( new attr() {
			public void callback(){
				spin_flywheel( store_speed );
				shooter_arm_motor.set( 0.1 );
			}
			public boolean poll(){ storeTime = System.nanoTime(); store_sec_timer = 0; return true; }
			public boolean complete(){
				store_sec_timer = (double)( System.nanoTime() - storeTime ) / 1e9;
				if ( store_sec_timer >= 1.0 ) {
					shooter_arm_motor.set( 0.0 );
					return true;
				} else {
					return false;
				}
			}
		});
		intaking = false;
		AsyncLoop intake = new AsyncLoop( new function () {
			public void fn(){
				if ( driver.get_right_bumper() ){
					spin_flywheel( intake_speed );
					intaking = true;
					System.out.println( "Intaking" );
				}
				if ( !driver.get_right_bumper() && intaking ) {
					stop_flywheel();
					intaking = false;
				}
			}
		});
		
		AsyncLoop debug = new AsyncLoop( new function (){
			public void fn() {
				System.out.println( "Encoder: " + flywheel_encoder.get() );
			}
		});
		/* Event Chains */
		shoot
		.then( light_reverse_wheel )
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
		
		store
		.then( raise_arm )
		.then( store_wheel )
		.then( lower_arm )
		.then( stop_wheel );
		
		main_loop.on( eject );
		main_loop.on( shoot );
		//main_loop.on( debug );
		main_loop.on( intake );
		main_loop.on( store );
		
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
	public static double to_rpm( double ticks ) {
		return ticks/1440;
	}
	public void reset() {
		shooter_arm_motor.set( 0.0 );
		outer_flywheel_motor.set( 0.0 );
		inner_flywheel_motor.set( 0.0 );
	}
}
