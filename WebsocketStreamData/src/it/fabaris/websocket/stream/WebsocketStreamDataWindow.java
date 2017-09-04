package it.fabaris.websocket.stream;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.json.JSONObject;

import it.fabaris.websocket.stream.data.ServiceData;
import sun.security.krb5.Config;

import java.awt.Window.Type;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
				if(WebsocketStreamDataWindow.this.getServiceWindow().open() != 0) return;
				String url = serviceWindow.getTxtURL().getText();
				List<JSONObject> messages = new ArrayList<>();
				String[] lines = serviceWindow.getTxtMessages().getText().split("\r?\n");
				for(String s : lines){
					try{
						JSONObject obj = new JSONObject(s);
						messages.add(obj);
					}catch (Exception ex) {
						System.out.println("Error parsing json:"+ex.getMessage());
					}
				}
				if(messages.size()==0)return;
				tableModel.addRow(new ServiceData(url, messages));
			}
		});
		panel.add(btnAdd);
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Configuration.getInstance().getConfig().services = tableModel.getData();
				Configuration.getInstance().save();
			}
		});
		panel.add(btnSave);
		
		JButton btnLoad = new JButton("Load");
		btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Configuration.getInstance().load();
				tableModel.getData().clear();
				tableModel.getData().addAll(Configuration.getInstance().getConfig().services);
				tableModel.fireTableDataChanged();
			}
		});
		panel.add(btnLoad);
		
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
