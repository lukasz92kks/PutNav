package pl.poznan.put.putnav;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;

@DatabaseTable(tableName = "Rooms")

public class Room {

    @DatabaseField(id = true)
    private int Id;
    @DatabaseField
    private String Name;
    @DatabaseField
    private String Function;
    //@DatabaseField
    private int floor;
    //@DatabaseField(foreign = true)
    private Building building;
    private ArrayList<MapPoint> mapPoints = new ArrayList<MapPoint>();

    public Room() {}

    public Room(int id, String name, String function, int floor) {
        this.Id = id;
        this.Name = name;
        this.Function = function;
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
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getFunction() {
        return Function;
    }

    public void setFunction(String function) {
        this.Function = function;
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
