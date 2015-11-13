package pl.poznan.put.nav.admin;

import java.awt.Image;
import java.util.ArrayList;

public class Map {

	private Image mapImage = null;
	private ArrayList<MapPoint> mapPoints = null;
	
	public Map() {
		setMapPoints(new ArrayList<MapPoint>());
	}
	
	public Map(Image mapImage) {
		this.mapImage = mapImage;
		setMapPoints(new ArrayList<MapPoint>());
	}
	
	public MapPoint addMapPoint(MapPoint point) {
		getMapPoints().add(point);
		return point;
	}
	
	public void removeMapPoint(MapPoint point) {
		mapPoints.remove(point);
	}

	public Image getMapImage() {
		return mapImage;
	}

	public void setMapImage(Image mapImage) {
		this.mapImage = mapImage;
	}

	public ArrayList<MapPoint> getMapPoints() {
		return mapPoints;
	}

	public void setMapPoints(ArrayList<MapPoint> mapPoints) {
		this.mapPoints = mapPoints;
	}
}
