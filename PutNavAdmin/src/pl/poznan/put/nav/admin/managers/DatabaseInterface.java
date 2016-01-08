package pl.poznan.put.nav.admin.managers;

import java.util.List;

import pl.poznan.put.nav.admin.entities.Building;
import pl.poznan.put.nav.admin.entities.Department;
import pl.poznan.put.nav.admin.entities.Map;

public interface DatabaseInterface {
	
	public List<Building> getBuildings();
	
	public List<Department> getDepartments();
	
	public List<Map> getMaps();
	
	public void commit();
}
