package it.fabaris.websocket.stream.data;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

public class ServicesTableModel extends DefaultTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8852514242099812291L;
	private ArrayList<ServiceData> serviceData = new ArrayList<>();
	@Override
	public String getColumnName(int column) {
		switch (column) {
		case 0:
			return "Service";
		default:
			return "Messages";
		}
	}
	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public int getRowCount() {
		return serviceData != null ? serviceData.size() : 0;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		ServiceData data = this.serviceData.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return data.getServiceUrl();
		case 1:
			return data.getMessages().size()+" messages";
		default:
			return null;
		}
	}
	public ArrayList<ServiceData> getData() {
		return serviceData;
	}
	public void addRow(ServiceData data) {
		serviceData.add(data);
		fireTableDataChanged();
	}
	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}
	

}
