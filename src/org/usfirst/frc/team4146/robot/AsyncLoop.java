package org.usfirst.frc.team4146.robot;

public class AsyncLoop extends Event {
	private function f;
	AsyncLoop( function f ){
		super( new attr(){
			public boolean poll(){ f.fn(); return false; }
			public void callback(){}
			public boolean complete(){ return true; }
		} );
		this.f = f;
	}
}
