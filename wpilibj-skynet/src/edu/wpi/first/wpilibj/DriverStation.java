package edu.wpi.first.wpilibj;

import java.nio.ByteBuffer;

/**
 * Provide access to the network communication data to/from the Driver Station 
 * @author zhiquan
 *
 */
public class DriverStation implements RobotState.Interface {

	/**
	 * Number of Joystick ports
	 */
	public static final int kJoystickPorts = 6;
	public static final int kMaxJoystickAxes = 12;
	public static final int kMaxJoystickPOVs = 12;
	
	private class HALJoystickButtons {
		public int buttons;
		public byte count;
	}
	
	/**
	 * The robot alliance that the robot is a part of
	 */
	public enum Alliance {Red, Blue, Invalid};
	
	private static final double JOYSTICK_UNPLUGGED_MESAGE_INTERVAL = 1.0;
	private double m_nextMessageTime = 0.0;
	
	private static class DriverStationTask implements Runnable {
		
		private DriverStation m_ds;
		
		DriverStationTask(DriverStation ds) {
			m_ds = ds;
		}
		
		@Override
		public void run() {
			m_ds.task();
		}	
	} /* End of DriverStationTask */
	
	private static DriverStation instance = new DriverStation();
	
	private short[][] m_joystickAxes = new short[kJoystickPorts][kMaxJoystickAxes];
	private short[][] m_joystickPOVs = new short[kJoystickPorts][kMaxJoystickPOVs];
	private HALJoystickButtons[] m_joystickButtons = new HALJoystickButtons[kJoystickPorts];
	
	private Thread m_thread;
	private final Object m_dataSem;
	private volatile boolean m_thread_keepalive = true;
	private boolean m_userInDisabled = false;
	private boolean m_userInAutonomous = false;
	private boolean m_userInTeleop = false;
	private boolean m_userInTest = false;
	private boolean m_newControlData;
	//private final ByteBuffer m_packetDataAvailableMutex;
	//private final ByteBuffer m_packetDataAvailableSem;
	
	/**
	 * Gets an instance of the DriverStation
	 * 
	 * @return The DriverStation
	 */
	public static DriverStation getInstance() {
		return DriverStation.instance;
	}
	
	/**
	 * DriverStation constructor
	 * 
	 * The single DriverStation instance is created statically with the
	 * instance static member variable
	 */
	protected DriverStation() {
		m_dataSem = new Object();
		for (int i = 0; i < kJoystickPorts; i++) {
			m_joystickButtons[i] = new HALJoystickButtons();
		}
		
		//NOTE: We might want to use a semaphore hooked up to our listening thread
		
		//TODO We should start up our listening utility here
		
		//This starts the driver station instance
		m_thread = new Thread(new DriverStationTask(this), "FRCDriverStation");
		m_thread.setPriority((Thread.NORM_PRIORITY + Thread.MAX_PRIORITY) / 2);
		
		m_thread.start();
	}
	
	/**
	 * Kill the thread
	 */
	public void release() {
		m_thread_keepalive = false;
	}
	
	/** 
	 * Provides the service routine for the DS polling thread
	 */
	private void task() {
		int safetyCounter = 0;
		while (m_thread_keepalive) {
			//TODO Wait on our comms util?
			synchronized (this) {
				getData();
			}
			synchronized (m_dataSem) {
				m_dataSem.notifyAll();
			}
			
			//TODO something with motor safety? maybe?
			
			//TODO Signal that something is happening
			//I think this sends a signal BACK to the DS
			if (m_userInDisabled) {
				//Signal that we are in disabled mode
			}
			if (m_userInAutonomous) {
				//Signal that we are in autonomous
			}
			if (m_userInTeleop) {
				//Signal that we are in teleop
			}
			if (m_userInTest) {
				//Signal that we are in test
			}
		}
	}
	
	/**
	 * Wait for new data from the driver station
	 */
	public void waitForData() {
		waitForData(0);
	}
	
	/** 
	 * Wait for new data or for timeout, whichever comes first. If timeout is
	 * 0, wait for new data only
	 * 
	 * @param timeout The maximum time in milliseconds to wait
	 */
	public void waitForData(long timeout) {
		synchronized(m_dataSem) {
			try {
				m_dataSem.wait(timeout);
			} catch (InterruptedException ex) {}
		}
	}
	
	/**
	 * copy data from the DS task for the user.
	 * If no new data exists, it will just be returned, otherwise
	 * the data will be copied from the DS polling loop
	 */
	protected synchronized void getData() {
		//Get the status of all the joysticks
		for (byte stick = 0; stick < kJoystickPorts; stick++) {
			//TODO Get the joystick axes and POV values from our comms thread
			//m_joystickAxes[stick] = 
			//m_joystickPOVs[stick] = 
			ByteBuffer countBuffer = ByteBuffer.allocateDirect(1);
			//m_joystickButtons[stick].buttons = 
			m_joystickButtons[stick].count = countBuffer.get();
		}
		
		m_newControlData = true;
	}
	
