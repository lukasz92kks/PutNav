package pl.poznan.put.nav.admin.entities;

//@Entity
public class MapPointsArcs {

	//@Id
	//@GeneratedValue(strategy = GenerationType.TABLE)
	private int id;
	private MapPoint from;
	private MapPoint to;
	
	public MapPointsArcs() {}
	
	public MapPointsArcs(MapPoint from, MapPoint to) {
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
