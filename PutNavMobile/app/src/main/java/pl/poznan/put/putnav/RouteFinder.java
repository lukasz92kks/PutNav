package pl.poznan.put.putnav;

import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;

public class RouteFinder {

    ArrayList<MapPoint> findPath(MapPoint start, MapPoint goal) {

        ArrayList<MapPoint> path = new ArrayList<MapPoint>();
        start.setDistance(0);
        PriorityQueue<MapPoint> toVisit = new PriorityQueue<MapPoint>();
        toVisit.add(goal);

        while (!toVisit.isEmpty()) {
            MapPoint p = toVisit.poll();
            for (MapPointsArcs currentEdge : p.getEdges()) {
                double newDistance = p.getDistance() + currentEdge.getWeight();
                if (newDistance < currentEdge.getTo().getDistance()) {
                    MapPoint m = currentEdge.getTo();
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