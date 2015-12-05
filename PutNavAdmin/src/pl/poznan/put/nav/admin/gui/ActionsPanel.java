package pl.poznan.put.nav.admin.gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import pl.poznan.put.nav.admin.entities.Map;
import pl.poznan.put.nav.admin.entities.MapPointTypes;
import pl.poznan.put.nav.admin.managers.AppFactory;

public class ActionsPanel extends JPanel {

	private static final long serialVersionUID = 4199937067539429658L;
	
	private MapPanel mapPanel = AppFactory.getMapPanel();
	private Map activeMap;
	private JButton addPointsConnectionButton = null;
	private JButton addBuildingPointButton = null;
	private JButton addNaviPointButton = null;
	private JButton addDoorPointButton = null;
	private JButton addLiftPointButton = null;
	private JButton addStairsPointButton = null;
	private JButton addOutdoorPointButton = null;
	private JButton addRoomPointButton = null;
	private JButton deletePointButton = null;

	public ActionsPanel() {
		this.setLayout(new FlowLayout());
		
		addPointsConnectionButton = new JButton(new ImageIcon("images/arrow.png"));
		addBuildingPointButton = new JButton(new ImageIcon("images/building.png"));
		addNaviPointButton = new JButton(new ImageIcon("images/navi.png"));
		addDoorPointButton = new JButton(new ImageIcon("images/door.png"));
		addLiftPointButton = new JButton(new ImageIcon("images/lift.png"));
		addStairsPointButton = new JButton(new ImageIcon("images/stairs.png"));
		addOutdoorPointButton = new JButton(new ImageIcon("images/outdoor.png"));
		addRoomPointButton = new JButton(new ImageIcon("images/room.png"));
		deletePointButton = new JButton(new ImageIcon("images/delete.png"));
		
		addPointsConnectionButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				System.out.println("connection");
				mapPanel.setMode(MapPanelModes.EDIT_POINTS_CONNECTIONS);
			}
		});
		addBuildingPointButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				System.out.println("building");
				mapPanel.setMode(MapPanelModes.EDIT_POINTS);
				mapPanel.setActiveAddMapPointType(MapPointTypes.BUILDING);
			}
		});
		addRoomPointButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				System.out.println("room");
				mapPanel.setMode(MapPanelModes.EDIT_POINTS);
				mapPanel.setActiveAddMapPointType(MapPointTypes.ROOM);
			}
		});
		addNaviPointButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				System.out.println("navi");
				mapPanel.setMode(MapPanelModes.EDIT_POINTS);
				mapPanel.setActiveAddMapPointType(MapPointTypes.NAVIGATION);
			}
		});
		addDoorPointButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				System.out.println("door");
				mapPanel.setMode(MapPanelModes.EDIT_POINTS);
				mapPanel.setActiveAddMapPointType(MapPointTypes.DOOR);
			}
		});
		addOutdoorPointButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				System.out.println("outdoor");
				mapPanel.setMode(MapPanelModes.EDIT_POINTS);
				mapPanel.setActiveAddMapPointType(MapPointTypes.OUTDOOR);
			}
		});
		addLiftPointButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				System.out.println("lift");
				mapPanel.setMode(MapPanelModes.EDIT_POINTS);
				mapPanel.setActiveAddMapPointType(MapPointTypes.LIFT);
			}
		});
		addStairsPointButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				System.out.println("stairs");
				mapPanel.setMode(MapPanelModes.EDIT_POINTS);
				mapPanel.setActiveAddMapPointType(MapPointTypes.STAIRS);
			}
		});
		deletePointButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				System.out.println("delete");
				mapPanel.setMode(MapPanelModes.EDIT_POINTS);
				mapPanel.deleteActiveMapPoint();
			}
		});
		
		this.add(addPointsConnectionButton);
		this.add(new JSeparator());
		this.add(addBuildingPointButton);
		this.add(addRoomPointButton);
		this.add(addNaviPointButton);
		this.add(addDoorPointButton);
		this.add(addOutdoorPointButton);
		this.add(addLiftPointButton);
		this.add(addStairsPointButton);
		this.add(new JSeparator());
		this.add(deletePointButton);
	}
	
	public Map getActiveMap() {
		return activeMap;
	}

	public void setActiveMap(Map activeMap) {
		this.activeMap = activeMap;
		if(activeMap.getMapFile().toString().equals("images/building.png")) {
			addBuildingPointButton.setEnabled(true);
			addDoorPointButton.setEnabled(false);
			addLiftPointButton.setEnabled(false);
			addStairsPointButton.setEnabled(false);
		} else {
			addBuildingPointButton.setEnabled(false);
			addDoorPointButton.setEnabled(true);
			addLiftPointButton.setEnabled(true);
			addStairsPointButton.setEnabled(true);
		}
	}
}
