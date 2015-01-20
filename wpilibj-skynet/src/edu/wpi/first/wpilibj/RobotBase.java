package edu.wpi.first.wpilibj;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.Manifest;

import com.zhiquanyeo.skynet.SkynetNode;

import edu.wpi.first.wpilibj.internal.SkynetTimer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

/**
 * Implement a Robot Program framework
 * The RobotBase class is intended to be subclassed by a user creating a robot program
 * Overriden autonomous() and operatorControl() methods are called at the appropriate time
 * as the match proceeds. 
 *
 */
public abstract class RobotBase {
	/**
	 * The VxWorks priority that robot code should work at
	 */
	public static final int ROBOT_TASK_PRIORITY = 101;
	
	protected final DriverStation m_ds;
	
	/**
	 * Constructor for a generic robot program
	 * User code should be placed in the constructor that runs before the Autonomous
	 * or Operator Control period starts. The constructor will run to completion
	 * before autonomous is entered.
	 * 
	 * This must be used to ensure that the communications code starts. 
	 */
	protected RobotBase() {
		NetworkTable.setServerMode();
		m_ds = DriverStation.getInstance();
		NetworkTable.getTable(""); //Force network tables to initialize
		NetworkTable.getTable("LiveWindow").getSubTable("~STATUS~").putBoolean("LW Enabled",  false);
	}
	
	/**
	 * Free the resources for a RobotBase class
	 */
	public void free() {
		
	}
	
	/**
	 * @return If the robot is running skynet mode
	 */
	public static boolean isSkynet() {
		return true;
	}
	
	/**
	 * @return If the robot is running in the real world
	 */
	public static boolean isReal() {
		return false;
	}
	
	/**
	 * Determine if the Robot is currently disabled.
	 * @return True if the Robot is currently disabled by field controls
	 */
	public boolean isDisabled() {
		return m_ds.isDisabled();
	}
	
	/**
	 * Determine id the Robot is currently enabled
	 * @return True if the Robot is currently enabled by the field controls
	 */
	public boolean isEnabled() {
		return m_ds.isEnabled();
	}
	
	/**
	 * Determine if the Robot is currently in Autonomous mode
	 * @return True if the robot is currently operating autonomously
	 */
	public boolean isAutonomous() {
		return m_ds.isAutonomous();
	}
	
	public boolean isTest() {
		return m_ds.isTest();
	}
	
	public boolean isOperatorControl() {
		return m_ds.isOperatorControl();
	}
	
	public boolean isNewDataAvailable() {
		return m_ds.isNewControlData();
	}
	
	/**
	 * Provide an alternate "main loop" via startCompetition()
	 */
	public abstract void startCompetition();
	
	public static boolean getBooleanProperty(String name, boolean defaultValue) {
		String propVal = System.getProperty(name);
		if (propVal == null) {
			return defaultValue;
		}
		if (propVal.equalsIgnoreCase("false")) {
			return false;
		}
		else if (propVal.equalsIgnoreCase("true")) {
			return true;
		}
		else {
			throw new IllegalStateException(propVal);
		}
	}
	
	/**
	 * Starting point for the application
	 */
	public static void main(String args[]) {
		boolean errorOnExit = false;
		
		//TODO: We need to start up the skynet MQTT connection
		
		//Set some implementations so that the static methods work properly
		Timer.SetImplementation(new SkynetTimer());
		RobotState.SetImplementation(DriverStation.getInstance());
		HLUsageReporting.SetImplementation(new HLUsageReporting.Null());
		
		String robotName = "";
		Enumeration<URL> resources = null;
		try {
			resources = RobotBase.class.getClassLoader().getResources("META-INF/MANIFEST.MF");
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
		while (resources != null && resources.hasMoreElements()) {
			try {
				Manifest manifest = new Manifest(resources.nextElement().openStream());
				robotName = manifest.getMainAttributes().getValue("Robot-Class");
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("RobotName = " + robotName);
		RobotBase robot = null;
		try {
			robot = (RobotBase) Class.forName(robotName).newInstance();
		}
		catch (InstantiationException|IllegalAccessException|ClassNotFoundException e) {
			System.err.println("WARNING: Robots don't quit!");
			System.err.println("ERROR: Could not instantiate robot " + robotName + "!");
		}
		
		try {
			robot.startCompetition();
		}
		catch (Throwable t) {
			t.printStackTrace();
			errorOnExit = true;
		}
		finally {
			System.err.println("WARNING: Robots don't quit!");
			if (errorOnExit) {
				System.err.println("---> The startCompetition() method (or methods called by it) should have handled the exception above.");
			}
			else {
				System.err.println("---> Unexpected return from startCompetition() method.");
			}
		}
	}
}
