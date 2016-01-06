package pl.poznan.put.nav.admin.entities;

import java.util.ArrayList;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@Entity(name = "Departments")
@Table(name = "Departments")
@TableGenerator(name="generator", initialValue=100, allocationSize=1)
public class Department {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="generator")
	@Column(name = "Id", columnDefinition="INTEGER")
	private int id;
	
	@Column(name = "Name")
	private String name;
	
	@ManyToMany(mappedBy = "departments", cascade = CascadeType.ALL)
	private ArrayList<Building> buildings = new ArrayList<Building>();
	
	public Department() {}
	
	public Department(String name) {
		this.name = name;
	}
	
	public Building addBuilding(Building building) {
		buildings.add(building);
    	return building;
    }
    
    public void removeBuilding(Building building) {
    	buildings.remove(building);
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
	
	public ArrayList<Building> getBuildings() {
		return buildings;
	}
	
	public void setBuildings(ArrayList<Building> buildings) {
		this.buildings = buildings;
	}
}
