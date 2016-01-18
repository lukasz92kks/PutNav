package pl.poznan.put.putnav;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "MapPointsArcs")
public class MapPointsArcs {

    @DatabaseField(id = true)
    private int id;

    @DatabaseField
    private int fromId; // inna nazwa?

    @DatabaseField
    private int toId;

    private MapPoint from; // inna nazwa?
    private MapPoint to;


    private double weight;

    public MapPointsArcs() {}

    public MapPointsArcs(int id, MapPoint from, MapPoint to) {
        this.id = id;
        this.from = from;
        this.to = to;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MapPoint getFrom() {
        return from;
    }

    public void setFrom(MapPoint from) {
        this.from = from;
    }

    public MapPoint getTo() {
        return to;
    }

    public void setTo(MapPoint to) {
        this.to = to;
    }

    public double getWeight() {
        return this.weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void calculateWeight() {
        int x1 = this.getFrom().getX();
        int y1 = this.getFrom().getY();
        int x2 = this.getTo().getX();
        int y2 = this.getTo().getY();
        weight = Math.sqrt((double) ((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)));
    }
}
