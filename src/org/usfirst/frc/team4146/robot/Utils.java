package org.usfirst.frc.team4146.robot;

public class Utils {
	/**
	 *	One on the smoothing methods that may be used for a ramp drive style control. Linear interpolation.
	 *	Usage: double a = lerp( a, 15, dt ) // getting a to get to value 15
	 *	@param a double current value of variable being set.
	 *	@param b double value that a is linearly interpolating to.
	 *	@param t double change in time
	 *	@return next value of a
	 */
	static double lerp( double a, double b, double t ){
		return a + (b - a) * t;
	}
	/**
	 *	One on the smoothing methods that may be used for a ramp drive style control. Linear interpolation.
	 *	A separate implementation of lerp. Very different!
	 *	Usage: double a = lerp2( a, 15, dt ) // getting a to get to value 15
	 *	@param a double current value of variable being set.
	 *	@param b double value that a is linearly interpolating to.
	 *	@param t double change in time
	 *	@return next value of a
	 */
	static double lerp2( double a, double b, double t ){
		return ( 1.0 - t ) * a + t*b;
	}
	/**
	 *	One on the smoothing methods that may be used for a ramp drive style control. Cosine interpolation.
	 *	Usage: double a = cerp( a, 15, dt ) // getting a to get to value 15
	 *	@param a double current value of variable being set.
	 *	@param b double value that a is linearly interpolating to.
	 *	@param t double change in time
	 *	@return next value of a
	 */
	static double cerp( double a, double b, double t ){
		double c = (1-Math.cos( t * Math.PI )) * 0.5;
		return 	a*(1-c) + b*c;
	}
}
