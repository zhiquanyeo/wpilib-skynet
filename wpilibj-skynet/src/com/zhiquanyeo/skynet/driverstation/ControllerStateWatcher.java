package com.zhiquanyeo.skynet.driverstation;

import java.util.ArrayList;

import net.java.games.input.Component;
import net.java.games.input.Controller;

public class ControllerStateWatcher implements Runnable {
    
    protected ControllerState controllerState;
    
    public ControllerStateWatcher(ControllerState state) {
        controllerState = state;
    }

    @Override
    public void run() {
        while (true) {
            //Get the controller list
            ArrayList<Controller> controllers = controllerState.getFoundControllers();
            for (int i = 0; i < controllers.size(); i++) {
                Controller theController = controllers.get(i);
                ControllerState.StickState stickState = new ControllerState.StickState();
                
                if (!theController.poll()) {
                    System.out.println("Controller " + i + " disconnected");
                    continue;
                }
                
                int xPct = 0, yPct = 0;
                Component [] components = theController.getComponents();
                for (int j = 0; j < components.length; j++) {
                    Component component = components[j];
                    Component.Identifier componentIdentifier = component.getIdentifier();
                    
                    //Buttons
                    if (componentIdentifier.getName().matches("^[0-9]*$")) {
                        boolean isPressed = true;
                        if (component.getPollData() == 0.0f)
                            isPressed = false;
                        
                        //Button index
                        String buttonIdxString = component.getIdentifier().toString();
                        int buttonIdx = Integer.parseInt(buttonIdxString);
                        
                        if (buttonIdx >= 0 && buttonIdx < stickState.buttons.length) {
                            stickState.buttons[buttonIdx] = isPressed;
                        }
                        //Button processing complete
                        continue;
                    }
                    
                    //Axes
                    if (component.isAnalog()) {
                        //We get -1.0 to 1.0 
                        float axisValue = component.getPollData();
                        //We need to convert to a byte value
                        byte axisValueByte;
                        if (axisValue < 0.0) {
                            axisValueByte = (byte)(axisValue * 128);
                        }
                        else {
                            axisValueByte = (byte)(axisValue * 127);
                        }
                        
                        if (componentIdentifier == Component.Identifier.Axis.X) {
                            stickState.x = axisValueByte;
                            continue;
                        }
                        
                        if (componentIdentifier == Component.Identifier.Axis.Y) {
                            stickState.y = axisValueByte;
                            continue;
                        }
                        
                        if (componentIdentifier == Component.Identifier.Axis.Z) {
                            stickState.z = axisValueByte;
                            continue;
                        }
                        
                        //TODO: Throttle?
                    }
                }
                
                controllerState.onControllerStateUpdated(i, stickState);
            }
            
            try {
                Thread.sleep(20);
            }
            catch (InterruptedException e) {
                return;
            }
        }
    }
    
}
