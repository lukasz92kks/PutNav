package pl.poznan.put.putnav;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class RouteFinder {

    private DatabaseHandler db;
    private ArrayList<MapPoint> mapPoints;
    private ArrayList<MapPointsArcs> mapPointsArcs;

    RouteFinder(ArrayList<MapPoint> nn, ArrayList<MapPointsArcs> mpa) {
        this.mapPoints = nn;
        this.mapPointsArcs = mpa;
    }

    ArrayList<MapPoint> findPath(MapPoint start, MapPoint goal) {


        ArrayList<MapPoint> path = new ArrayList<MapPoint>();
        start.setDistance(0);
        PriorityQueue<MapPoint> toVisit = new PriorityQueue<MapPoint>();
        toVisit.add(goal);

        while (!toVisit.isEmpty()) {
            MapPoint p = toVisit.poll();
            for (MapPointsArcs currentEdge : p.getEdges()) {
                double newDistance = p.getDistance() + currentEdge.getWeight();
                if (newDistance < currentEdge.getPoint2().getDistance()) {
                    MapPoint m = currentEdge.getPoint2();
                    toVisit.remove(m);
                    m.setDistance(newDistance);
                    m.setPrevious(p);
                    toVisit.add(m);
                }
            }
        }

        for (MapPoint m = goal; m.getPrevious() != null; m = m.getPrevious()) {
            path.add(m);
        }

        Collections.reverse(path);
        return path;

    }

}
