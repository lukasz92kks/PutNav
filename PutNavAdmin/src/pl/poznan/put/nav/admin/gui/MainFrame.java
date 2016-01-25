package pl.poznan.put.nav.admin.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.filechooser.FileNameExtensionFilter;

import pl.poznan.put.nav.admin.entities.Building;
import pl.poznan.put.nav.admin.entities.Map;
import pl.poznan.put.nav.admin.entities.Photo;
import pl.poznan.put.nav.admin.managers.AppFactory;
import pl.poznan.put.nav.admin.managers.ArchiveFileManager;
import pl.poznan.put.nav.admin.managers.DatabaseManager;
import pl.poznan.put.nav.admin.managers.EntitiesManager;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = -7706750255666688523L;

	private MapPanel mapPanel = AppFactory.getMapPanel();
	private PropertiesPanel propertiesPanel = AppFactory.getPropertiesPanel();
	private ActionsPanel actionsPanel = AppFactory.getActionsPanel();
	private ArchiveFileManager archiveFileManager = AppFactory.getArchiveFileManager();
	
	public MainFrame() {
		System.out.println("MainFrame");
		this.setTitle("PutNav Admin");
		this.setExtendedState(MAXIMIZED_BOTH);
		this.setLayout(new BorderLayout());
		
		this.add(mapPanel, BorderLayout.CENTER);
		this.add(propertiesPanel, BorderLayout.WEST);
		this.add(actionsPanel, BorderLayout.NORTH);
		
		createMenu();
	}
	
	public void createMenu() {
		JMenu fileMenu = new JMenu("Plik");
		JMenuItem openMenuItem = new JMenuItem("Otwórz...");
		openMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				openMenuItemAction();
			}
		});
		JMenuItem saveMenuItem = new JMenuItem("Zapisz");
		saveMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveMenuItemAction();
			}
		});
		JMenuItem saveAsMenuItem = new JMenuItem("Zapisz jako...");
		JMenuItem exitMenuItem =  new JMenuItem("Zakoncz");
		fileMenu.add(openMenuItem);
		fileMenu.add(saveMenuItem);
		fileMenu.add(saveAsMenuItem);
		fileMenu.add(exitMenuItem);
		
		JMenu editMenu = new JMenu("Edycja");
		JMenuItem deleteAllPoints = new JMenuItem("Usun wszystkie punkty");
		JMenu manageMenuItem = new JMenu("Zarzadzaj");
		JMenuItem manageBuildingsMenuItem = new JMenuItem("Zarzadzaj budynkami");
		manageBuildingsMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				manageBuildingsItemAction();
			}
		});
		JMenuItem manageMapsMenuItem = new JMenuItem("Zarzadzaj mapami");
		manageMapsMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				manageMapsItemAction();
			}
		});
		JMenuItem manageDepartmentsMenuItem = new JMenuItem("Zarzadzaj wydzialami");
		manageDepartmentsMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				manageDepartmentsItemAction();
			}
		});
		editMenu.add(deleteAllPoints);
		editMenu.add(new JSeparator());
		editMenu.add(manageMenuItem);
		manageMenuItem.add(manageBuildingsMenuItem);
		manageMenuItem.add(manageMapsMenuItem);
		manageMenuItem.add(manageDepartmentsMenuItem);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		setJMenuBar(menuBar);
	}
	
	private void openMenuItemAction() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
		fileChooser.setFileFilter(new FileNameExtensionFilter("PNA Put Nav Archive file", "pna"));
		
		int result = fileChooser.showOpenDialog(this);
		if(result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			System.out.println("Selected file: " + selectedFile.getAbsolutePath());
			
			archiveFileManager.openArchiveFile(selectedFile.getAbsolutePath());
			archiveFileManager.extractArchiveFile();
			
			EntitiesManager entitiesManager = AppFactory.getEntitiesManager();
			entitiesManager.loadData();
			
			ArrayList<String> mapsFiles = new ArrayList<String>();
			for(Map map : entitiesManager.getMaps()) {
				mapsFiles.add(map.getMapFile());
			}
			
			if(entitiesManager.getMaps().size() > 0)
				entitiesManager.setActiveMap(entitiesManager.getMaps().get(0));
			propertiesPanel.loadData();
			propertiesPanel.setMapsComboBoxList(mapsFiles);
			
			ArrayList<String> buildingsName = new ArrayList<String>();
			for(Building b : entitiesManager.getBuildings()) {
				buildingsName.add(b.getName());
			}
			propertiesPanel.setBuildings(entitiesManager.getBuildings());
			propertiesPanel.setBuildingsComboBoxList(buildingsName);
		}
	}
	
	private void saveMenuItemAction() {
		System.out.println("Saving changes");
		
		DatabaseManager databaseManager = AppFactory.getDatabaseManager();
		databaseManager.commit();
		
		archiveFileManager.openToWrite();
		
		EntitiesManager entitiesManager = AppFactory.getEntitiesManager();
		for(Map map : entitiesManager.getMaps()) {
			archiveFileManager.addMapFile(map.getMapFile());
		}
		for(Building building : entitiesManager.getBuildings()) {
			for(Photo photo : building.getPhotos()) {
				archiveFileManager.addImageFile(new File(photo.getFile()).getName());
			}
		}
		
		archiveFileManager.addDatabase(archiveFileManager.getDatabaseFileName());
		
		archiveFileManager.closeWrite();
	}
	
	private void manageBuildingsItemAction() {
		BuildingsManagerFrame manager = new BuildingsManagerFrame();
		manager.setVisible(true);
	}

	private void manageMapsItemAction() {
		MapsManagerFrame manager = new MapsManagerFrame();
		manager.setVisible(true);
	}

	private void manageDepartmentsItemAction() {
		DepartmentsManagerFrame manager = new DepartmentsManagerFrame();
		manager.setVisible(true);
	}
}
