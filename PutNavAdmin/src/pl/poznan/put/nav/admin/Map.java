package pl.poznan.put.nav.admin;

import java.awt.Image;
import java.util.ArrayList;

public class Map {

	private int id;
	private int floor;
	private Image mapImage = null;
	private Building buildings;
	private ArrayList<MapPoint> mapPoints = new ArrayList<MapPoint>();
	
	public Map() {}
	
	public Map(int id, int floor, Image mapImage) {
		this.id = id;
		this.floor = floor;
		this.mapImage = mapImage;
	}
	
	public MapPoint addMapPoint(MapPoint point) {
		mapPoints.add(point);
		return point;
	}
	
	public void removeMapPoint(MapPoint point) {
		mapPoints.remove(point);
	}

	public Image getMapImage() {
		return mapImage;
	}

	public void setMapImage(Image mapImage) {
		this.mapImage = mapImage;
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

	public Building getBuildings() {
		return buildings;
	}

	public void setBuildings(Building buildings) {
		this.buildings = buildings;
	}
}
