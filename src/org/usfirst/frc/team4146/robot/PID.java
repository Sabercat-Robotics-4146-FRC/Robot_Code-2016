package org.usfirst.frc.team4146.robot;
/**
 * General purpose PID loop.
 * @author Gowan Rowland
 * @version 9/8/2016
 */
public class PID {
	private double Kp;
	private double Ki;
	private double Kd;
	private double prevError;
	private double setpoint;
	private double integral;
	private double error;
	private double derivative;
	private signal functions;
	private double output;
	private double ramped_setpoint;
	public PID ( signal functions ){
		this.functions = functions;
		integral = 0;
		setpoint = 0;
	}
	public void set_setpoint( double s ){
		setpoint = s;
	}
	public void set_pid( double p, double i, double d ) {
		this.Kp = p;
		this.Ki = i;
		this.Kd = d;
	}
	public void update( double dt ){
		ramped_setpoint = Utils.lerp( setpoint, ramped_setpoint, dt );
		error = ramped_setpoint - functions.getValue();
		integral += ( Ki * error * dt );
		derivative = ( prevError - error ) / dt;
		prevError = error;
		output = ( Kp * error ) + ( integral ) + ( Kd * derivative );
		System.out.println( integral );
	}
	public double get_angle(){
		return functions.getValue();
	}
	public double get() {
		return output;
	}
}
