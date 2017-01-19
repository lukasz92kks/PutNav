package pl.poznan.put.putnav;

import java.io.Serializable;
import java.util.ArrayList;

class SerializedObjects implements Serializable {
    private ArrayList<MapPoint> mapPoints;
    private ArrayList<MapPointsArcs> mapPointsArcs;
    private ArrayList<Building> buildings;
    private ArrayList<Photo> photos;
    private ArrayList<Department> departments;
    private ArrayList<Room> rooms;
    private ArrayList<Map> maps;

    SerializedObjects (){

    }

    SerializedObjects(
            ArrayList<MapPoint> mapPoints,
            ArrayList<MapPointsArcs> mapPointsArcs,
            ArrayList<Map> maps,
            ArrayList<Building> buildings,
            ArrayList<Photo> photos,
            ArrayList<Department> departments,
            ArrayList<Room> rooms
    ) {
        this.mapPoints = mapPoints;
        this.mapPointsArcs = mapPointsArcs;
        this.buildings = buildings;
        this.photos = photos;
        this.departments = departments;
        this.rooms = rooms;
        this.maps = maps;
    }

    public ArrayList<MapPoint> getMapPoints() {
        return mapPoints;
    }

    public ArrayList<MapPointsArcs> getMapPointsArcs() {
        return mapPointsArcs;
    }

    public ArrayList<Building> getBuildings() {
        return buildings;
    }

    public ArrayList<Photo> getPhotos() {
        return photos;
    }

    public ArrayList<Department> getDepartments() {
        return departments;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public ArrayList<Map> getMaps() {
        return maps;
    }
}
