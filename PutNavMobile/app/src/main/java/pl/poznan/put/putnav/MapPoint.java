package pl.poznan.put.putnav;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;

@DatabaseTable(tableName = "MapPoint")

public class MapPoint implements Comparable<MapPoint> {

    @DatabaseField(id = true)
    private int id;
    @DatabaseField
    private int x;
    @DatabaseField
    private int y;

    @DatabaseField
    private int type;
    @DatabaseField
    private Map map;
    @DatabaseField
    private Building building;
    @DatabaseField
    private Room room;

    private double distance;
    private MapPoint previous;

    private ArrayList<MapPoint> successors = new ArrayList<MapPoint>();	  // lista nastepnikow
    private ArrayList<MapPointsArcs> edges = new ArrayList<MapPointsArcs>(); //lista krawedzi wychodzacych z danego wierzcholka

    @Override
    public int compareTo(MapPoint another) {
        return Double.compare(distance, another.distance);
    }

    public MapPoint() {}

    public MapPoint(int id, int x, int y, int type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.distance = Double.longBitsToDouble(0x7ff0000000000000L);
    }

    //po co zwracamy?
    public MapPoint addSuccessor(MapPoint mapPoint) {
        successors.add(mapPoint);
        return mapPoint;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
}
