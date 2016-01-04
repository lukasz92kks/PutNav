package pl.poznan.put.nav.admin.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileNameExtensionFilter;

import pl.poznan.put.nav.admin.entities.Map;
import pl.poznan.put.nav.admin.managers.AppFactory;
import pl.poznan.put.nav.admin.managers.ArchiveFileManager;
import pl.poznan.put.nav.admin.managers.DatabaseManager;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = -7706750255666688523L;

	private MapPanel mapPanel = AppFactory.getMapPanel();
	private PropertiesPanel propertiesPanel = AppFactory.getPropertiesPanel();
	private ActionsPanel actionsPanel = AppFactory.getActionsPanel();
	private ArchiveFileManager archiveFileManager = AppFactory.getArchiveFileManager();
	
	public MainFrame() {
		this.setExtendedState(MAXIMIZED_BOTH);
		this.setLayout(new BorderLayout());
		
		this.add(mapPanel, BorderLayout.CENTER);
		this.add(propertiesPanel, BorderLayout.WEST);
		this.add(actionsPanel, BorderLayout.NORTH);
		
		createMenu();
	}
	
	public void createMenu() {
		JMenu fileMenu = new JMenu("Plik");
		JMenu addMenuItem = new JMenu("Dodaj");
		JMenuItem addBuildingMenuItem = new JMenuItem("Budynek");
		JMenuItem addMapMenuItem = new JMenuItem("Mape");
		JMenuItem addImageMenuItem = new JMenuItem("Zdjecie");
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
		addMenuItem.add(addBuildingMenuItem);
		addMenuItem.add(addMapMenuItem);
		addMenuItem.add(addImageMenuItem);
		fileMenu.add(addMenuItem);
		fileMenu.add(openMenuItem);
		fileMenu.add(saveMenuItem);
		fileMenu.add(saveAsMenuItem);
		fileMenu.add(exitMenuItem);
		
		JMenu editMenu = new JMenu("Edycja");
		JMenuItem undoMenuItem = new JMenuItem("Cofnij");
		JMenuItem redoMenuItem = new JMenuItem("Ponow");
		JMenuItem deleteAllPoints = new JMenuItem("Usun wszystkie punkty");
		editMenu.add(undoMenuItem);
		editMenu.add(redoMenuItem);
		editMenu.add(deleteAllPoints);
		
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
			
			DatabaseManager databaseManager = AppFactory.getDatabaseManager();
			List<Map> maps = databaseManager.getMaps();
			
			ArrayList<String> mapsFiles = new ArrayList<String>();
			for(Map map : maps) {
				mapsFiles.add(map.getMapFile());
			}
			
			if(maps.size() > 0)
				mapPanel.setMap(maps.get(0));
			propertiesPanel.setMaps(maps);
			propertiesPanel.setMapsComboBoxList(mapsFiles);
			
		}
	}
	
	private void saveMenuItemAction() {
		System.out.println("Saving changes");
		
		DatabaseManager databaseManager = AppFactory.getDatabaseManager();
		databaseManager.commit();
		
		archiveFileManager.addDatabase(archiveFileManager.getDatabaseFileName());
	}
}
