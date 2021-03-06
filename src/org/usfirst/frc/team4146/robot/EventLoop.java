package org.usfirst.frc.team4146.robot;
import java.util.ArrayList;

/**
 * EventLoop class is used for running Events. 
 * Usage: 
 * EventLoop name = new EventLoop;
 * (new Thread( name )).start();
 * 
 * @author GowanRowland1
 * @version 9/4/2016 
 */
class EventLoop implements Runnable {
	private ArrayList<Event> stack;
	private ArrayList<Event> completeStack;
	private ArrayList<Event> persistentStack;
	public double dt;
	// Just a good old constructor.. Nothing to see here.
	public EventLoop() {
		stack = new ArrayList<Event>();
		completeStack = new ArrayList<Event>();
		persistentStack = new ArrayList<Event>();
	}
	/**
	 * Adds Event e to the persistent stack so that the event will be polled always. Never removed or cleared.
	 * @param Event e event that will be persistently polled.
	 */
	public void on( Event e ){
		persistentStack.add( e );
	}
	/**
	 * Adds Event e to the normal stack. This Event will die when triggered.
	 * @param Event e to be polled once. 
	 */
	public void once( Event e ){
		stack.add( e );
	}
	/**
	 * Gets the delta (change in) time for the event loop to complete.
	 * @return double dt change in time.
	 */
	public double get_dt(){
		return dt;
	}
	@Override
	public void run(){
		long startTime;
		while ( true ){
			startTime = System.nanoTime();
			for ( int i = 0; i < persistentStack.size(); i++ ){
				Event e = persistentStack.get( i );
				if ( e.poll() ){
					e.callback();
					if ( ! completeStack.contains( e ) ){
						completeStack.add( e );
					}
				}
			}
			for ( int i = 0; i < stack.size(); i++ ){
				Event e = stack.get( i );
				if ( e.poll() ){
					e.callback();
					completeStack.add( e );
					stack.remove( i );
				}
			}
			for ( int i = 0; i < completeStack.size(); i++ ){
				Event e = completeStack.get( i );
				if ( e.complete() ) {
					if ( e.hasNext ){
						stack.add( e.next );
					}
					completeStack.remove( i );
				}
			}
			// Get the time it takes to complete the event loop. Nanosecond to second conversion.
			dt = (double)( System.nanoTime() - startTime ) / 1e9;
		}
	}
}