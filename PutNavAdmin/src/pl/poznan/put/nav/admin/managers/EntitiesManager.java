package pl.poznan.put.nav.admin.managers;

import java.util.ArrayList;
import java.util.List;

import pl.poznan.put.nav.admin.entities.Building;
import pl.poznan.put.nav.admin.entities.Department;
import pl.poznan.put.nav.admin.entities.Map;
import pl.poznan.put.nav.admin.gui.ActionsPanel;
import pl.poznan.put.nav.admin.gui.MapPanel;

public class EntitiesManager {

	private DatabaseManager databaseManager;
	private List<Building> buildings;
	private List<Building> buildingsToRemove;
	private List<Department> departments;
	private List<Department> departmentsToRemove;
	private List<Map> maps;
	private List<Map> mapsToRemove;

	private Building activeBuilding;
	private Map activeMap;
	private boolean campusMapActive = false;

	public EntitiesManager() {
		buildingsToRemove = new ArrayList<Building>();
		departmentsToRemove = new ArrayList<Department>();
		mapsToRemove = new ArrayList<Map>();
	}
	
	public void loadData() {
		databaseManager = AppFactory.getDatabaseManager();
		buildings = databaseManager.getBuildings();
		departments = databaseManager.getDepartments();
		maps = databaseManager.getMaps();
	}
	
	public boolean isCampusMapActive() {
		return campusMapActive;
	}

	public void setCampusMapActive(boolean campusMapActive) {
		this.campusMapActive = campusMapActive;
	}
	
	public List<Building> getBuildings() {
		return buildings;
	}

	public void setBuildings(List<Building> buildings) {
		this.buildings = buildings;
	}
	
	public List<Building> getBuildingsToRemove() {
		return buildingsToRemove;
	}

	public void setBuildingsToRemove(List<Building> buildingsToRemove) {
		this.buildingsToRemove = buildingsToRemove;
	}
	
	public void addBuildingToRemove(Building b) {
		buildingsToRemove.add(b);
	}

	public List<Department> getDepartments() {
		return departments;
	}

	public void setDepartments(List<Department> departments) {
		this.departments = departments;
	}
	
	public List<Department> getDepartmentsToRemove() {
		return departmentsToRemove;
	}

	public void setDepartmentsToRemove(List<Department> departmentsToRemove) {
		this.departmentsToRemove = departmentsToRemove;
	}
	
	public void addDepartmentToRemove(Department d) {
		departmentsToRemove.add(d);
	}

	public List<Map> getMaps() {
		return maps;
	}

	public void setMaps(List<Map> maps) {
		this.maps = maps;
	}
	
	public List<Map> getMapsToRemove() {
		return mapsToRemove;
	}

	public void setMapsToRemove(List<Map> mapsToRemove) {
		this.mapsToRemove = mapsToRemove;
	}
	
	public void addMapToRemove(Map m) {
		mapsToRemove.add(m);
	}

	public Building getActiveBuilding() {
		return activeBuilding;
	}

	public void setActiveBuilding(Building activeBuilding) {
		this.activeBuilding = activeBuilding;
	}

	public Map getActiveMap() {
		return activeMap;
	}

	public void setActiveMap(Map activeMap) {
		this.activeMap = activeMap;
		
		MapPanel mapPanel = AppFactory.getMapPanel();
		mapPanel.setMap(activeMap);
		
		if(activeMap != null) {
			if(activeMap.isCampus())
				campusMapActive = true;
			else
				campusMapActive = false;
		}
			
		ActionsPanel actionsPanel = AppFactory.getActionsPanel();
		actionsPanel.setActiveMap(activeMap);
	}
}
