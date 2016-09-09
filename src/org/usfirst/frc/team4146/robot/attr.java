package org.usfirst.frc.team4146.robot;
/**
 * Used to construct events. Defined attributes of the event. i.e. defines the Events callback, poll, and complete functions.
 * @author Gowan Rowland
 * @version 9/4/2016
 */
public interface attr {
	boolean poll();
	void callback();
	boolean complete();
}
