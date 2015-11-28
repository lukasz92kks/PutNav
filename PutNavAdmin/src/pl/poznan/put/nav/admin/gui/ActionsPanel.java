package pl.poznan.put.nav.admin;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSeparator;

public class ActionsPanel extends JPanel {

	private static final long serialVersionUID = 4199937067539429658L;
	
	private MapPanel mapPanel = AppFactory.getMapPanel();
	private Map activeMap;
	private JButton addBuildingPointButton = null;
	private JButton addNaviPointButton = null;
	private JButton addDoorPointButton = null;
	private JButton addLiftPointButton = null;
	private JButton addStairsPointButton = null;
	private JButton deletePointButton = null;

	public ActionsPanel() {
		this.setLayout(new FlowLayout());
		
		addBuildingPointButton = new JButton(new ImageIcon("images/building.png"));
		addNaviPointButton = new JButton(new ImageIcon("images/navi.png"));
		addDoorPointButton = new JButton(new ImageIcon("images/door.png"));
		addLiftPointButton = new JButton(new ImageIcon("images/lift.png"));
		addStairsPointButton = new JButton(new ImageIcon("images/stairs.png"));
		deletePointButton = new JButton(new ImageIcon("images/delete.png"));
		
		addBuildingPointButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				System.out.println("building");
				mapPanel.setActiveAddMapPointType(MapPointTypes.BUILDING);
			}
		});
		addNaviPointButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				System.out.println("navi");
				mapPanel.setActiveAddMapPointType(MapPointTypes.NAVIGATION);
			}
		});
		addDoorPointButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				System.out.println("door");
				mapPanel.setActiveAddMapPointType(MapPointTypes.DOOR);
			}
		});
		addLiftPointButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				System.out.println("lift");
				mapPanel.setActiveAddMapPointType(MapPointTypes.LIFT);
			}
		});
		addStairsPointButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				System.out.println("stairs");
				mapPanel.setActiveAddMapPointType(MapPointTypes.STAIRS);
			}
		});
		deletePointButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				System.out.println("delete");
				mapPanel.deleteActiveMapPoint();
			}
		});
		
		this.add(addBuildingPointButton);
		this.add(addNaviPointButton);
		this.add(addDoorPointButton);
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
