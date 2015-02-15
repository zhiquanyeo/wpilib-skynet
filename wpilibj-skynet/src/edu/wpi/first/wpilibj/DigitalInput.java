package edu.wpi.first.wpilibj;

import com.zhiquanyeo.skynet.SkynetDigitalInput;

import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.tables.ITable;

public class DigitalInput implements LiveWindowSendable {
	private SkynetDigitalInput impl;
	private int m_channel;
	
	public DigitalInput(int channel) {
		impl = new SkynetDigitalInput("skynet/robot/sensors/digital/" + channel);
		m_channel = channel;
	}
	
	public boolean get() {
		return impl.get();
	}
	
	public int getChannel() {
		return m_channel;
	}
	
	public boolean getAnalogTriggerForRouting() {
		return false;
	}
	

	private ITable m_table;
	
	@Override
	public void initTable(ITable subtable) {
		m_table = subtable;
		updateTable();
	}
	
	@Override
	public ITable getTable() {
		// TODO Auto-generated method stub
		return m_table;
	}

	@Override
	public String getSmartDashboardType() {
		return "Digital Input";
	}

	@Override
	public void updateTable() {
		if (m_table != null) {
			m_table.putBoolean("Value", get());
		}
	}

	@Override
	public void startLiveWindowMode() {
		
	}

	@Override
	public void stopLiveWindowMode() {
		// TODO Auto-generated method stub
		
	}

}
