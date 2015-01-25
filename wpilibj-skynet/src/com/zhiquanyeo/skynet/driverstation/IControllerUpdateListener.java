package com.zhiquanyeo.skynet.driverstation;

import java.util.ArrayList;

import net.java.games.input.Controller;

public interface IControllerUpdateListener {
	void onControllerListUpdated(ArrayList<Controller> controllerList);
}
