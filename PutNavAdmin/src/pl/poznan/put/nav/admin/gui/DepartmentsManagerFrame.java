package pl.poznan.put.nav.admin.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import pl.poznan.put.nav.admin.entities.Department;
import pl.poznan.put.nav.admin.managers.AppFactory;
import pl.poznan.put.nav.admin.managers.EntitiesManager;

public class DepartmentsManagerFrame extends JFrame {

	private static final long serialVersionUID = -778067423423476974L;

	private JTable table;
	private EntitiesManager em = AppFactory.getEntitiesManager();
	private List<Department> departments;
	
	public DepartmentsManagerFrame() {
		this.setSize(600, 400);
		this.setLocation(100, 20);
		
		table = new JTable(3, 2);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		loadData();
		JScrollPane scrollPane = new JScrollPane(table);
		
		this.add(scrollPane, BorderLayout.CENTER);
		this.add(createButtonPanel(), BorderLayout.SOUTH);
	}
	
	private JPanel createButtonPanel() {
		JButton addButton = new JButton("Dodaj");
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String result = JOptionPane.showInputDialog(null, "Nazwa wydzialu:");
				System.out.println(result);
				if(result != null && !result.isEmpty()) {
					Department department = new Department();
					department.setName(result);
					departments.add(department);
					loadData();
				}
			}
		});
		JButton delButton = new JButton("Usun");
		delButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(table.getSelectedRow() >= 0) {
					Department department = departments.remove(table.getSelectedRow());
					loadData();
					em.addDepartmentToRemove(department);
				}
			}
		});
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(addButton);
		buttonPanel.add(delButton);
		
		return buttonPanel;
	}
	
	private void loadData() {
		departments = em.getDepartments();
		TableModel model = new AbstractTableModel() {

			private static final long serialVersionUID = -3387204221783015479L;
			private String[] columnNames = { "Id", "Nazwa" };
			
			@Override
			public Object getValueAt(int row, int col) {
				if(col == 0)
					return departments.get(row).getId();
				if(col == 1)
					return departments.get(row).getName();
				return null;
			}
			
			@Override
			public int getRowCount() {
				return departments.size();
			}
			
			@Override
			public int getColumnCount() {
				return columnNames.length;
			}
			
			@Override
			public boolean isCellEditable(int row, int col) { 
			    return false; 
			}
			
			@Override
			public String getColumnName(int col) {
		        return columnNames[col];
		    }
		};
		table.setModel(model);
	}
}
