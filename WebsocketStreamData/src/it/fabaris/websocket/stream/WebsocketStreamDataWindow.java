package it.fabaris.websocket.stream.data;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.Window.Type;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.awt.event.ActionEvent;

public class WebsocketStreamDataWindow {

	private JFrame frame;
	private JTable table;
	private ServiceWindow serviceWindow = new ServiceWindow();
	private ServicesTableModel tableModel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WebsocketStreamDataWindow window = new WebsocketStreamDataWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public WebsocketStreamDataWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 598, 399);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.NORTH);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				WebsocketStreamDataWindow.this.getServiceWindow().setVisible(true);
				tableModel.addRow(new ServiceData(serviceWindow.getTxtURL().getText(), Arrays.asList(serviceWindow.getTxtMessages().getText().split("[^\r\n]+"))));
				frame.repaint();
			}
		});
		panel.add(btnAdd);
		
		JScrollPane scrollPane = new JScrollPane();
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);

		tableModel = new ServicesTableModel();
		table = new JTable();
		table.setModel(tableModel);
		scrollPane.setViewportView(table);
	}

	public ServiceWindow getServiceWindow() {
		return serviceWindow;
	}

}
