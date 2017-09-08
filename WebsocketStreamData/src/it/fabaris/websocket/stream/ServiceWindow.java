package it.fabaris.websocket.stream;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import java.awt.Component;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Window.Type;
import java.awt.Dialog.ModalExclusionType;

public class ServiceWindow extends JDialog {

	private JPanel contentPane;
	private JTextField txtURL;
	private JTextArea txtMessages;
	private int exitCode;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServiceWindow frame = new ServiceWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ServiceWindow() {
		setModal(true);
		setType(Type.POPUP);
		setTitle("Service");
		setBounds(100, 100, 980, 685);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
		
		JPanel panel = new JPanel();
		contentPane.add(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
		
		JLabel lblUrl = new JLabel("URL:");
		panel.add(lblUrl);
		
		txtURL = new JTextField();
		lblUrl.setLabelFor(txtURL);
		txtURL.setText("ws://portal.mscoe.it:6180/arcgis/ws/services/smart_airfield/fuel-tank/StreamServer/broadcast");
		panel.add(txtURL);
		txtURL.setColumns(10);
		txtURL.setMaximumSize(new Dimension(5000, 30));
		
		JLabel lblMessages = new JLabel("Messages:");
		lblMessages.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblMessages.setHorizontalAlignment(SwingConstants.LEFT);
		contentPane.add(lblMessages);
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane);
		
		txtMessages = new JTextArea();
		scrollPane.setViewportView(txtMessages);
		lblMessages.setLabelFor(txtMessages);
		
		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				exitCode = 0;
				ServiceWindow.this.setVisible(false);
			}
		});
		btnOk.setAlignmentX(Component.CENTER_ALIGNMENT);
		contentPane.add(btnOk);
	}

	public JTextField getTxtURL() {
		return txtURL;
	}
	public JTextArea getTxtMessages() {
		return txtMessages;
	}

	public int open() {
		exitCode = -1;
		this.setVisible(true);
		return exitCode;
	}
}