	/**
	 * Battery voltage
	 * 
	 * @return The battery voltage in volts
	 */
	public double getBatteryVoltage() {
		return 12.0;
	}
	
	/**
	 * Reports errors related to unplugged joysticks
	 * Throttle the errors so that they don't overwhelm the DS
	 */
	private void reportJoystickUnpluggedError(String message) {
		double currentTime = Timer.getFPGATimestamp();
		if (currentTime > m_nextMessageTime) {
			reportError(message, false);
			m_nextMessageTime = currentTime + JOYSTICK_UNPLUGGED_MESAGE_INTERVAL;
		}
	}
	
	/**
	 * Get the value of the axis on a joystick
	 * This depends on the mapping of the joystick connected to the specific port
	 * 
	 * @param stick The joystick to read
	 * @param axis The analog axis value to read from the joystick
	 * @return The value of the axis on the joystick
	 */
	public synchronized double getStickAxis(int stick, int axis) {
		if (stick < 0 || stick >= kJoystickPorts) {
			throw new RuntimeException("Joystick index is out of range, should be 0-5");
		}
		
		if (axis < 0 || axis > kMaxJoystickAxes) {
			throw new RuntimeException("Joystick axis is out of range");
		}
		
		if (axis >= m_joystickAxes[stick].length){
			reportJoystickUnpluggedError("WARNING: Joystick axis " + axis + " on port " + stick + " not available. Check if controller is plugged in\n");
			return 0.0;
		}
		
		byte value = (byte)m_joystickAxes[stick][axis];
		
		if (value < 0) {
			return value / 128.0;
		}
		else {
			return value / 127.0;
		}
	}
	
	/**
	 * Returns the number of axes on a given joystick port
	 * 
	 * @param stick The joystick port number
	 * @return The number of axes on the indicated joystick
	 */
	public synchronized int getStickAxisCount(int stick) {
		if (stick < 0 || stick >= kJoystickPorts) {
			throw new RuntimeException("Joystick index is out of range, should be 0-5");
		}
		
		return m_joystickAxes[stick].length;
	}
	
	/**
	 * Get the state of a POV on the joystick
	 * 
	 * @return The angle of the POV in degrees, or -1 if the POV is not pressed
	 */
	public synchronized int getStickPOV(int stick, int pov) {
		if (stick < 0 || stick >= kJoystickPorts) {
			throw new RuntimeException("Joystick index is out of range, should be 0-5");
		}
		
		if (pov < 0 || pov > kMaxJoystickPOVs) {
			throw new RuntimeException("Joystick POV is out of range");
		}
		
		if (pov >= m_joystickPOVs[stick].length){
			reportJoystickUnpluggedError("WARNING: Joystick POV " + pov + " on port " + stick + " not available. Check if controller is plugged in\n");
			return 0;
		}
		
		return m_joystickPOVs[stick][pov];
	}
	
	/**
	 * Returns the number of POVs on a given joystick port
	 * 
	 * @param stick The joystick port number
	 * @return The number of POVs on the indicated joystick
	 */
	public synchronized int getStickPOVCount(int stick) {
		if (stick < 0 || stick >= kJoystickPorts) {
			throw new RuntimeException("Joystick index is out of range, should be 0-5");
		}
		
		return m_joystickPOVs[stick].length;
	}
	
	/**
	 * The state of the buttons on the joystick
	 * 
	 * @param stick The joystick to read
	 * @return The state of the buttons on the joystick
	 */
	public synchronized int getStickButtons(final int stick) {
		if (stick < 0 || stick >= kJoystickPorts) {
			throw new RuntimeException("Joystick index is out of range, should be 0-5");
		}
		
		return m_joystickButtons[stick].buttons;
	}
	
	/**
	 * The state of one joystick button. Button indices begin at 1
	 * 
	 * @param stick The joystick to read
	 * @param button The button index, beginning at 1
	 * @return the state of the joystick button
	 */
	public synchronized boolean getStickButton(final int stick, byte button) {
		if (stick < 0 || stick >= kJoystickPorts) {
			throw new RuntimeException("Joystick index is out of range, should be 0-5");
		}
		
		if (button > m_joystickButtons[stick].count){
			reportJoystickUnpluggedError("WARNING: Joystick Button " + button + " on port " + stick + " not available. Check if controller is plugged in\n");
			return false;
		}
		
		if (button <= 0) {
			reportJoystickUnpluggedError("ERROR: Button indexes begin at 1\n");
			return false;
		}
		return ((0x1 << (button - 1)) & m_joystickButtons[stick].buttons)!= 0; 
	}
	
