package edu.wpi.first.wpilibj;

import com.zhiquanyeo.skynet.SkynetSpeedController;

import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;

public class Jaguar implements SpeedController, PIDOutput, MotorSafety, LiveWindowSendable {

	private int channel;
	private SkynetSpeedController impl;
	
	private MotorSafetyHelper m_safetyHelper;
	
	private ITable m_table;
	private ITableListener m_tableListener;
	
	private void initJaguar(final int channel) {
		this.channel = channel;
		impl = new SkynetSpeedController("skynet/control/pwm/" + channel);
		
		m_safetyHelper = new MotorSafetyHelper(this);
		m_safetyHelper.setExpiration(0.0);
		m_safetyHelper.setSafetyEnabled(false);
		
		LiveWindow.addActuator("Jaguar", channel, this);
	}
	
	public Jaguar(final int channel) {
		initJaguar(channel);
	}
	
	@Override
	public void initTable(ITable subtable) {
		m_table = subtable;
		updateTable();
	}

	@Override
	public ITable getTable() {
		return m_table;
	}

	@Override
	public String getSmartDashboardType() {
		return "Speed Controller";
	}

	@Override
	public void updateTable() {
		if (m_table != null) {
			m_table.putNumber("Value", get());
		}
	}

	@Override
	public void startLiveWindowMode() {
		set(0.0);
		m_tableListener = new ITableListener() {
			public void valueChanged(ITable itable, String key, Object value, boolean bln) {
				set(((Double) value).doubleValue());
			}
		};
		m_table.addTableListener("Value", m_tableListener, true);
	}

	@Override
	public void stopLiveWindowMode() {
		set(0.0);
		m_table.removeTableListener(m_tableListener);
	}

	@Override
	public void setExpiration(double timeout) {
		m_safetyHelper.setExpiration(timeout);
	}

	@Override
	public double getExpiration() {
		return m_safetyHelper.getExpiration();
	}

	@Override
	public boolean isAlive() {
		return m_safetyHelper.isAlive();
	}

	@Override
	public void stopMotor() {
		disable();
	}

	@Override
	public void setSafetyEnabled(boolean enabled) {
		m_safetyHelper.setSafetyEnabled(enabled);
	}

	@Override
	public boolean isSafetyEnabled() {
		return m_safetyHelper.isSafetyEnabled();
	}

	@Override
	public String getDescription() {
		return "PWM " + channel + " on module 0";
	}

	@Override
	public void pidWrite(double output) {
		impl.pidWrite(output);
	}

	@Override
	public double get() {
		return impl.get();
	}

	@Override
	public void set(double speed, byte syncGroup) {
		impl.set(speed, syncGroup);
	}

	@Override
	public void set(double speed) {
		impl.set(speed);
	}

	@Override
	public void disable() {
		impl.set(0);
	}

}
