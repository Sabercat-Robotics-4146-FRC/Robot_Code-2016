package org.usfirst.frc.team4146.robot;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class ImageTracker {
	private NetworkTable dashboard;
	private double x;
	private double dx;
	private double w;
	public PID vis_pid;
	
	public ImageTracker( NetworkTable dash ){
		dashboard = dash;
		vis_pid = new PID( new signal() {
			public double getValue(){
				return get_x_axis();
			}
		});
		vis_pid.set_setpoint( 0 );
		vis_pid.set_pid( 1, 0, 0 );
	}
	private double get_x_center() {
		x = dashboard.getNumber( "vis_x", -1.0 );
		w = dashboard.getNumber( "vis_w", -1.0 );
		return x + (w/2);
	}
	public double get_pid_vis (){
		return vis_pid.get();
	}
	public double get_x_axis(){
		dx = get_x_center();
		return (dx-320.0)/320.0;
	}
}
