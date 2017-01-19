package pl.poznan.put.putnav;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.ArrayList;

@DatabaseTable(tableName = "Departments")
public class Department implements Serializable{

    @DatabaseField(id = true, columnName = "Id")
    private int id;
    @DatabaseField(columnName = "Name")
    private String name;
    private ArrayList<Building> buildings = new ArrayList<>();

    public Department() {}

    public Department(int id, String name) {
        this.id = id;
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
