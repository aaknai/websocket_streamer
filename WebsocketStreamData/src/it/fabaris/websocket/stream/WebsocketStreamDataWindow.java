package it.fabaris.websocket.stream;

import java.awt.EventQueue;
import java.awt.Point;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

import org.json.JSONObject;


import it.fabaris.websocket.stream.data.ServiceData;
import sun.security.krb5.Config;

import java.awt.Window.Type;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.awt.event.ActionEvent;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class WebsocketStreamDataWindow {

	private JFrame frmDataStreammer;
	private JTable table;
	private ServiceWindow serviceWindow = new ServiceWindow();
	private ServicesTableModel tableModel;
	private JLabel lblTime;
	private JLabel lblStatus;
	private JButton btnStart;
	private boolean started;
	private ScheduledExecutorService timerServie;
	private DataStreamer dataStreamer;
	private DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss.SSS");
	private JSpinner spinner;
	private JButton btnAdd;
	private JButton btnRemove;
	private JButton btnSave;
	private JButton btnLoad;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WebsocketStreamDataWindow window = new WebsocketStreamDataWindow();
					window.frmDataStreammer.setVisible(true);
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
		applyConfig();
	}

	public void applyConfig() {
		tableModel.getData().clear();
		tableModel.getData().addAll(Configuration.getInstance().getConfig().services);
		tableModel.fireTableDataChanged();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmDataStreammer = new JFrame();
		frmDataStreammer.setTitle("Data Streammer");
		frmDataStreammer.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				String ObjButtons[] = {"Yes","No"};
			    int PromptResult = JOptionPane.showOptionDialog(null, 
			        "Are you sure you want to exit?", "Confirm", 
			        JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, 
			        ObjButtons,ObjButtons[1]);
			    if(PromptResult==0)
			    {
			    	if(started)stop();
			    	System.exit(0);          
			    }
			}
		});
		frmDataStreammer.setBounds(100, 100, 598, 399);
		frmDataStreammer.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frmDataStreammer.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel panel_3 = new JPanel();
		frmDataStreammer.getContentPane().add(panel_3, BorderLayout.NORTH);
		panel_3.setLayout(new BoxLayout(panel_3, BoxLayout.Y_AXIS));
		
		JPanel panel = new JPanel();
		panel_3.add(panel);
		
		btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(WebsocketStreamDataWindow.this.getServiceWindow().open() != 0) return;
				String url = serviceWindow.getTxtURL().getText().trim();
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
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.add(btnAdd);
		
		btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Configuration.getInstance().getConfig().services = tableModel.getData();
				Configuration.getInstance().save();
			}
		});
		
		btnRemove = new JButton("Remove");
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				tableModel.getData().remove(table.getSelectedRow());
				tableModel.fireTableDataChanged();
			}
		});
		panel.add(btnRemove);
		
		Component horizontalGlue = Box.createHorizontalGlue();
		panel.add(horizontalGlue);
		
		btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				startStop();
			}
		});
		panel.add(btnStart);
		
		Component horizontalGlue_1 = Box.createHorizontalGlue();
		panel.add(horizontalGlue_1);
		panel.add(btnSave);
		
		btnLoad = new JButton("Load");
		btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Configuration.getInstance().load();
				applyConfig();
			}
		});
		panel.add(btnLoad);
		
		JPanel panel_2 = new JPanel();
		panel_3.add(panel_2);
		panel_2.setLayout(new BoxLayout(panel_2, BoxLayout.X_AXIS));
		
		JLabel lblSpeed = new JLabel("Speed:");
		lblSpeed.setHorizontalAlignment(SwingConstants.LEFT);
		panel_2.add(lblSpeed);
		
		spinner = new JSpinner();
		spinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				Float val = (Float) spinner.getValue();
				if(dataStreamer != null) dataStreamer.setSpeed(val);
			}
		});
		spinner.setModel(new SpinnerNumberModel(new Float(1), new Float(0.01), new Float(10000), new Float(0.1)));
		panel_2.add(spinner);
		
		Component horizontalGlue_3 = Box.createHorizontalGlue();
		panel_2.add(horizontalGlue_3);
		
		JScrollPane scrollPane = new JScrollPane();
		frmDataStreammer.getContentPane().add(scrollPane, BorderLayout.CENTER);

		tableModel = new ServicesTableModel();
		table = new JTable();
		table.setModel(tableModel);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent me) {
				if(me.getSource()!=table)return;
				if(me.getClickCount() != 2) return;
				Point p = me.getPoint();
				int row = table.rowAtPoint(p);
				serviceWindow.getTxtURL().setText(tableModel.getData().get(row).getServiceUrl());
				serviceWindow.getTxtMessages().setText(tableModel.getData().get(row).getMessages().stream().map(d -> d.toString()).collect(Collectors.joining("\n")));
				if(serviceWindow.open() != 0) return;
				String url = serviceWindow.getTxtURL().getText().trim();
				String[] lines = serviceWindow.getTxtMessages().getText().split("\r?\n");
				ArrayList<JSONObject> messages = new ArrayList<>();
				for(String s : lines){
					try{
						JSONObject obj = new JSONObject(s);
						messages.add(obj);
					}catch (Exception ex) {
						System.out.println("Error parsing json:"+ex.getMessage());
					}
				}
				if(messages.size()==0)return;
				tableModel.getData().get(row).setServiceUrl(url);
				tableModel.getData().get(row).setMessages(messages);
				tableModel.fireTableDataChanged();
			}
		});
		scrollPane.setViewportView(table);
		
		JPanel panel_1 = new JPanel();
		frmDataStreammer.getContentPane().add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));
		
		lblStatus = new JLabel("Status:");
		panel_1.add(lblStatus);
		
		Component horizontalGlue_2 = Box.createHorizontalGlue();
		panel_1.add(horizontalGlue_2);
		
		lblTime = new JLabel("");
		panel_1.add(lblTime);
	}

	protected void startStop() {
		if(this.started){
			stop();
		}else{
			start();
		}
	}

	private void start() {
		started = true;
		getBtnStart().setText("Stop");
		this.timerServie = Executors.newSingleThreadScheduledExecutor();
		this.dataStreamer = new DataStreamer(tableModel.getData(), (float) getSpinner().getValue(), this);
		timerServie.scheduleAtFixedRate(dataStreamer,0,100, TimeUnit.MILLISECONDS);
		getBtnAdd().setEnabled(false);
		getBtnRemove().setEnabled(false);
		getBtnLoad().setEnabled(false);
		getBtnSave().setEnabled(false);
	}

	private void stop() {
		started = false;
		getBtnStart().setText("Start");
		timerServie.shutdown();
		dataStreamer.closeEndpoints();
		timerServie = null;
		dataStreamer = null;
		getBtnAdd().setEnabled(true);
		getBtnRemove().setEnabled(true);
		getBtnLoad().setEnabled(true);
		getBtnSave().setEnabled(true);
	}

	public ServiceWindow getServiceWindow() {
		return serviceWindow;
	}

	public synchronized void setTime(LocalDateTime now, float speed) {
		getLblTime().setText(now.format(dateTimeFormat )+" @ "+speed+"x");
	}
	public synchronized void setStatus(String status) {
		getLblStatus().setText("Status: "+status);
	}

	public JLabel getLblTime() {
		return lblTime;
	}
	public JLabel getLblStatus() {
		return lblStatus;
	}
	public JButton getBtnStart() {
		return btnStart;
	}
	public JSpinner getSpinner() {
		return spinner;
	}
	public JButton getBtnAdd() {
		return btnAdd;
	}
	public JButton getBtnRemove() {
		return btnRemove;
	}
	public JButton getBtnSave() {
		return btnSave;
	}
	public JButton getBtnLoad() {
		return btnLoad;
	}
}
