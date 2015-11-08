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
	
	MapPanel mapPanel = AppFactory.getMapPanel();

	public ActionsPanel() {
		this.setLayout(new FlowLayout());
		
		JButton addNaviPointButton = new JButton(new ImageIcon("images/navi-action.png"));
		JButton addDoorPointButton = new JButton(new ImageIcon("images/door-action.png"));
		JButton addLiftPointButton = new JButton(new ImageIcon("images/lift-action.png"));
		JButton addStairsPointButton = new JButton(new ImageIcon("images/stairs-action.png"));
		JButton deletePointButton = new JButton(new ImageIcon("images/delete-action.png"));
		
		addNaviPointButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				System.out.println("navi");
				mapPanel.setActiveMapPointType(MapPointTypes.NAVIGATION);
			}
		});
		addDoorPointButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				System.out.println("door");
				mapPanel.setActiveMapPointType(MapPointTypes.DOOR);
			}
		});
		addLiftPointButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				System.out.println("lift");
				mapPanel.setActiveMapPointType(MapPointTypes.LIFT);
			}
		});
		addStairsPointButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				System.out.println("stairs");
				mapPanel.setActiveMapPointType(MapPointTypes.STAIRS);
			}
		});
		deletePointButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				System.out.println("delete");
			}
		});
		
		this.add(addNaviPointButton);
		this.add(addDoorPointButton);
		this.add(addLiftPointButton);
		this.add(addStairsPointButton);
		this.add(new JSeparator());
		this.add(deletePointButton);
	}
}
