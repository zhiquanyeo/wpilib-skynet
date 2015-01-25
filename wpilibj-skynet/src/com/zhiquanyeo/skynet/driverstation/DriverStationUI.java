package com.zhiquanyeo.skynet.driverstation;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.GridLayout;

import javax.swing.JRadioButton;
import javax.swing.BoxLayout;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.RowSpec;
import com.zhiquanyeo.skynet.driverstation.ControllerState.StickState;

import javax.swing.ButtonGroup;
import javax.swing.JToggleButton;
import javax.swing.AbstractAction;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import java.awt.event.ActionListener;

public class DriverStationUI extends JFrame implements IControllerStateListener {

	private JPanel contentPane;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	
	
	private DriverStationUIListener m_listener;
	
	protected ControllerWatcher controllerWatcher;
    protected ControllerState controllerState;
    protected ControllerStateWatcher controllerStateWatcher;
	
	/**
	 * Create the frame.
	 */
	public DriverStationUI(DriverStationUIListener listener) {
		m_listener = listener;
		
		m_listener.modeChanged(0);
		m_listener.enabledStateChanged(false);
		
		controllerState = new ControllerState();
		controllerState.addListener(this);
		
		controllerWatcher = new ControllerWatcher(controllerState);
		new Thread(controllerWatcher).start();
		
		controllerStateWatcher = new ControllerStateWatcher(controllerState);
        new Thread(controllerStateWatcher).start();
		
		setTitle("Skynet Driver Station");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JRadioButton rdbtnAutonomous = new JRadioButton("Autonomous");
		rdbtnAutonomous.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m_listener.modeChanged(0);
			}
		});
		rdbtnAutonomous.setSelected(true);
		buttonGroup.add(rdbtnAutonomous);
		rdbtnAutonomous.setBounds(6, 6, 141, 23);
		contentPane.add(rdbtnAutonomous);
		
		JRadioButton rdbtnTeleoperated = new JRadioButton("Teleoperated");
		rdbtnTeleoperated.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m_listener.modeChanged(1);
			}
		});
		buttonGroup.add(rdbtnTeleoperated);
		rdbtnTeleoperated.setBounds(6, 30, 141, 23);
		contentPane.add(rdbtnTeleoperated);
		
		JRadioButton rdbtnTest = new JRadioButton("Test");
		rdbtnTest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				m_listener.modeChanged(2);
			}
		});
		buttonGroup.add(rdbtnTest);
		rdbtnTest.setBounds(6, 54, 141, 23);
		contentPane.add(rdbtnTest);
		
		JToggleButton tglbtnEnable = new JToggleButton("Enable");
		tglbtnEnable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (tglbtnEnable.isSelected()) {
					tglbtnEnable.setText("Disable");
					m_listener.enabledStateChanged(true);
				}
				else {
					tglbtnEnable.setText("Enable");
					m_listener.enabledStateChanged(false);
				}
			}
		});
		tglbtnEnable.setBounds(6, 78, 141, 29);
		contentPane.add(tglbtnEnable);
	}


	@Override
	public void onControllerStateChanged() {
		// TODO Auto-generated method stub
		//Get the new stick states
		for (int i = 0; i < controllerState.getFoundControllers().size(); i++) {
			StickState stickState = controllerState.getStickState(i);
			m_listener.stickUpdated(i, stickState);
			//System.out.println("stickState[" + i + "] " + stickState);
		}
	}


	@Override
	public void onFoundControllersChanged() {
		// TODO Auto-generated method stub
		
	}
	
}
