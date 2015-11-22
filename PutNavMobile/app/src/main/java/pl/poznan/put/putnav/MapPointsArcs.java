package pl.poznan.put.putnav;

public class MapPointsArcs {

    private int id;
    private MapPoint from;
    private MapPoint to;

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
}
