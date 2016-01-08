package pl.poznan.put.nav.admin.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity(name = "MapPoints")
@Table(name = "MapPoints")
@TableGenerator(name="generator", initialValue=100, allocationSize=1)
public class MapPoint {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="generator")
	@Column(name = "Id", columnDefinition="INTEGER")
	private int id;
	private int x;
	private int y;
	@Column(name = "Type")
	private int type;
	@JoinColumn(name = "Map")
	private Map map;
	@JoinColumn(name = "Building")
	private Building building;
	@JoinColumn(name = "Room")
	private Room room;
	
	@ManyToMany(cascade={CascadeType.ALL})
    @JoinTable(name="MapPointsArcs",
        joinColumns={@JoinColumn(name="FromId")},
        inverseJoinColumns={@JoinColumn(name="ToId")})
    private Set<MapPoint> successors = new HashSet<MapPoint>(); // lista nastepnikow
 
    @ManyToMany(mappedBy="successors")
    private Set<MapPoint> predecessors = new HashSet<MapPoint>(); // lista poprzednikow
	
	public MapPoint() {}

	public MapPoint(int x, int y, int type) {
		this.x = x;
		this.y = y;
		this.type = type;
	}
	
	public MapPoint addSuccessor(MapPoint mapPoint) {
		successors.add(mapPoint);
    	return mapPoint;
    }
    
    public void removeSuccessor(MapPoint mapPoint) {
    	successors.remove(mapPoint);
    }

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public void move(int x, int y) {
		setX(x);
		setY(y);
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public Building getBuilding() {
		return building;
	}

	public void setBuilding(Building building) {
		this.building = building;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public Set<MapPoint> getSuccessors() {
		return successors;
	}

	public void setSuccessors(Set<MapPoint> successors) {
		this.successors = successors;
	}
}
