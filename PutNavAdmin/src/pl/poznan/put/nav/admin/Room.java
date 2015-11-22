package pl.poznan.put.nav.admin;

import java.util.ArrayList;

public class Room {

	private int id;
	private String name;
    private String function;
    private int floor;
    private Building building;
    private ArrayList<MapPoint> mapPoints = new ArrayList<MapPoint>();
    
    public Room() {}
    
    public Room(int id, String name, String function, int floor) {
    	this.id = id;
    	this.name = name;
    	this.function = function;
    	this.floor = floor;
    }
    
    public MapPoint addMapPoint(MapPoint point) {
		mapPoints.add(point);
		return point;
	}
	
	public void removeMapPoint(MapPoint point) {
		mapPoints.remove(point);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
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

    public ArrayList<MapPoint> getMapPoints() {
        return mapPoints;
    }

    public void setMapPoints(ArrayList<MapPoint> mapPoints) {
        this.mapPoints = mapPoints;
    }
}
