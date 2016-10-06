package org.usfirst.frc.team4146.robot;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.AnalogPotentiometer;

public class LifterArm {
	Controller controller;
	public static double out = 0.868;
	public static double zero = 0.221;
	private AnalogPotentiometer pot;
	private Talon arm;
	private Talon ext_arm;
	
	public LifterArm ( Controller arm_controller, EventLoop loop ) {
		this.controller = arm_controller;
		pot = new AnalogPotentiometer( 2 );
		arm = new Talon( 2 );
		ext_arm = new Talon( 3 );
		
		AsyncLoop lift_arm = new AsyncLoop( new function (){
			public void fn(){
				arm.set( controller.get_left_y_axis() );
			}
		});
		
		AsyncLoop extend = new AsyncLoop( new function (){
			public void fn(){
				double ext = controller.get_right_y_axis();
				if ( pot.get() >= out && ext > 0 ) {
					ext = 0;
				}
				if ( pot.get() >= zero && ext < 0 ) {
					ext = 0;
				}
				ext_arm.set( ext );
			}
		});
		loop.on( lift_arm );
		loop.on( extend );
	}
}
