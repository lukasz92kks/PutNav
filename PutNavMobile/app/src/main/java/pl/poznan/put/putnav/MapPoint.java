package pl.poznan.put.putnav;

import java.util.ArrayList;

public class MapPoint {

    private int id;
    private int x;
    private int y;
    private int type;
    private Map map;
    private Building building;
    private Room room;
    private ArrayList<MapPoint> successors = new ArrayList<MapPoint>();	  // lista nastepnikow

    public MapPoint() {}

    public MapPoint(int id, int x, int y, int type) {
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

    public ArrayList<MapPoint> getSuccessors() {
        return successors;
    }

    public void setSuccessors(ArrayList<MapPoint> successors) {
        this.successors = successors;
    }
}
