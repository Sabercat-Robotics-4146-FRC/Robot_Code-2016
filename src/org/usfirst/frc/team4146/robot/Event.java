package org.usfirst.frc.team4146.robot;

/**
 * Event class is used for adding events to the EventLoop.
 * 
 * @author Gowan Rowland
 * @version 9/4/2016
 */
class Event implements attr, Cloneable {
	public boolean hasNext;
	attr e;
	Event next;
	public Event ( attr e ){
		this.e = e;
		hasNext = false;
	}
	//Used by EventLoop. No need to call these on their own.
	public boolean poll(){ return e.poll(); }
	public void callback() { e.callback(); }
	public boolean complete(){ return e.complete(); }
	/**
	 * This method is used to chain events. When each event is complete, the EventLoop checks to see if there is another Event in the chain. 
	 * @param Event e to chain to this
	 * @return Event e that was chained so that chain syntax is anonymous and readable.
	 */
	public Event then ( Event e ) {
		hasNext = true;
		this.next = e.clone();
		return this.next;
	}
	@Override
    public Event clone() {
    	try {
    		final Event result = (Event) super.clone();
        	return result;
        } catch (final CloneNotSupportedException ex) {
            throw new AssertionError();
        }
    }
}