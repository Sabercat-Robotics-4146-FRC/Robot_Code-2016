package org.usfirst.frc.team4146.robot;
import java.lang.Math;

/**
 *	Class for/ a two dimensional vector data type. 
 *	@version 8/20/2016
 *	@author GowanR
 */
public class Vector2d {
	public double x;
	public double y;
	/**
	 * Constructs a new Vector2d with the x and y values.
	 * @param double x value
	 * @param double y value
	 */
	public Vector2d( double x, double y ){
		this.x = x;
		this.y = y;
	}
	/**
	 *	Gets the normal of the vector.
	 *	@return vector magnitude 
	 */
	public double norm() {
		return Math.sqrt( Math.pow( this.x, 2 ) + Math.pow( this.y, 2 ) );
	}
}
