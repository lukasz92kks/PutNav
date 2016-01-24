package pl.poznan.put.putnav;

import android.widget.ImageView;

import com.j256.ormlite.field.DatabaseField;

import java.util.ArrayList;

public class Map {

    @DatabaseField(id = true)
    private int id;
    @DatabaseField
    private int floor;

    private ImageView mapImage = null;
    @DatabaseField(foreign = true)
    private Building buildings;

    private ArrayList<MapPoint> mapPoints = new ArrayList<MapPoint>();

    public Map() {}

    public Map(int id, int floor, ImageView mapImage) {
        this.id = id;
        this.floor = floor;
        this.mapImage = mapImage;
    }

    public MapPoint addMapPoint(MapPoint point) {
        mapPoints.add(point);
        return point;
    }

    public void removeMapPoint(MapPoint point) {
        mapPoints.remove(point);
    }

    public ImageView getMapImage() {
        return mapImage;
    }

    public void setMapImage(ImageView mapImage) {
        this.mapImage = mapImage;
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

    public Building getBuildings() {
        return buildings;
    }

    public void setBuildings(Building buildings) {
        this.buildings = buildings;
    }
}
