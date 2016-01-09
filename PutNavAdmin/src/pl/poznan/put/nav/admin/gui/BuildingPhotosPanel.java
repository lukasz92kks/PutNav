package pl.poznan.put.nav.admin.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import pl.poznan.put.nav.admin.entities.Building;
import pl.poznan.put.nav.admin.entities.Department;
import pl.poznan.put.nav.admin.entities.Photo;

public class BuildingPhotosPanel extends JPanel {

	private static final long serialVersionUID = -2807147583255286939L;
	
	private Building building;
	private JList<String> photosList;
	private List<String> photosFiles;
	
	public BuildingPhotosPanel(Building building) {
		this.building = building;
		
		initPhotosList();
		this.add(createButtonPanel());
	}
	
	private void initPhotosList() {
		photosList = new JList<String>();
		JScrollPane scrollPane = new JScrollPane(photosList);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.add(scrollPane);
		
		photosFiles = new ArrayList<String>();
		for(Photo p : building.getPhotos()) {
			photosFiles.add(p.getFile());
		}
		
		loadPhotosList(photosList, photosFiles);
	}
	
	private void loadPhotosList(JList<String> list, List<String> photos) {
		DefaultListModel<String> model = new DefaultListModel<String>();
		for(String p : photos) {
			model.addElement(p);
		}
		list.setModel(model);
		list.repaint();
	}
	
	private JPanel createButtonPanel() {
		JButton addButton = new JButton("+");
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
				fileChooser.setFileFilter(new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes()));
				
				int result = fileChooser.showOpenDialog(null);
				if(result == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					photosFiles.add(selectedFile.getAbsolutePath());
					loadPhotosList(photosList, photosFiles);
				}
			}
		});
		JButton delButton = new JButton("-");
		delButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(photosList != null && photosFiles != null && photosFiles.size() > 0) {
					String selectedFile = photosList.getSelectedValue();
					removeStringFromList(photosFiles, selectedFile);
					loadPhotosList(photosList, photosFiles);
				}
			}
		});
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(5, 1));
		buttonPanel.add(addButton);
		buttonPanel.add(delButton);
		
		return buttonPanel;
	}

	public ArrayList<Photo> getPhotos() {
		ArrayList<Photo> photos = new ArrayList<Photo>();
		
		for(String file : photosFiles) {
			Photo photo = new Photo();
			photo.setFile(file);
			photo.setBuilding(building);
			photos.add(photo);
		}
		
		return photos;
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
}
