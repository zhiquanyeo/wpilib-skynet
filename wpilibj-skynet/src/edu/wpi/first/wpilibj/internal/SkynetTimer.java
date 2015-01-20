package edu.wpi.first.wpilibj.internal;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;

public class SkynetTimer implements Timer.StaticInterface {
	
	/**
	 * Return system clock time in seconds. In this case, it is
	 * the number of seconds since epoch
	 * 
	 * @return Current wall clock time
	 */
	@Override
	public double getFPGATimestamp() {
		return System.currentTimeMillis() / 1e3;
	}

	@Override
	public double getMatchTime() {
		return DriverStation.getInstance().getMatchTime();
	}

	/**
	 * Pause the thread for a specified time. Pause the execution of the
	 * thread for a specified period of time given in seconds. Motors till
	 * continue to run at their last assigned values, and sensors will
	 * continue to update. Only the task containing the wait will pause 
	 * until the wait time is expired
	 * 
	 * @param seconds Length of time to pause
	 */
	@Override
	public void delay(double seconds) {
		try {
			Thread.sleep((long)(seconds * 1e3));
		} catch (final InterruptedException e) {
			
		}
		
	}

	@Override
	public Timer.Interface newTimer() {
		return new TimerImpl();
	}

	class TimerImpl implements Timer.Interface {
		private long m_startTime;
		private double m_accumulatedTime;
		private boolean m_running;
		
		/**
		 * Create a new timer object and reset the time to zero
		 * The timer is initially not running and must be started
		 */
		public TimerImpl() {
			reset();
		}
		
		private long getMsClock() {
			return System.currentTimeMillis();
		}
		
		/**
		 * Get the current time from the timer. If the clock is running,
		 * it is derived from the current system clock. Otherwise, it returns
		 * the time when it was last stopped.
		 * 
		 * @return Current time value for this timer in seconds
		 */
		@Override
		public synchronized double get() {
			if (m_running) {
				return ((double) ((getMsClock() - m_startTime) + m_accumulatedTime)) / 1000.0;
			}
			else {
				return m_accumulatedTime;
			}
		}

		/**
		 * Reset the timer by setting the time to 0
		 * Make the timer startTime the currentTime so
		 * new requests will be relative
		 */
		@Override
		public synchronized void reset() {
			m_accumulatedTime = 0;
			m_startTime = getMsClock();
		}

		/**
		 * Start the timer
		 * Set the running flag to true, indicating that all time requests
		 * should be relative to the system clock
		 */
		@Override
		public synchronized void start() {
			m_startTime = getMsClock();
			m_running = true;
		}

		/**
		 * Stop the timer
		 * This computes the time as of now and clears the running flag, causing all
		 * subsequent time requests to be read from the accumulated time rather than
		 * looking at the system clock
		 */
		@Override
		public synchronized void stop() {
			final double temp = get();
			m_accumulatedTime = temp;
			m_running = false;
		}

		/**
		 * Check if the period specified has passed and if it has, advance
		 * the start time by that period. This is useful to decide if it's
		 * time to do periodic work without drifting later by the time it took
		 * to get around to checking.
		 * 
		 * @param period The period to check for (in seconds)
		 * @return True if the period has passed
		 */
		@Override
		public synchronized boolean hasPeriodPassed(double period) {
			if (get() > period) {
				//Advance the start time by the period
				//Don't set it to the current time. We want to avoid drift
				m_startTime += period;
				return true;
			}
			return false;
		}
		
	}

}
