package com.zhiquanyeo.skynet.driverstation;

import java.util.ArrayList;

import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.ControllerEvent;
import net.java.games.input.ControllerListener;

public class ControllerWatcher implements Runnable, ControllerListener {

	protected IControllerUpdateListener updateListener;
    protected ArrayList<Controller> foundControllers;
    protected int numControllers = 0;
    
    public ControllerWatcher(IControllerUpdateListener listener) {
        updateListener = listener;
        foundControllers = new ArrayList<Controller>();
        
        ControllerEnvironment.getDefaultEnvironment().addControllerListener(this);
    }
    
    public synchronized ArrayList<Controller> getFoundControllers() {
        return foundControllers;
    }

    @Override
    public void run() {
        while (true) {
            Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
            int numLegalControllers = 0;
            
            foundControllers.clear();
            
            for (int i = 0; i < controllers.length; i++) {
                Controller theController = controllers[i];
                if (theController.getType() == Controller.Type.STICK ||
                    theController.getType() == Controller.Type.GAMEPAD ||
                    theController.getType() == Controller.Type.WHEEL ||
                    theController.getType() == Controller.Type.FINGERSTICK) {
                    
                    //This is a legal controller
                    numLegalControllers++;
                    foundControllers.add(theController);
                }
            }
            
            if (numLegalControllers != numControllers) {
                numControllers = numLegalControllers;
                updateListener.onControllerListUpdated(foundControllers);
            }
            
            try {
                Thread.sleep(500);
            }
            catch (InterruptedException e) {
                System.out.println("ControllerWatcher stopping");
                return;
            }
        }
    }

    @Override
    public void controllerRemoved(ControllerEvent ev) {
        System.out.println("Controller Removed");
    }

    @Override
    public void controllerAdded(ControllerEvent ev) {
        System.out.println("Controller Added");
    }

}
