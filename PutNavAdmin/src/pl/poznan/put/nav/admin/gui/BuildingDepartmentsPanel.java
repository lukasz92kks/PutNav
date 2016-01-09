package pl.poznan.put.nav.admin.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
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
	
	private List<String> includedDepartmentNames;
	private List<String> allDepartmentNames;

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
		
		includedDepartmentNames = new ArrayList<String>();
		for(Department d : building.getDepartments()) {
			includedDepartmentNames.add(d.getName());
		}
		
		loadDepartmentList(includedDepartmentsList, includedDepartmentNames);
	}
	
	private void initAllDepartmentsList() {
		allDepartmentsList = new JList<String>();
		allDepartmentsList.setPreferredSize(new Dimension(200, 400));
		
		allDepartmentNames = new ArrayList<String>();
		for(Department d : departments) {
			if(!building.getDepartments().contains(d))
				allDepartmentNames.add(d.getName());
		}
		
		loadDepartmentList(allDepartmentsList, allDepartmentNames);
	}
	
	private void loadDepartmentList(JList<String> list, List<String> departments) {
		DefaultListModel<String> model = new DefaultListModel<String>();
		for(String name : departments) {
			model.addElement(name);
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
				if(allDepartmentNames.size() > 0 && !listContainsString(includedDepartmentsList, selectedDepartment)) {
					includedDepartmentNames.add(selectedDepartment);
					removeStringFromList(allDepartmentNames, selectedDepartment);
					loadDepartmentList(allDepartmentsList, allDepartmentNames);
					loadDepartmentList(includedDepartmentsList, includedDepartmentNames);
				}
			}
		});
		JButton delButton = new JButton(">>");
		delButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				String selectedDepartment = includedDepartmentsList.getSelectedValue();
				if(includedDepartmentNames.size() > 0 && !listContainsString(allDepartmentsList, selectedDepartment)) {
					allDepartmentNames.add(selectedDepartment);
					removeStringFromList(includedDepartmentNames, selectedDepartment);
					loadDepartmentList(allDepartmentsList, allDepartmentNames);
					loadDepartmentList(includedDepartmentsList, includedDepartmentNames);
				}
			}
		});
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		buttonPanel.add(addButton);
		buttonPanel.add(delButton);
		
		return buttonPanel;
	}
	
	private boolean listContainsString(JList<String> list, String str) {
		if(list != null && str != null)
			for(int i=0; i<list.getModel().getSize(); ++i) {
				if(list.getModel().getElementAt(i).equals(str))
					return true;
			}
		return false;
	}
	
	private void removeStringFromList(List<String> list, String str) {
		if(list != null && str != null) {
			Iterator<String> i = list.iterator();
			while (i.hasNext()) {
				String s = i.next();
				if(s != null) {
				if(s.equals(str))
					i.remove();
				}
			}
		}
	}
	
	public ArrayList<Department> getIncludedDepartments() {
		ArrayList<Department> includedDepartments = new ArrayList<Department>();
		
		for(int i=0; i<includedDepartmentsList.getModel().getSize(); ++i) {
			String name = includedDepartmentsList.getModel().getElementAt(i);
			for(Department d : departments) {
				if(d.getName().equals(name))
					includedDepartments.add(d);
			}
		}
		
		return includedDepartments;
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
