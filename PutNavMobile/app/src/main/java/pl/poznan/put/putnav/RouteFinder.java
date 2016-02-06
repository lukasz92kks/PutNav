package pl.poznan.put.putnav;

import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class RouteFinder implements Serializable {

    private DatabaseHandler db;
    private ArrayList<MapPoint> mapPoints;
    private ArrayList<MapPointsArcs> mapPointsArcs;

    RouteFinder(ArrayList<MapPoint> nn, ArrayList<MapPointsArcs> mpa) {
        this.mapPoints = nn;
        this.mapPointsArcs = mpa;
    }

    ArrayList<MapPoint> findPath(MapPoint start, MapPoint goal) {


        boolean goalReached = false;
        ArrayList<MapPoint> path = new ArrayList<MapPoint>();
        start.setDistance(0);
        PriorityQueue<MapPoint> toVisit = new PriorityQueue<MapPoint>();
        toVisit.add(start);

        while (!toVisit.isEmpty() && !goalReached) {
            MapPoint p = toVisit.poll();
            if (p.getId() == goal.getId()) {
                goalReached = true;
            }
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

        for (MapPoint m = goal; m != null; m = m.getPrevious()) {
            path.add(m);
        }

        Collections.reverse(path);
        return path;

    }

}
