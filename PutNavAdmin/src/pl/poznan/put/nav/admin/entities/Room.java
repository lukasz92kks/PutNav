package pl.poznan.put.nav.admin.entities;

import java.util.ArrayList;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity(name = "Rooms")
@Table(name = "Rooms")
@TableGenerator(name="generator", initialValue=100, allocationSize=1)
public class Room {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="generator")
	@Column(name = "Id", columnDefinition="INTEGER")
	private int id;
	
	@Column(name = "Name")
	private String name;
	
	@Column(name = "Function")
    private String function;
	
	@Column(name = "Floor")
    private int floor;
	
	@JoinColumn(name = "Building")
    private Building building;
    
	@OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval=true)
    private ArrayList<MapPoint> mapPoints = new ArrayList<MapPoint>();
    
    public Room() {}
    
    public Room(String name, String function, int floor) {
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
