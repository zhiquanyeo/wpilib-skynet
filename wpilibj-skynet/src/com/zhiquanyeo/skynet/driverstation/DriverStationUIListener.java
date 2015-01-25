package com.zhiquanyeo.skynet.driverstation;

import com.zhiquanyeo.skynet.driverstation.ControllerState.StickState;

public interface DriverStationUIListener {
	void enabledStateChanged(boolean isEnabled);
	void modeChanged(int mode);
	void stickUpdated(int index, StickState state);
}
