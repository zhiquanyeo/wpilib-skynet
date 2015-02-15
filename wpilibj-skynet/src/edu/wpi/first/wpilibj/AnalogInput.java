package edu.wpi.first.wpilibj;

import com.zhiquanyeo.skynet.SkynetAnalogInput;

import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.tables.ITable;

public class AnalogInput extends SensorBase implements PIDSource,
		LiveWindowSendable {
	
	private SkynetAnalogInput m_impl;
	private int m_channel;
	
	private ITable m_table;
	
	public AnalogInput(final int channel) {
		m_channel = channel;
		m_impl = new SkynetAnalogInput("skynet/robot/sensors/analog/" + channel);
		
		LiveWindow.addSensor("AnalogInput", channel, this);
	}
	
	public void free() {
		m_channel = 0;
	}
	
	public double getVoltage() {
		return m_impl.get();
	}
	
	public double getAverageVoltage() {
		return getVoltage();
	}
	
	public int getChannel() {
		return m_channel;
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
		return "Analog Input";
	}

	@Override
	public void updateTable() {
		if (m_table != null) {
			m_table.putNumber("Value",  getAverageVoltage());
		}
	}

	@Override
	public void startLiveWindowMode() {
		// TODO Auto-generated method stub

	}

	@Override
	public void stopLiveWindowMode() {
		// TODO Auto-generated method stub

	}

	@Override
	public double pidGet() {
		return getAverageVoltage();
	}

}
