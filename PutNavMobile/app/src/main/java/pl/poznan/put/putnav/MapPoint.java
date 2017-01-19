package pl.poznan.put.putnav;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.ArrayList;



@DatabaseTable(tableName = "MapPoints")

public class MapPoint implements Comparable<MapPoint>, Serializable{

    @DatabaseField(id = true, columnName = "Id")
    private int id;
    @DatabaseField(columnName = "X")
    private int x;
    @DatabaseField(columnName = "Y")
    private int y;
    @DatabaseField(columnName = "Type")
    private int typeId;
    @DatabaseField(foreign = true, columnName = "Map", foreignAutoRefresh = true, maxForeignAutoRefreshLevel = 3)
    private Map map;
    @DatabaseField (foreign = true, columnName = "Building", foreignAutoRefresh = true, maxForeignAutoRefreshLevel = 2)
    private Building building;
    @DatabaseField(foreign = true, columnName = "Room", foreignAutoRefresh = true)
    private Room room;

    private double distance;
    private MapPoint previous;

    private boolean deactivated;
    private MapPointTypes type;

    enum MapPointTypes {
        NAVIGATION,
        DOOR,
        OUTDOOR,
        STAIRS,
        LIFT,
        ROOM,
        BUILDING
    }

    private ArrayList<MapPoint> successors = new ArrayList<MapPoint>();	  // objects nastepnikow
    private ArrayList<MapPointsArcs> edges = new ArrayList<MapPointsArcs>(); //objects krawedzi wychodzacych z danego wierzcholka

    @Override
    public String toString() {
        return Integer.toString(id);
    }

    @Override
    public int compareTo(MapPoint another) {
        return Double.compare(distance, another.distance);
    }

    public MapPoint() {
        this.distance = Double.longBitsToDouble(0x7ff0000000000000L);
        deactivated = false;
    }

    public MapPoint(int id, int x, int y, int typeId) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.typeId = typeId;
        this.distance = Double.longBitsToDouble(0x7ff0000000000000L);
    }

    public boolean isDeactivated() {
        return deactivated;
    }

    public void setDeactivated(boolean isDeactivated) {
        this.deactivated = isDeactivated;
    }


    void searchEdges(ArrayList<MapPointsArcs> allEdges) {
        ArrayList<MapPointsArcs> edges = new ArrayList<>();
        for (MapPointsArcs current : allEdges) {
            if (current.getPoint1().getId() == this.getId() || current.getPoint2().getId() == this.getId())
                edges.add(current);
        }

        this.edges =  edges;
    }

    //po co zwracamy?
    public MapPoint addSuccessor(MapPoint mapPoint) {
        successors.add(mapPoint);
        return mapPoint;
    }

    public MapPoint findPointById(ArrayList<MapPoint> points, int id) {
        for (MapPoint m : points) {
            if (m.getId() == id) return m;
        }
        return null;
    }

    public void removeSuccessor(MapPoint mapPoint) {
        successors.remove(mapPoint);
    }

    public void addEdge(MapPointsArcs edge) {
        edges.add(edge);
    }

    public void removeEdge(MapPointsArcs edge) {
        edges.remove(edge);
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

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public double getDistance() {
        return this.distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public MapPoint getPrevious() {
        return this.previous;
    }

    public void setPrevious(MapPoint previos) {
        this.previous = previos;
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

    public ArrayList<MapPointsArcs> getEdges() {
        return edges;
    }

    public void setEdges(ArrayList<MapPointsArcs> edges) {
        this.edges = edges;
    }

    public void addSimpleEdge(MapPointsArcs arc) {
        edges.add(arc);
    }

    public void setType(){
        switch (typeId){
            case 1:
                type = MapPointTypes.NAVIGATION;
                break;
            case 2:
                type = MapPointTypes.DOOR;
                break;
            case 3:
                type = MapPointTypes.OUTDOOR;
                break;
            case 4:
                type = MapPointTypes.STAIRS;
                break;
            case 5:
                type = MapPointTypes.LIFT;
                break;
            case 6:
                type = MapPointTypes.ROOM;
                break;
            case 7:
                type = MapPointTypes.BUILDING;
                break;
        }
    }
    public MapPointTypes getType(){
        return type;
    }
}
