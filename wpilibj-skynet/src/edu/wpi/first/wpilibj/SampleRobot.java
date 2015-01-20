package edu.wpi.first.wpilibj;

import edu.wpi.first.wpilibj.livewindow.LiveWindow;

/**
 * A simple robot base class that knows the standard FRC competition states
 * 
 * You can build a simple robot program off of this by overriding the
 * robotInit(), disabled(), autonomous() and operatorControl() methods.
 * The startCompetition() method will call these methods (sometimes repeatedly)
 * depending on the state of the competition.
 * 
 * Alternatively, you can override the robotMain() method and manage all
 * aspects of the robot yourself
 *
 */
public class SampleRobot extends RobotBase {
	
	private boolean m_robotMainOverriden;
	
	public SampleRobot() {
		super();
		m_robotMainOverriden = true;
	}
	
	/**
	 * Robot-wide initialization code should go here
	 * 
	 * Users should override this method for default robot-wide initialization
	 * which will be called when the robot is first powered on.
	 * 
	 * Called exactly once when the competition starts.
	 */
	protected void robotInit() {
		System.out.println("Default robotInit() method running, consider providing your own");
	}
	
	/**
	 * Disabled should go here
	 * Users should overload this method to run code that should run while the field
	 * is disabled.
	 * 
	 * Called once each time the robot enters the disabled state.
	 */
	protected void disabled() {
		System.out.println("Default disabled() method running, consider providing your own");
	}
	
	/**
	 * Autonomous should go here
	 * Users should add autonomous code to this method that should run while the
	 * robot is in the autonomous period.
	 * 
	 * Called once each time the robot enters the autonomous state.
	 */
	protected void autonomous() {
		System.out.println("Default autonomous() method running, consider providing your own");
	}
	
	/**
	 * Operator control should go here
	 */
	protected void operatorControl() {
		System.out.println("Default operatorControl() method running, consider providing your own");
	}
	
	protected void test() {
		System.out.println("Default test() method running, consider providing your own");
	}
	
	/**
	 * Robot main program for free-form programs
	 * 
	 */
	public void robotMain() {
		m_robotMainOverriden = false;
	}
	
	/** 
	 * Start a competition
	 * 
	 */
	@Override
	public void startCompetition() {
		robotMain();
		if (!m_robotMainOverriden) {
			// First and one-time initialization
			LiveWindow.setEnabled(false);
			robotInit();
			
			while (true) {
				if (isDisabled()) {
					m_ds.InDisabled(true);
					disabled();
					m_ds.InDisabled(false);
					while (isDisabled()) {
						Timer.delay(0.01);
					}
				}
				else if (isAutonomous()) {
					m_ds.InAutonomous(true);
					autonomous();
					m_ds.InAutonomous(false);
					while (isAutonomous() && !isDisabled()) {
						Timer.delay(0.01);
					}
				}
				else if (isTest()) {
					LiveWindow.setEnabled(true);
					m_ds.InTest(true);
					test();
					m_ds.InTest(false);
					while (isTest() && isEnabled()) {
						Timer.delay(0.01);
					}
					LiveWindow.setEnabled(false);
				}
				else {
					m_ds.InOperatorControl(true);
					operatorControl();
					m_ds.InOperatorControl(false);
					while (isOperatorControl() && !isDisabled()) {
						Timer.delay(0.01);
					}
				}
			}
		}
	}

}
