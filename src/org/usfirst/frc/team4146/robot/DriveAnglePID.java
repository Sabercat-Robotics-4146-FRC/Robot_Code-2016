package org.usfirst.frc.team4146.robot;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

/**
 * This class is used for setting the robot's heading angle to a given value using the AHRS GYRO
 * @author GowanR
 * @version 8/21/2016 
 */
public class DriveAnglePID implements PIDOutput  {
	private double delta_rotation;
	private PIDController turn_controller;
	private NetworkTable table;
	private AHRS gyro;
	/**
	 * Constructor for making the PID loop for heading angle.
	 * @param gyro AHRS gyroscope object
	 */
	public DriveAnglePID( AHRS gyro, NetworkTable table ){
		this.table = table;
		this.gyro = gyro;
		//double Kp, double Ki, double Kd, double Kf
		turn_controller = new PIDController( 0.2, 0.00, 0.00, 0.00, gyro, this );
	    turn_controller.setInputRange(-180.0f,  180.0f);
	    turn_controller.setOutputRange(-1.0, 1.0);
	    turn_controller.setAbsoluteTolerance( 0.5f );
	    turn_controller.setContinuous(true);
	    turn_controller.setSetpoint(0.0f);
	    turn_controller.enable();
	}
	/**
	 * Gets the x-axis proportion needed to change heading to set-point angle.
	 * @return delta_rotation double value of x-axis proportion. Range: [ -1.0, 1.0 ]
	 */
	public double get_axis( ){
		return delta_rotation;
	}
	/**
	 * Sets the angle to go to with heading.
	 * @param angle double angle to turn robot to.
	 */
	public void set_angle( double angle ){
		table.putNumber( "Angle Actual", gyro.getAngle() );
		turn_controller.setPID(table.getNumber("Kp", 0.2), table.getNumber("Ki", 0.0), table.getNumber("Kd", 0.0), table.getNumber("Kf", 0.0));
		turn_controller.setSetpoint( (float) angle );
	}
	@Override
	/* This function is invoked periodically by the PID Controller, */
	/* based upon navX-MXP yaw angle input and PID Coefficients.    */
	public void pidWrite(double output) {
		delta_rotation = output;
	}
}
