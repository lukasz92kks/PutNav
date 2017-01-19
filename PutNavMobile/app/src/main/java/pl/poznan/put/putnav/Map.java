package pl.poznan.put.putnav;

import android.widget.ImageView;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.ArrayList;

@DatabaseTable(tableName = "Maps")

public class Map implements Comparable<Map>, Serializable {

    @DatabaseField(id = true, columnName = "Id")
    private int id;
    @DatabaseField(columnName = "Floor")
    private int floor;
    @DatabaseField(columnName = "Campus")
    private int campus;
    @DatabaseField(foreign = true, columnName = "Building", foreignAutoRefresh = true, maxForeignAutoRefreshLevel = 2)
    private Building building;
    @DatabaseField(columnName = "FileName")
    private String fileName;

    private ArrayList<MapPoint> mapPoints = new ArrayList<MapPoint>();

    public Map() {}

    public Map(int id, int floor) {
        this.id = id;
        this.floor = floor;
    }

    public MapPoint addMapPoint(MapPoint point) {
        mapPoints.add(point);
        return point;
    }

    @Override
    public int compareTo(Map map){
        return ((Integer)this.getFloor()).compareTo(((Integer)map.getFloor()));
    }

    public void removeMapPoint(MapPoint point) {
        mapPoints.remove(point);
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

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    public boolean isCampus() {
        if(campus>0){
            return true;
        } else {
            return false;
        }
    }

    public void setCampus(int campus) {
        this.campus = campus;
    }

    public String getFileName() {
        return fileName;
    }
    public String getDrawableName(){
        return fileName.substring(0, fileName.length()-4);
    }
}
