package org.usfirst.frc.team4146.robot;
import edu.wpi.first.wpilibj.Talon;

public class LifterArm {
	Controller controller;
	public LifterArm ( Controller arm_controller, EventLoop loop ) {
		this.controller = arm_controller;
		Talon arm = new Talon(2);
		Event lift_arm = new Event( new attr() {
			public boolean poll () {
				arm.set( controller.get_left_y_axis() ); //FUCKING MAGIC NUMBERS
				return false;
			}
			public void callback(){}
			public boolean complete(){
				return false;
			}
		});
		loop.on( lift_arm );
	}
}
