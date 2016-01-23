package pl.poznan.put.nav.admin.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import pl.poznan.put.nav.admin.entities.Building;
import pl.poznan.put.nav.admin.managers.AppFactory;
import pl.poznan.put.nav.admin.managers.EntitiesManager;

public class BuildingsManagerFrame extends JFrame {

	private static final long serialVersionUID = -4737355401402057723L;
	
	private JTable table;
	private EntitiesManager em = AppFactory.getEntitiesManager();
	private List<Building> buildings;

	public BuildingsManagerFrame() {
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
		JButton delButton = new JButton("Usun");
		delButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(table.getSelectedRow() >= 0) {
					Building building = buildings.remove(table.getSelectedRow());
					loadData();
					em.addBuildingToRemove(building);
				}
			}
		});
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(delButton);
		
		return buttonPanel;
	}
	
	private void loadData() {
		buildings = em.getBuildings();
		TableModel model = new AbstractTableModel() {
			
			private static final long serialVersionUID = 9028832463793284903L;
			private String[] columnNames = { "Id", "Nazwa", "Adres" };
			
			@Override
			public Object getValueAt(int row, int col) {
				if(col == 0)
					return buildings.get(row). getId();
				if(col == 1)
					return buildings.get(row).getName();
				if(col == 2)
					return buildings.get(row).getAddress();
				return null;
			}
			
			@Override
			public int getRowCount() {
				return buildings.size();
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
