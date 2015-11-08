package pl.poznan.put.nav.admin;

public class AppFactory {

	private static MapPanel mapPanel;
	private static PropertiesPanel propertiesPanel;
	private static ActionsPanel actionsPanel;
	
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
}
