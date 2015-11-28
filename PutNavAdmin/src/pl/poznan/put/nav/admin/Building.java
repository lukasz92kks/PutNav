package pl.poznan.put.nav.admin;

import java.io.File;
import java.util.ArrayList;

public class Building {

	private int id;
	private String name;
    private String address;
    private int numberOfFloors;
    private ArrayList<Department> departments = new ArrayList<Department>();
    private ArrayList<Map> maps = new ArrayList<Map>();
    private ArrayList<Room> rooms = new ArrayList<Room>();
    private ArrayList<MapPoint> mapPoints = new ArrayList<MapPoint>();
    private ArrayList<File> images = new ArrayList<File>();
    
    public Building() {}
    
    public Building(int id, String name, String address, int numberOfFloors) {
    	this.id  = id;
    	this.name = name;
    	this.address = address;
    	this.numberOfFloors = numberOfFloors;
    }
    
    public Department addDepartment(Department department) {
    	departments.add(department);
    	return department;
    }
    
    public void removeDepartment(Department department) {
    	departments.remove(department);
    }
    
    public Map addMap(Map map) {
    	maps.add(map);
    	return map;
    }
    
    public void removeMap(Map map) {
    	maps.remove(map);
    }
    
    public Room addRoom(Room room) {
    	rooms.add(room);
    	return room;
    }
    
    public void removeRoom(Room room) {
    	rooms.remove(room);
    }
    
    public MapPoint addMapPoint(MapPoint mapPoint) {
    	mapPoints.add(mapPoint);
    	return mapPoint;
    }
    
    public void removeMapPoint(MapPoint mapPoint) {
    	mapPoints.remove(mapPoint);
    }
    
    public File addImage(File image) {
    	images.add(image);
    	return image;
    }
    
    public void removeImage(File image) {
    	images.remove(image);
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getNumberOfFloors() {
        return numberOfFloors;
    }

    public void setNumberOfFloors(int numberOfFloors) {
        this.numberOfFloors = numberOfFloors;
    }

    public ArrayList<Map> getMaps() {
        return maps;
    }

    public void setMaps(ArrayList<Map> maps) {
        this.maps = maps;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public void setRooms(ArrayList<Room> rooms) {
        this.rooms = rooms;
    }

    public ArrayList<MapPoint> getMapPoints() {
        return mapPoints;
    }

    public void setMapPoints(ArrayList<MapPoint> mapPoints) {
        this.mapPoints = mapPoints;
    }

	public ArrayList<File> getImages() {
		return images;
	}

	public void setImages(ArrayList<File> images) {
		this.images = images;
	}

	public ArrayList<Department> getDepartments() {
		return departments;
	}

	public void setDepartments(ArrayList<Department> departments) {
		this.departments = departments;
	}
}
