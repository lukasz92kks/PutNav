package pl.poznan.put.nav.admin;

import java.io.File;
import java.util.ArrayList;

public interface DatabaseInterface {
	
	public ArrayList<Building> getAllBuildings();
	
	public Building getBuildingById(int id);
	
	public Building getBuildingByMapPoint(MapPoint mapPoint);
	
	public Building getBuildingByName(String name);
	
	public ArrayList<File> getImagesByBuilding(Building building);
	
	public File getImageById(int id);
	
	public ArrayList<Department> getAllDepartments();
	
	public ArrayList<Department> getDepartmentsByBuilding(Building building);
	
	public Department getDepartmentById(int id);
	
	public Department getDepartmentByName(String name);
	
	public ArrayList<Map> getAllMaps();
	
	public ArrayList<Map> getMapsByBuilding(Building building);
	
	public Map getMapByFileName(String fileName);
	
	public ArrayList<MapPoint> getMapPointsByMap(Map map);
	
	public ArrayList<Room> getRoomsByBuilding(Building building);
	
	public ArrayList<Room> getRoomsByMap(Map map);
	
	public Room getRoomByName(String name);
}
