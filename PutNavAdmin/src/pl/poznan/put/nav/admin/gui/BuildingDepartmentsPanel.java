package pl.poznan.put.nav.admin.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;

import pl.poznan.put.nav.admin.entities.Building;
import pl.poznan.put.nav.admin.entities.Department;
import pl.poznan.put.nav.admin.managers.AppFactory;
import pl.poznan.put.nav.admin.managers.DatabaseManager;

public class BuildingDepartmentsPanel extends JPanel {
	
	private static final long serialVersionUID = -5003915529116868854L;
	
	private Building building = null;
	private List<Department> departments;

	private JList<String> includedDepartmentsList;
	private JList<String> allDepartmentsList;

	public BuildingDepartmentsPanel(Building building, List<Department> allDepartments) {
		this.building = building;
		this.departments = allDepartments;
		
		initIncludedDepartmentsList();
		initAllDepartmentsList();
		
		this.add(includedDepartmentsList);
		this.add(createButtonsPanel());
		this.add(allDepartmentsList);
		
	}
	
	private void initIncludedDepartmentsList() {
		includedDepartmentsList = new JList<String>();
		includedDepartmentsList.setPreferredSize(new Dimension(200, 400));
		
		loadDepartmentList(includedDepartmentsList, building.getDepartments());
	}
	
	private void initAllDepartmentsList() {
		allDepartmentsList = new JList<String>();
		allDepartmentsList.setPreferredSize(new Dimension(200, 400));
		
		loadDepartmentList(allDepartmentsList, departments);
	}
	
	private void loadDepartmentList(JList<String> list, List<Department> departments) {
		DefaultListModel<String> model = new DefaultListModel<String>();
		for(Department d : departments) {
			model.addElement(d.getName());
		}
		list.setModel(model);
		list.repaint();
	}
	
	private JPanel createButtonsPanel() {
		JButton addButton = new JButton("<<");
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				String selectedDepartment = allDepartmentsList.getSelectedValue();
				for(Department d : departments) {
					if(d.getName().equals(selectedDepartment)) {
						if(!building.getDepartments().contains(d)) {
							building.addDepartment(d);
							d.addBuilding(building);
							loadDepartmentList(includedDepartmentsList, building.getDepartments());
						}
					}
				}
			}
		});
		JButton delButton = new JButton(">>");
		delButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				String selectedDepartment = includedDepartmentsList.getSelectedValue();
				for(Department d : building.getDepartments()) {
					if(d.getName().equals(selectedDepartment)) {
						building.removeDepartment(d);
						d.removeBuilding(building);
						loadDepartmentList(includedDepartmentsList, building.getDepartments());
					}
				}
			}
		});
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		buttonPanel.add(addButton);
		buttonPanel.add(delButton);
		
		return buttonPanel;
	}
	
	public Building getBuilding() {
		return building;
	}

	public void setBuilding(Building building) {
		this.building = building;
	}

	public List<Department> getDepartments() {
		return departments;
	}

	public void setDepartments(List<Department> departments) {
		this.departments = departments;
	}
}
