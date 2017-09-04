package it.fabaris.websocket.client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.DefaultCaret;

import org.eclipse.jetty.websocket.api.Session;

import it.fabaris.websocket.client.SimpleWebsocket.WebsocketHandler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import javax.swing.JScrollPane;

public class MainWindow implements WebsocketHandler {

	private JFrame frame;
	private JTextField txtURL;
	private JButton btnDisconnect;
	private JButton btnConnect;
	private JTextArea txtConsole;
	protected SimpleWebsocketEndpoint websocketEndpoint;
	private JScrollPane scrollPane;
	private JTextField txtMessage;
	private JButton btnSend;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
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
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 332);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		txtURL = new JTextField();
		txtURL.setText("ws://portal.mscoe.it:6180/arcgis/ws/services/smart_airfield/fluel-tank-stream/StreamServer/broadcast");
		txtURL.setBounds(10, 12, 216, 20);
		frame.getContentPane().add(txtURL);
		txtURL.setColumns(10);
		
		btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					MainWindow.this.websocketEndpoint = new SimpleWebsocketEndpoint(new URI(MainWindow.this.getTxtURL().getText()))
							.addWebsocketHandler(MainWindow.this)
							.connect();
					MainWindow.this.getBtnDisconnect().setEnabled(true);
					MainWindow.this.getBtnConnect().setEnabled(false);
					MainWindow.this.getTxtURL().setEnabled(false);
				} catch (Exception e) {
					MainWindow.this.txtConsole.append("Error Connecting:"+e.getMessage()+"\n");
					e.printStackTrace();
				}
			}
		});
		btnConnect.setBounds(236, 11, 89, 23);
		frame.getContentPane().add(btnConnect);
		
		btnDisconnect = new JButton("Disconnect");
		btnDisconnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					MainWindow.this.getBtnDisconnect().setEnabled(false);
					MainWindow.this.getBtnConnect().setEnabled(true);
					MainWindow.this.getTxtURL().setEnabled(true);
					MainWindow.this.websocketEndpoint.disconnect();
					MainWindow.this.websocketEndpoint = null;
				} catch (Exception ex) {
					MainWindow.this.txtConsole.append("Error Disconnecting:"+ex.getMessage()+"\n");
					ex.printStackTrace();
				}
			}
		});
		btnDisconnect.setEnabled(false);
		btnDisconnect.setBounds(335, 11, 89, 23);
		frame.getContentPane().add(btnDisconnect);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 45, 414, 206);
		frame.getContentPane().add(scrollPane);
		
		txtConsole = new JTextArea();
		scrollPane.setViewportView(txtConsole);
		txtConsole.setLineWrap(true);
		txtConsole.setEditable(false);
		
		txtMessage = new JTextField();
		txtMessage.setText("{\"attributes\":{ \"tank_id\":\"tank n.1\", \"fluel_capacity\":200, \"fluel_level\":60}}");
		txtMessage.setBounds(10, 262, 315, 20);
		frame.getContentPane().add(txtMessage);
		txtMessage.setColumns(10);
		
		btnSend = new JButton("Send");
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainWindow.this.websocketEndpoint.sendMessage(MainWindow.this.txtMessage.getText());
			}
		});
		btnSend.setBounds(335, 262, 89, 23);
		frame.getContentPane().add(btnSend);
		((DefaultCaret)txtConsole.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
	}
	public JButton getBtnDisconnect() {
		return btnDisconnect;
	}
	public JButton getBtnConnect() {
		return btnConnect;
	}
	public JTextField getTxtURL() {
		return txtURL;
	}
	public JTextArea getTxtConsole() {
		return txtConsole;
	}

	@Override
	public void onConnect(Session session) {
		this.txtConsole.append("Connected:"+session+"\n");
	}

	@Override
	public void onClose(int statusCode, String reason) {
		this.txtConsole.append("Disconnected:"+statusCode+" - "+reason+"\n");
		
	}

	@Override
	public void onMessageSent(String msg) {
		this.txtConsole.append("Sent:"+msg+"\n");
	}

	@Override
	public void onMessageReceive(String msg) {
		this.txtConsole.append("Received:"+msg+"\n");
		
	}
}
