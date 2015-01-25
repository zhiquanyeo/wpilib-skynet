package com.zhiquanyeo.skynet.driverstation;

public interface IControllerStateUpdateListener {
	void onControllerStateUpdated(int index, ControllerState.StickState stickState);
    
}
