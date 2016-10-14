package org.usfirst.frc.team4146.robot;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class ImageTracker {
	private NetworkTable dashboard;
	private double x;
	private double dx;
	private double w;
	public PID vis_pid;
	public static double center = 380.0;
	
	public ImageTracker( NetworkTable dash ){
		dashboard = dash;
		vis_pid = new PID( new signal() {
			public double getValue(){
				return get_x_axis();
			}
		});
		vis_pid.set_setpoint( 0 );
		vis_pid.set_pid( 0.5, 0, 0 );
		vis_pid.add_const( 0.35 );
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
		return (dx-center)/320.0;
	}
	
}
