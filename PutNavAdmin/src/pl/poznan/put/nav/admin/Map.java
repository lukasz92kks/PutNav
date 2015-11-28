package pl.poznan.put.nav.admin;

import java.io.File;
import java.util.ArrayList;

public class Map {

	private int id;
	private int floor;
	private File mapPath = null;
	private Building building;
	private ArrayList<MapPoint> mapPoints = new ArrayList<MapPoint>();
	
	public Map() {}
	
	public Map(int id, int floor, File mapPath) {
		this.id = id;
		this.floor = floor;
		this.mapPath = mapPath;
	}
	
	public MapPoint addMapPoint(MapPoint point) {
		mapPoints.add(point);
		return point;
	}
	
	public void removeMapPoint(MapPoint point) {
		mapPoints.remove(point);
	}

	public File getMapFile() {
		return mapPath;
	}

	public void setMapFile(File mapPath) {
		this.mapPath = mapPath;
	}

	public ArrayList<MapPoint> getMapPoints() {
		return mapPoints;
	}

	public void setMapPoints(ArrayList<MapPoint> mapPoints) {
		this.mapPoints = mapPoints;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFloor() {
		return floor;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}

	public Building getBuilding() {
		return building;
	}

	public void setBuilding(Building building) {
		this.building = building;
	}
}
