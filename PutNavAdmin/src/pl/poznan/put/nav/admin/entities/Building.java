package pl.poznan.put.nav.admin.entities;

import java.io.File;
import java.util.ArrayList;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity(name = "Buildings")
@Table(name = "Buildings")
@TableGenerator(name="generator", initialValue=100, allocationSize=1)
public class Building {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="generator")
	@Column(name = "Id", columnDefinition="INTEGER")
	private int id;
	
	@Column(name = "Name")
	private String name;
	
	@Column(name = "Address")
    private String address;
	
	@Column(name = "NumberOfFloors")
    private int numberOfFloors;
    
	@ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "BuildingsDepartments")
	@JoinColumn(name = "Departments")
    private ArrayList<Department> departments = new ArrayList<Department>();
    
	@OneToMany(mappedBy = "building", cascade = CascadeType.ALL, orphanRemoval=true)
    private ArrayList<Map> maps = new ArrayList<Map>();
    
	@OneToMany(mappedBy = "building", cascade = CascadeType.ALL, orphanRemoval=true)
    private ArrayList<Room> rooms = new ArrayList<Room>();
    
	@OneToMany(mappedBy = "building", cascade = CascadeType.ALL, orphanRemoval=true)
    private ArrayList<MapPoint> mapPoints = new ArrayList<MapPoint>();
    
	@OneToMany(mappedBy = "building", cascade = CascadeType.ALL, orphanRemoval=true)
    private ArrayList<Photo> photos = new ArrayList<Photo>();
    
    public Building() {}
    
    public Building(String name, String address, int numberOfFloors) {
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
    
    public File addPhoto(File file) {
    	Photo p = new Photo();
    	p.setFile(file.getAbsolutePath());
    	photos.add(p);
    	return file;
    }
    
    public void removePhoto(Photo photo) {
    	photos.remove(photo);
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

	public ArrayList<Photo> getPhotos() {
		return photos;
	}

	public void setPhotos(ArrayList<Photo> photos) {
		this.photos = photos;
	}

	public ArrayList<Department> getDepartments() {
		return departments;
	}

	public void setDepartments(ArrayList<Department> departments) {
		this.departments = departments;
	}
}
