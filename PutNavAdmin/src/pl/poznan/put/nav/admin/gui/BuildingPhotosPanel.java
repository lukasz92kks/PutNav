package pl.poznan.put.nav.admin.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
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
import pl.poznan.put.nav.admin.entities.Photo;

public class BuildingPhotosPanel extends JPanel {

	private static final long serialVersionUID = -2807147583255286939L;
	
	private Building building;
	private JList<String> photosList;
	
	public BuildingPhotosPanel(Building building) {
		this.building = building;
		
		initPhotosList();
		
		this.add(photosList);
		this.add(createButtonPanel());
	}
	
	private void initPhotosList() {
		photosList = new JList<String>();
		photosList.setPreferredSize(new Dimension(200, 400));
		
		loadPhotosList(photosList, building.getPhotos());
	}
	
	private void loadPhotosList(JList<String> list, List<Photo> photos) {
		DefaultListModel<String> model = new DefaultListModel<String>();
		for(Photo p : photos) {
			model.addElement(p.getFile());
		}
		list.setModel(model);
		list.repaint();
	}
	
	private JPanel createButtonPanel() {
		JButton addButton = new JButton("+");
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		JButton delButton = new JButton("-");
		delButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(5, 1));
		buttonPanel.add(addButton);
		buttonPanel.add(delButton);
		
		return buttonPanel;
	}

}
