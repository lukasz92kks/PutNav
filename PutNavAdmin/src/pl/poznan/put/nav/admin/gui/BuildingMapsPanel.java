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
import pl.poznan.put.nav.admin.entities.Map;

public class BuildingMapsPanel extends JPanel {

	private static final long serialVersionUID = -3973709613817976102L;

	private Building building = null;
	private List<Map> maps;

	private JList<String> includedMapsList;
	private JList<String> allFreeMapsList;
	
	private List<String> includedMapsNames;
	private List<String> allFreeMapsNames;

	public BuildingMapsPanel(Building building, List<Map> allMaps) {
		this.building = building;
		this.maps = allMaps;
		
		initIncludedMapsList();
		initAllFreeMapsList();
		
		this.add(includedMapsList);
		this.add(createButtonsPanel());
		this.add(allFreeMapsList);
		
	}
	
	private void initIncludedMapsList() {
		includedMapsList = new JList<String>();
		includedMapsList.setPreferredSize(new Dimension(200, 400));
		
		includedMapsNames = new ArrayList<String>();
		for(Map map : building.getMaps()) {
			includedMapsNames.add(map.getMapFile());
		}
		
		loadDepartmentList(includedMapsList, includedMapsNames);
	}
	
	private void initAllFreeMapsList() {
		allFreeMapsList = new JList<String>();
		allFreeMapsList.setPreferredSize(new Dimension(200, 400));
		
		allFreeMapsNames = new ArrayList<String>();
		for(Map map : maps) {
			if(map.getBuilding() == null)
				allFreeMapsNames.add(map.getMapFile());
		}
		
		loadDepartmentList(allFreeMapsList, allFreeMapsNames);
	}
	
	private void loadDepartmentList(JList<String> list, List<String> maps) {
		DefaultListModel<String> model = new DefaultListModel<String>();
		for(String file : maps) {
			model.addElement(file);
		}
		list.setModel(model);
		list.repaint();
	}
	
	private JPanel createButtonsPanel() {
		JButton addButton = new JButton("<<");
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				String selectedDepartment = allFreeMapsList.getSelectedValue();
				if(allFreeMapsNames.size() > 0 && !listContainsString(includedMapsList, selectedDepartment)) {
					includedMapsNames.add(selectedDepartment);
					removeStringFromList(allFreeMapsNames, selectedDepartment);
					loadDepartmentList(allFreeMapsList, allFreeMapsNames);
					loadDepartmentList(includedMapsList, includedMapsNames);
				}
			}
		});
		JButton delButton = new JButton(">>");
		delButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				String selectedDepartment = includedMapsList.getSelectedValue();
				if(includedMapsNames.size() > 0 && !listContainsString(allFreeMapsList, selectedDepartment)) {
					allFreeMapsNames.add(selectedDepartment);
					removeStringFromList(includedMapsNames, selectedDepartment);
					loadDepartmentList(allFreeMapsList, allFreeMapsNames);
					loadDepartmentList(includedMapsList, includedMapsNames);
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
		System.out.println("   " + str);
		if(list != null && str != null) {
			System.out.println("if");
			for(int i=0; i<list.getModel().getSize(); ++i) {
				System.out.println("1");
				if(list.getModel().getElementAt(i).equals(str))
					return true;
				System.out.println("2");
			}
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
	
	public ArrayList<Map> getIncludedMaps() {
		ArrayList<Map> includedMaps = new ArrayList<Map>();
		
		for(int i=0; i<includedMapsList.getModel().getSize(); ++i) {
			String mapFile = includedMapsList.getModel().getElementAt(i);
			for(Map map : maps) {
				if(map.getMapFile().equals(mapFile)) {
					map.setBuilding(building);
					includedMaps.add(map);
					System.out.println(map.getMapFile());
				}
			}
		}
		System.out.println();
		return includedMaps;
	}
}