package pl.poznan.put.nav.admin.gui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import pl.poznan.put.nav.admin.Main;
import pl.poznan.put.nav.admin.entities.Map;
import pl.poznan.put.nav.admin.entities.MapPointTypes;
import pl.poznan.put.nav.admin.managers.AppFactory;
import pl.poznan.put.nav.admin.managers.EntitiesManager;

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
		
		addPointsConnectionButton = new JButton(new ImageIcon(Main.class.getResource("/resources/arrow.png")));
		deletePointsConnectionButton = new JButton(new ImageIcon(Main.class.getResource("/resources/delete-arrow.png")));
		addFloorsConnectionButton = new JButton(new ImageIcon(Main.class.getResource("/resources/arrowfloor.png")));
		deleteFloorsConnectionButton = new JButton(new ImageIcon(Main.class.getResource("/resources/delete-arrowfloor.png")));
		addBuildingPointButton = new JButton(new ImageIcon(Main.class.getResource("/resources/building.png")));
		addNaviPointButton = new JButton(new ImageIcon(Main.class.getResource("/resources/navi.png")));
		addDoorPointButton = new JButton(new ImageIcon(Main.class.getResource("/resources/door.png")));
		addLiftPointButton = new JButton(new ImageIcon(Main.class.getResource("/resources/lift.png")));
		addStairsPointButton = new JButton(new ImageIcon(Main.class.getResource("/resources/stairs.png")));
		addOutdoorPointButton = new JButton(new ImageIcon(Main.class.getResource("/resources/outdoor.png")));
		addRoomPointButton = new JButton(new ImageIcon(Main.class.getResource("/resources/room.png")));
		deletePointButton = new JButton(new ImageIcon(Main.class.getResource("/resources/delete.png")));
		
		addPointsConnectionButton.setToolTipText("Po��cz punkty");
		deletePointsConnectionButton.setToolTipText("Usu� po��czenie punkt�w");
		addFloorsConnectionButton.setToolTipText("Po��cz pi�tra");
		deleteFloorsConnectionButton.setToolTipText("Usu� po��czenie pi�ter");
		addBuildingPointButton.setToolTipText("Dodaj budynek");
		addNaviPointButton.setToolTipText("Dodaj punkt nawigacyjny");
		addDoorPointButton.setToolTipText("Dodaj drzwi wewn�trzne");
		addLiftPointButton.setToolTipText("Dodaj wind�");
		addStairsPointButton.setToolTipText("Dodaj schody");
		addOutdoorPointButton.setToolTipText("Dodaj drzwi zewn�trzne");
		addRoomPointButton.setToolTipText("Dodaj pomieszczenie");
		deletePointButton.setToolTipText("Usu� punkt");
		
		addActionListener(addPointsConnectionButton, MapPanelModes.EDIT_POINTS_CONNECTIONS, -1);
		addActionListener(deletePointsConnectionButton, MapPanelModes.REMOVE_POINTS_CONNECTIONS, -1);
		addActionListener(addFloorsConnectionButton, MapPanelModes.EDIT_FLOORS_CONNECTIONS, -1);
		addActionListener(deleteFloorsConnectionButton, MapPanelModes.REMOVE_FLOORS_CONNECTIONS, -1);
		addActionListener(addBuildingPointButton, MapPanelModes.EDIT_POINTS, MapPointTypes.BUILDING);
		addActionListener(addRoomPointButton, MapPanelModes.EDIT_POINTS, MapPointTypes.ROOM);
		addActionListener(addNaviPointButton, MapPanelModes.EDIT_POINTS, MapPointTypes.NAVIGATION);
		addActionListener(addDoorPointButton, MapPanelModes.EDIT_POINTS, MapPointTypes.DOOR);
		addActionListener(addOutdoorPointButton, MapPanelModes.EDIT_POINTS, MapPointTypes.OUTDOOR);
		addActionListener(addLiftPointButton, MapPanelModes.EDIT_POINTS, MapPointTypes.LIFT);
		addActionListener(addStairsPointButton, MapPanelModes.EDIT_POINTS, MapPointTypes.STAIRS);
		
		deletePointButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				EntitiesManager em = AppFactory.getEntitiesManager();
				if(em.getActiveMap() != null) {
					mapPanel.setMode(MapPanelModes.EDIT_POINTS);
					mapPanel.deleteActiveMapPoint();
					mapPanel.clearStartArc();
					mapPanel.refresh();
				}
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
	
	public void addActionListener(final JButton button, final int mode, final int type) {
		
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				EntitiesManager em = AppFactory.getEntitiesManager();
				if(em.getActiveMap() != null) {
					mapPanel.setMode(mode);
					if(type >= 0)
						mapPanel.setActiveAddMapPointType(type);
					mapPanel.clearStartArc();
					mapPanel.refresh();
					button.getModel().setPressed(true);
				}
			}
		});
	}
	
	public Map getActiveMap() {
		return activeMap;
	}

	public void setActiveMap(Map activeMap) {
		this.activeMap = activeMap;
		
		if(activeMap != null)
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
