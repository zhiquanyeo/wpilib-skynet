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
import javax.swing.ButtonGroup;
import javax.swing.JToggleButton;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import javax.swing.Action;
import java.awt.event.ActionListener;

public class DriverStationUI extends JFrame {

	private JPanel contentPane;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	
	
	private DriverStationUIListener m_listener;

	
	/**
	 * Create the frame.
	 */
	public DriverStationUI(DriverStationUIListener listener) {
		m_listener = listener;
		
		m_listener.modeChanged(0);
		m_listener.enabledStateChanged(false);
		
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
	
}
