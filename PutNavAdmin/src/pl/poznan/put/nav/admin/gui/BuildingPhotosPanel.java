package pl.poznan.put.nav.admin.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ScrollPaneConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import pl.poznan.put.nav.admin.entities.Building;
import pl.poznan.put.nav.admin.entities.Department;
import pl.poznan.put.nav.admin.entities.Photo;
import pl.poznan.put.nav.admin.managers.AppFactory;
import pl.poznan.put.nav.admin.managers.EntitiesManager;

public class BuildingPhotosPanel extends JPanel {

	private static final long serialVersionUID = -2807147583255286939L;
	
	//private Building building;
	private JList photosList;
	private List<String> photosFiles;
	
	EntitiesManager em = AppFactory.getEntitiesManager();
	
	public BuildingPhotosPanel() {
		//this.building = building;
		
		this.setPreferredSize(new Dimension(800, 550));
		initPhotosList();
		this.add(createButtonPanel());
	}
	
	private void initPhotosList() {
		photosList = new JList();
		JScrollPane scrollPane = new JScrollPane(photosList);
		scrollPane.setPreferredSize(new Dimension(700, 540));
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.add(scrollPane);
		
		photosFiles = new ArrayList<String>();
		for(Photo p : em.getActiveBuilding().getPhotos()) {
			photosFiles.add(p.getFile());
		}
		
		loadPhotosList(photosList, photosFiles);
	}
	
	private void loadPhotosList(JList<String> list, List<String> photos) {
		DefaultListModel model = new DefaultListModel<String>();
		for(String p : photos) {
			ImageIcon icon = resizeImageIcon(new ImageIcon(p), 320, 240);
			model.addElement(new ListEntry(p, icon));
		}
		list.setModel(model);
		list.setCellRenderer(new ListEntryCellRenderer());
		list.repaint();
	}
	
	private ImageIcon resizeImageIcon(ImageIcon icon, int width, int height) {
		Image image = icon.getImage();
		Image newimg = image.getScaledInstance(width, height,  java.awt.Image.SCALE_SMOOTH); 
		icon = new ImageIcon(newimg);
		
		return icon;
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
					String selectedFile = photosList.getSelectedValue().toString();
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
			photo.setBuilding(em.getActiveBuilding());
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
	
	
	class ListEntry
	{
	   private String value;
	   private ImageIcon icon;
	  
	   public ListEntry(String value, ImageIcon icon) {
	      this.value = value;
	      this.icon = icon;
	   }
	  
	   public String getValue() {
	      return value;
	   }
	  
	   public ImageIcon getIcon() {
	      return icon;
	   }
	  
	   public String toString() {
	      return value;
	   }
	}
	  
	class ListEntryCellRenderer extends JLabel implements ListCellRenderer
	{
		private static final long serialVersionUID = 3984786259130607978L;
		private JLabel label;
	  
		public Component getListCellRendererComponent(JList list, Object value,
	                                                 int index, boolean isSelected,
	                                                 boolean cellHasFocus) {
			ListEntry entry = (ListEntry) value;
	  
			setText(value.toString());
			setIcon(entry.getIcon());
	   
			if (isSelected) {
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			}
			else {
				setBackground(list.getBackground());
	         	setForeground(list.getForeground());
			}
	  
			setEnabled(list.isEnabled());
			setFont(list.getFont());
			setOpaque(true);
	  
			return this;
	   }
	}
}
