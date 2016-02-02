package pl.poznan.put.putnav;

import android.widget.ImageView;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;

@DatabaseTable(tableName = "Maps")

public class Map {

    @DatabaseField(id = true, columnName = "Id")
    private int id;
    @DatabaseField(columnName = "Floor")
    private int floor;
    @DatabaseField(columnName = "Campus")
    private int campus;
    @DatabaseField(foreign = true, columnName = "Building")
    private Building buildings;
    @DatabaseField(columnName = "FileName")
    private String fileName;

    private ImageView mapImage = null;

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

    public int getCampus() {
        return campus;
    }

    public void setCampus(int campus) {
        this.campus = campus;
    }

    public String getFileName() {
        return fileName;
    }
}
