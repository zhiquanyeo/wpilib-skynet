package com.zhiquanyeo.skynet.driverstation;

public class DriverStationData {
	
	private static DriverStationData instance;
	
	public static final int kJoystickPorts = 6;
	public static final int kMaxJoystickAxes = 12;
	public static final int kMaxJoystickPOVs = 12;
	
	private static short[][] m_joystickAxes = new short[kJoystickPorts][kMaxJoystickAxes];
	private static short[][] m_joystickPOVs = new short[kJoystickPorts][kMaxJoystickPOVs];
	private static int[] m_joystickButtons = new int[kJoystickPorts];
	
	private DriverStationData() {
		
	}
	
	public static synchronized void putJoystickAxes(int stick, short[] axes) {
		for (int i = 0; i < axes.length; i++) {
			m_joystickAxes[stick][i] = axes[i];
		}
	}
	
	public static synchronized short[] getJoystickAxes(int stick) {
		return m_joystickAxes[stick];
	}
	
	public static synchronized void putJoystickButtons(int stick, boolean[] buttons) {
		int temp = 0;
		for (int i = 0; i < buttons.length; i++) {
			if (buttons[i]) {
				temp |= (1 << i);
			}
		}
		m_joystickButtons[stick] = temp;
	}
	
	public static synchronized int getJoystickButtons(int stick) {
		return m_joystickButtons[stick];
	}
}
