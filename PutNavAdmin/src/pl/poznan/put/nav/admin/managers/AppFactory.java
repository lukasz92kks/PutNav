package pl.poznan.put.nav.admin.managers;

import pl.poznan.put.nav.admin.gui.ActionsPanel;
import pl.poznan.put.nav.admin.gui.MapPanel;
import pl.poznan.put.nav.admin.gui.PropertiesPanel;

public class AppFactory {

	private static MapPanel mapPanel;
	private static PropertiesPanel propertiesPanel;
	private static ActionsPanel actionsPanel;
	private static ArchiveFileManager archiveFileManager;
	private static DatabaseManager databaseManager;
	private static EntitiesManager entitiesManager;
	
	public static MapPanel getMapPanel() {
		if(mapPanel == null)
			mapPanel = new MapPanel();
		return mapPanel;
	}
	
	public static PropertiesPanel getPropertiesPanel() {
		if(propertiesPanel == null)
			propertiesPanel = new PropertiesPanel();
		return propertiesPanel;
	}
	
	public static ActionsPanel getActionsPanel() {
		if(actionsPanel == null)
			actionsPanel = new ActionsPanel();
		return actionsPanel;
	}
	
	public static ArchiveFileManager getArchiveFileManager() {
		if(archiveFileManager == null)
			archiveFileManager = new ArchiveFileManager();
		return archiveFileManager;
	}
	
	public static DatabaseManager getDatabaseManager() {
		if(databaseManager == null)
			databaseManager = new DatabaseManager();
		return databaseManager;
	}
	
	public static EntitiesManager getEntitiesManager() {
		if(entitiesManager == null)
			entitiesManager = new EntitiesManager();
		return entitiesManager;
	}
}
