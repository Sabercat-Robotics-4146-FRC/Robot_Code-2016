package org.usfirst.frc.team4146.robot;

import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class FlyWheelPID implements PIDOutput, PIDSource {
	private double motor_prop;
	private PIDController controller;
	private Encoder wheel;

	public FlyWheelPID ( Encoder wheel ) {
		this.wheel = wheel;
		controller = new PIDController( 0.2, 0.0, 0.0, this, this );
		controller.setContinuous( true );
		controller.enable();
		set_rpm( 1000 );
	}
	public void set_rpm( double rpm ){
		controller.setSetpoint( rpm );
	}
	public double get_pid_val(){
		return motor_prop;
	}
	@Override
	public void pidWrite(double output) {
		motor_prop = output;
	}
	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public PIDSourceType getPIDSourceType() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public double pidGet() {
		// TODO Auto-generated method stub
		return wheel.getRate();
	}
}