	/**
	 * Gets the number of buttons on a joystick
	 * 
	 * @param stick The joystick port number
	 * @return The number of buttons on the indicated joystick
	 */
	public synchronized int getStickButtonCount(int stick) {
		if (stick < 0 || stick >= kJoystickPorts) {
			throw new RuntimeException("Joystick index is out of range, should be 0-5");
		}
		
		return m_joystickButtons[stick].count;
	}
	
	/**
	 * Gets a value indicating whether the Driver Station requires the
	 * robot to be disabled
	 * 
	 * @return True if the robot should be disabled, false otherwise
	 */
	@Override
	public boolean isDisabled() {
		return !isEnabled();
	}

	/**
	 * Gets a value indicating whether the Driver Station requires the
	 * robot to be enabled
	 * 
	 * @return True if the robot is enabled, false otherwise
	 */
	@Override
	public boolean isEnabled() {
		// TODO Get a HALControlWord from our communications module
		return false;
	}

	/**
	 * Gets a value indicating whether the Driver Station requires the
	 * robot to be running in operator-controlled mode
	 * 
	 * @return True if operator-controlled mode should be enabled, false otherwise
	 */
	@Override
	public boolean isOperatorControl() {
		return !(isAutonomous() || isTest());
	}

	/**
	 * Gets a value indicating whether the Driver Station requires the
	 * robot to be running in autonomous mode
	 * 
	 * @return True if autonomous mode should be enabled, false otherwise
	 */
	@Override
	public boolean isAutonomous() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Gets a value indicating whether the Driver Station requires the
	 * robot to be running in test mode
	 * 
	 * @return True if test mode should be enabled, false otherwise
	 */
	@Override
	public boolean isTest() {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * Gets a value indicating whether FPGA outputs are enabled
	 * 
	 * @return True if FPGA outputs are enabled
	 */
	public boolean isSysActive() {
		return true;
	}
	
	/**
	 * Check if the system is browned out
	 * 
	 * @return True if the system is browned out
	 */
	public boolean isBrownedOut() {
		return false;
	}
	
	/**
	 * Has a new control packet from the driver station arrived since the last time
	 * this was called
	 * 
	 * @return True if control data has updated
	 */
	public synchronized boolean isNewControlData() {
		boolean result = m_newControlData;
		m_newControlData = false;
		return result;
	}
	
	/**
	 * Get the current alliance from the FMS
	 * @return the current alliance
	 */
	public Alliance getAlliance() {
		//TODO Actually grab an alliance ID from our configuration
		return Alliance.Red;
	}
	
	/**
	 * Gets the location of the team's driver station controls
	 * @return the location: 1, 2, 3
	 */
	public int getLocation() {
		return 1;
	}
	
	public boolean isFMSAttached() {
		return false;
	}
	
	public boolean isDSAttached() {
		//TODO Actually pick up whether or not we were connected
		return true;
	}
	
	/**
	 * Return the approximate match time
	 * @return Time remaining in current match period in seconds
	 */
	public double getMatchTime() {
		return 0.0;
	}
	
	/**
	 * Report error to driver station
	 * @param printTrace If true, append stack trace to error string
	 */
	public static void reportError(String error, boolean printTrace) {
		String errorString = error;
		if (printTrace) {
			errorString += " at ";
			StackTraceElement[] traces = Thread.currentThread().getStackTrace();
			for (int i = 2; i < traces.length; i++) {
				errorString += traces[i].toString() + "\n";
			}
		}
		System.err.println(errorString);
		//TODO Send this to the driver station
	}
	
	/**
	 * Only to be used to tell the DS what code you claim to be executing
	 * for diagnostic purposes only
	 * @param entering If true, starting disabled code; if false leaving disabled code
	 */
	public void InDisabled(boolean entering) {
		m_userInDisabled = entering;
	}
	
	/**
	 * Only to be used to tell the DS what code you claim to be executing
	 * for diagnostic purposes only
	 * @param entering If true, starting auto code; if false leaving auto code
	 */
	public void InAutonomous(boolean entering) {
		m_userInAutonomous = entering;
	}
	
	/**
	 * Only to be used to tell the DS what code you claim to be executing
	 * for diagnostic purposes only
	 * @param entering If true, starting teleop code; if false leaving teleop code
	 */
	public void InOperatorControl(boolean entering) {
		m_userInTeleop = entering;
	}
	
	/**
	 * Only to be used to tell the DS what code you claim to be executing
	 * for diagnostic purposes only
	 * @param entering If true, starting test code; if false leaving test code
	 */
	public void InTest(boolean entering) {
		m_userInTest = entering;
	}
}
