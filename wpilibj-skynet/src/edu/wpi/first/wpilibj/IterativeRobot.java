package edu.wpi.first.wpilibj;

import edu.wpi.first.wpilibj.livewindow.LiveWindow;

public class IterativeRobot extends RobotBase {
	private boolean m_disabledInitialized;
	private boolean m_autonomousInitialized;
	private boolean m_teleopInitialized;
	private boolean m_testInitialized;
	
	public IterativeRobot() {
		m_disabledInitialized = false;
		m_autonomousInitialized = false;
		m_teleopInitialized = false;
		m_testInitialized = false;
	}
	
	@Override
	public void startCompetition() {
		robotInit();
		
		//tracing support
		final int TRACE_LOOP_MAX = 100;
		int loopCount = TRACE_LOOP_MAX;
		Object marker = null;
		boolean didDisabledPeriodic = false;
		boolean didAutonomousPeriodic = false;
		boolean didTeleopPeriodic = false;
		boolean didTestPeriodic = false;
		
		//Loop forever, calling appropriate mode-dependent function
		LiveWindow.setEnabled(true);
		while (true) {
			if (isDisabled()) {
				// call disableinit if we are not just entering disabled from
				// either a differnt mode or from power-on
				if (!m_disabledInitialized) {
					LiveWindow.setEnabled(false);
					disabledInit();
					m_disabledInitialized = true;
					//reset the rest
					m_autonomousInitialized = false;
					m_teleopInitialized = false;
					m_testInitialized = false;
				}
				if (nextPeriodReady()) {
					disabledPeriodic();
					didDisabledPeriodic = true;
				}
			}
			else if (isTest()) {
				if (!m_testInitialized) {
					LiveWindow.setEnabled(true);
					testInit();
					m_testInitialized = true;
					m_autonomousInitialized = false;
					m_teleopInitialized = false;
					m_disabledInitialized = false;
				}
				if (nextPeriodReady()) {
					testPeriodic();
					didTestPeriodic = true;
				}
			}
			else if (isAutonomous()) {
				if (!m_autonomousInitialized) {
					LiveWindow.setEnabled(true);
					autonomousInit();
					m_autonomousInitialized = true;
					m_testInitialized = false;
					m_teleopInitialized = false;
					m_disabledInitialized = false;
				}
				if (nextPeriodReady()) {
					autonomousPeriodic();
					didAutonomousPeriodic = true;
				}
			}
			else {
				if (!m_teleopInitialized) {
					LiveWindow.setEnabled(false);
					teleopInit();
					m_teleopInitialized = true;
					m_testInitialized = false;
					m_autonomousInitialized = false;
					m_disabledInitialized = false;
				}
				if (nextPeriodReady()) {
					teleopPeriodic();
					didTeleopPeriodic = true;
				}
			}
			m_ds.waitForData();
		}
	}

	private boolean nextPeriodReady() {
		return m_ds.isNewControlData();
	}
	
	public void robotInit() {
		System.out.println("Default IterativeRobot.robotInit() method. Consdier providing your own!");
	}
	
	public void disabledInit() {
		System.out.println("Default IterativeRobot.disabledInit() method. Consdier providing your own!");
	}
	
	public void autonomousInit() {
		System.out.println("Default IterativeRobot.autonomousInit() method. Consdier providing your own!");
	}
	
	public void teleopInit() {
		System.out.println("Default IterativeRobot.teleopInit() method. Consdier providing your own!");
	}
	
	public void testInit() {
		System.out.println("Default IterativeRobot.testInit() method. Consdier providing your own!");
	}
	
	private boolean dpFirstRun = true;
	
	public void disabledPeriodic() {
		if (dpFirstRun) {
			System.out.println("Default IterativeRobot.disabledPeriodic() method. Consdier providing your own!");
			dpFirstRun = false;
		}
		Timer.delay(0.001);
	}
	
	private boolean apFirstRun = true;
	
	public void autonomousPeriodic() {
		if (apFirstRun) {
			System.out.println("Default IterativeRobot.autonomousPeriodic() method. Consdier providing your own!");
			apFirstRun = false;
		}
		Timer.delay(0.001);
	}
	
	private boolean tpFirstRun = true;
	
	public void teleopPeriodic() {
		if (tpFirstRun) {
			System.out.println("Default IterativeRobot.teleopPeriodic() method. Consdier providing your own!");
			tpFirstRun = false;
		}
		Timer.delay(0.001);
	}
	
	private boolean tmpFirstRun = true;
	
	public void testPeriodic() {
		if (tmpFirstRun) {
			System.out.println("Default IterativeRobot.testPeriodic() method. Consdier providing your own!");
			tmpFirstRun = false;
		}
		Timer.delay(0.001);
	}
}
