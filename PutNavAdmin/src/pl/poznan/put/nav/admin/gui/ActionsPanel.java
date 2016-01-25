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
	private JButton deletePointsConnectionButton = null;
	private JButton addFloorsConnectionButton = null;
	private JButton deleteFloorsConnectionButton = null;
	private JButton addBuildingPointButton = null;
	private JButton addNaviPointButton = null;
	private JButton addDoorPointButton = null;
	private JButton addLiftPointButton = null;
	private JButton addStairsPointButton = null;
	private JButton addOutdoorPointButton = null;
	private JButton addRoomPointButton = null;
	private JButton deletePointButton = null;

	public ActionsPanel() {
		System.out.println("ActionsPanel");
		this.setLayout(new FlowLayout());
		
		addPointsConnectionButton = new JButton(new ImageIcon("images/arrow.png"));
		deletePointsConnectionButton = new JButton(new ImageIcon("images/delete-arrow.png"));
		addFloorsConnectionButton = new JButton(new ImageIcon("images/arrowfloor.png"));
		deleteFloorsConnectionButton = new JButton(new ImageIcon("images/delete-arrowfloor.png"));
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
				mapPanel.setMode(MapPanelModes.EDIT_POINTS_CONNECTIONS);
				mapPanel.clearStartArc();
				mapPanel.refresh();
			}
		});
		deletePointsConnectionButton.addActionListener(new ActionListener() {
					
			@Override
			public void actionPerformed(ActionEvent event) {
				mapPanel.setMode(MapPanelModes.REMOVE_POINTS_CONNECTIONS);
				mapPanel.clearStartArc();
				mapPanel.refresh();
			}
		});
		addFloorsConnectionButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				mapPanel.setMode(MapPanelModes.EDIT_FLOORS_CONNECTIONS);
				mapPanel.clearStartArc();
				mapPanel.refresh();
			}
		});
		deleteFloorsConnectionButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				mapPanel.setMode(MapPanelModes.REMOVE_FLOORS_CONNECTIONS);
				mapPanel.clearStartArc();
				mapPanel.refresh();
			}
		});
		addBuildingPointButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				mapPanel.setMode(MapPanelModes.EDIT_POINTS);
				mapPanel.setActiveAddMapPointType(MapPointTypes.BUILDING);
			}
		});
		addRoomPointButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				mapPanel.setMode(MapPanelModes.EDIT_POINTS);
				mapPanel.setActiveAddMapPointType(MapPointTypes.ROOM);
				mapPanel.clearStartArc();
				mapPanel.refresh();
			}
		});
		addNaviPointButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				mapPanel.setMode(MapPanelModes.EDIT_POINTS);
				mapPanel.setActiveAddMapPointType(MapPointTypes.NAVIGATION);
				mapPanel.clearStartArc();
				mapPanel.refresh();
			}
		});
		addDoorPointButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				mapPanel.setMode(MapPanelModes.EDIT_POINTS);
				mapPanel.setActiveAddMapPointType(MapPointTypes.DOOR);
				mapPanel.clearStartArc();
				mapPanel.refresh();
			}
		});
		addOutdoorPointButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				mapPanel.setMode(MapPanelModes.EDIT_POINTS);
				mapPanel.setActiveAddMapPointType(MapPointTypes.OUTDOOR);
				mapPanel.clearStartArc();
				mapPanel.refresh();
			}
		});
		addLiftPointButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				mapPanel.setMode(MapPanelModes.EDIT_POINTS);
				mapPanel.setActiveAddMapPointType(MapPointTypes.LIFT);
				mapPanel.clearStartArc();
				mapPanel.refresh();
			}
		});
		addStairsPointButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				mapPanel.setMode(MapPanelModes.EDIT_POINTS);
				mapPanel.setActiveAddMapPointType(MapPointTypes.STAIRS);
				mapPanel.clearStartArc();
				mapPanel.refresh();
			}
		});
		deletePointButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				mapPanel.setMode(MapPanelModes.EDIT_POINTS);
				mapPanel.deleteActiveMapPoint();
				mapPanel.clearStartArc();
				mapPanel.refresh();
			}
		});
		
		this.add(addPointsConnectionButton);
		this.add(deletePointsConnectionButton);
		this.add(addFloorsConnectionButton);
		this.add(deleteFloorsConnectionButton);
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
		if(activeMap.isCampus()) {
			addBuildingPointButton.setEnabled(true);
			addFloorsConnectionButton.setEnabled(false);
			deleteFloorsConnectionButton.setEnabled(false);
			addDoorPointButton.setEnabled(false);
			addLiftPointButton.setEnabled(false);
			addStairsPointButton.setEnabled(false);
			addOutdoorPointButton.setEnabled(false);
			addRoomPointButton.setEnabled(false);
		} else {
			addBuildingPointButton.setEnabled(false);
			addFloorsConnectionButton.setEnabled(true);
			deleteFloorsConnectionButton.setEnabled(true);
			addDoorPointButton.setEnabled(true);
			addLiftPointButton.setEnabled(true);
			addStairsPointButton.setEnabled(true);
			addOutdoorPointButton.setEnabled(true);
			addRoomPointButton.setEnabled(true);
		}
	}
}
