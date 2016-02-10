package pl.poznan.put.putnav;

import android.util.Log;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.ArrayList;

@DatabaseTable(tableName = "MapPointsArcs")
public class MapPointsArcs implements Serializable {

    //@DatabaseField(id = true)
    //private int id;

    @DatabaseField(columnName = "FromId")
    private int fromId;

    @DatabaseField(columnName = "ToId")
    private int toId;

    //@DatabaseField(foreign = true, columnName = "FromId", foreignAutoRefresh = true, maxForeignAutoRefreshLevel = 2)
    private MapPoint point1; // inna nazwa?
    //@DatabaseField(foreign = true, columnName = "ToId", foreignAutoRefresh = true, maxForeignAutoRefreshLevel = 2)
    private MapPoint point2;

    private double weight;

    public MapPointsArcs() {}

    public MapPointsArcs(int id, int fromId, int toId) {
        //this.id = id;
        //this.fromId = fromId;
        //this.toId = toId;
        //calculateWeight();
    }

    void setPoint1(ArrayList<MapPoint> vertices) {
        MapPoint x = null;
        for (MapPoint m : vertices) {
            if (m.getId() == fromId) {
                x = m;
                //break;
            }
        }
        this.point1 = x;
    }

    void setPoint2(ArrayList<MapPoint> vertices) {
        MapPoint x = null;
        for (MapPoint m : vertices) {
            if (m.getId() == toId) {
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

    public void calculateWeight(boolean disabled) { //TODO inwalidzi oraz przejscia miedzy budynkami itd.
        int x1 = this.getPoint1().getX();
        int y1 = this.getPoint1().getY();
        int x2 = this.getPoint2().getX();
        int y2 = this.getPoint2().getY();
        int t1 = this.getPoint1().getType();
        int t2 = this.getPoint2().getType();

        /*
        nav nav
        nav door
        nav outdoor
        nav room
        nav building

        room room
        */
        if ((t1 == 1 && t2 == 1) ||
                (t1 == 1 && t2 == 2) || (t1 == 2 && t2 == 1) ||
                (t1 == 1 && t2 == 3) || (t1 == 3 && t2 == 1) ||
                (t1 == 1 && t2 == 6) || (t1 == 6 && t2 == 1) ||
                (t1 == 1 && t2 == 7) || (t1 == 7 && t2 == 1) ||

                (t1 == 6 && t2 == 6)) {
            weight = Math.sqrt((double) ((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)));

        } else if ((t1 == 1 && t2 == 4) || (t1 == 4 && t2 == 1)) { //nav stairs
            weight = 20;
        } else if ((t1 == 1 && t2 == 5) || (t1 == 5 && t2 == 1)) { //nav lift
            weight = 100;
        } else if (t1 == 5 && t2 == 5) { // lift lift
            weight = 5;
        } else if (t1 == 2 && t2 == 2) { // door door
            weight = 1;
        } else if ((t1 == 3 && t2 == 7) || (t1 == 7 && t2 == 3)) { // outdoor building
            weight = 300;
        }

        if (!disabled) {
            if (t1 == 4 && t2 == 4) { // stairs stairs
                weight = 60.0;
            }
        } else {
            if (t1 == 4 && t2 == 4) { // stairs stairs
                weight = 10000.0;
            }
        }


            /*
            NAVIGATION = 1;
            DOOR = 2;
            OUTDOOR = 3;
            STAIRS = 4;
            LIFT = 5;
            ROOM = 6;
            BUILDING = 7;
             */

    }
}
