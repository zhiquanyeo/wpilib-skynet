package com.zhiquanyeo.skynet.driverstation;

import java.util.ArrayList;

import net.java.games.input.Controller;

public class ControllerState 
	implements IControllerUpdateListener,
           IControllerStateUpdateListener {
	
	public static class StickState {
	    public byte x;
	    public byte y;
	    public byte z;
	    public byte throttle;
	    
	    public boolean [] buttons = new boolean[12];
	    
	    public void copyState(StickState newState) {
	        x = newState.x;
	        y = newState.y;
	        z = newState.z;
	        throttle = newState.throttle;
	        
	        for (int i = 0; i < buttons.length; i++) {
	            buttons[i] = newState.buttons[i];
	        }
	    }
	    
	    public String toString() {
	    	String ret = "(x: " + x + ", y: " + y + ", z: " + z + ", thr: " + throttle + ") ";
	    	ret += "[";
	    	for (int i = 0; i < buttons.length; i++) {
	    		if (buttons[i]) {
	    			ret += "1";
	    		}
	    		else {
	    			ret += "0";
	    		}
	    		if (i < buttons.length - 1) {
	    			ret += ", ";
	    		}
	    	}
	    	ret += "]";
	    	return ret;
	    }
	}
	
	//Up to 4 sticks
	private StickState [] stickStates = new StickState[4];
	
	private ArrayList<Controller> foundControllers;
	
	private ArrayList<IControllerStateListener> listeners;
	
	public ControllerState() {
	    foundControllers = new ArrayList<Controller>();
	    listeners = new ArrayList<IControllerStateListener>();
	    
	    for (int i = 0; i < 4; i++) {
	        stickStates[i] = new StickState();
	    }
	}
	
	public void addListener(IControllerStateListener listener) {
	    listeners.add(listener);
	}
	
	protected synchronized void setFoundControllers(ArrayList<Controller> controllerList) {
	    foundControllers = controllerList;
	}
	
	public synchronized ArrayList<Controller> getFoundControllers() {
	    return foundControllers;
	}
	
	protected synchronized void updateControllerState(int index, StickState newState) {
	    if (index >= 0 && index < stickStates.length) {
	        stickStates[index].copyState(newState);
	    }
	}
	
	public synchronized StickState getStickState(int index) {
		if (index >= 0 && index < stickStates.length) {
			return stickStates[index];
		}
		return null;
	}
	
	//=== IControllerUpdateListener ===
	@Override
	public void onControllerListUpdated(ArrayList<Controller> controllerList) {
	    System.out.println("ControllerListUpdated");
	    setFoundControllers(controllerList);
	    for (int i = 0; i < listeners.size(); i++) {
	        listeners.get(i).onFoundControllersChanged();
	    }
	}
	
	public void onControllerStateUpdated(int index, StickState state) {
	    updateControllerState(index, state);
	    for (int i = 0; i < listeners.size(); i++) {
	    	listeners.get(i).onControllerStateChanged();
	    }
	}
}


