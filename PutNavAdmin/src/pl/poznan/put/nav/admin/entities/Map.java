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

@Entity(name = "Maps")
@Table(name = "Maps")
@TableGenerator(name="generator", initialValue=100, allocationSize=1)
public class Map {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="generator")
	@Column(name = "Id", columnDefinition="INTEGER")
	private int id;
	
	@Column(name = "Floor")
	private int floor;
	
	@Column(name = "FileName")
	private String mapPath;
	
	@JoinColumn(name = "Building")
	private Building building;
	
	@OneToMany(mappedBy = "map", cascade = CascadeType.ALL, orphanRemoval=true)
	private ArrayList<MapPoint> mapPoints = new ArrayList<MapPoint>();
	
	public Map() {}
	
	public Map(int id, int floor, String mapPath) {
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

	public String getMapFile() {
		return mapPath;
	}

	public void setMapFile(String mapPath) {
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
