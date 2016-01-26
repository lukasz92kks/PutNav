package pl.poznan.put.putnav;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.ArrayList;

@DatabaseTable(tableName = "MapPointsArcs")
public class MapPointsArcs implements Serializable {

    /*@DatabaseField(id = true)
    private int id;*/

    @DatabaseField
    private int FromId;

    @DatabaseField
    private int ToId;

    private MapPoint point1; // inna nazwa?
    private MapPoint point2;

    private double weight;

    public MapPointsArcs() {}

    public MapPointsArcs(int id, int fromId, int toId) {
        //this.id = id;
        this.FromId = fromId;
        this.ToId = toId;
        calculateWeight();
    }

    void setPoint1(ArrayList<MapPoint> vertices) {
        MapPoint x = null;
        for (MapPoint m : vertices) {
            if (m.getId() == FromId) {
                x = m;
                //break;
            }
        }
        this.point1 = x;
    }

    void setPoint2(ArrayList<MapPoint> vertices) {
        MapPoint x = null;
        for (MapPoint m : vertices) {
            if (m.getId() == ToId) {
                x = m;
                //break;
            }
        }
        this.point2 = x;
    }

    /*public int getId() {
        return id;
    }*/

    /*public void setId(int id) {
        this.id = id;
    }*/

    public MapPoint getPoint1() {
        return point1;
    }

    public void setPoint1(MapPoint point1) {
        this.point1 = point1;
    }

    public MapPoint getPoint2() {
        return point2;
    }

    public void setPoint2(MapPoint point2) {
        this.point2 = point2;
    }

    public double getWeight() {
        return this.weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void calculateWeight() {
        if (this.getPoint1().getType() != this.getPoint2().getType()) {
            weight = 30.0;
        } else {
            int x1 = this.getPoint1().getX();
            int y1 = this.getPoint1().getY();
            int x2 = this.getPoint2().getX();
            int y2 = this.getPoint2().getY();
            weight = Math.sqrt((double) ((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)));
        }
    }
}
