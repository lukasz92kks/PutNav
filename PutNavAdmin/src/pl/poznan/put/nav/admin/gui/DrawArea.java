package pl.poznan.put.nav.admin.gui;

public class DrawArea {

	private int xStartSource;
	private int yStartSource;
	
	private int xEndSource;
	private int yEndSource;
	
	
	private int xStartDestination;
	private int yStartDestination;
	
	private int xEndDestination;
	private int yEndDestination;
	
	
	public DrawArea() {}
	
	public DrawArea(int xss, int yss, int xes, int yes, int xsd, int ysd, int xed, int yed) {
		setArea(xss, yss, xes, yes, xsd, ysd, xed, yed);
	}
	
	public void setArea(int xss, int yss, int xes, int yes, int xsd, int ysd, int xed, int yed) {
		this.xStartSource = xss;
		this.yStartSource = yss;
		this.xEndSource = xes;
		this.yEndSource = yes;
		this.xStartDestination = xsd;
		this.yStartDestination = ysd;
		this.xEndDestination = xed;
		this.yEndDestination = yed;
	}
	
	public int getxStartSource() {
		return xStartSource;
	}
	
	public void setxStartSource(int xStartSource) {
		this.xStartSource = xStartSource;
	}
	
	public int getyStartSource() {
		return yStartSource;
	}
	
	public void setyStartSource(int yStartSource) {
		this.yStartSource = yStartSource;
	}
	
	public int getxEndSource() {
		return xEndSource;
	}
	
	public void setxEndSource(int xEndSource) {
		this.xEndSource = xEndSource;
	}
	
	public int getyEndSource() {
		return yEndSource;
	}
	
	public void setyEndSource(int yEndSource) {
		this.yEndSource = yEndSource;
	}
	
	public int getxStartDestination() {
		return xStartDestination;
	}
	
	public void setxStartDestination(int xStartDestination) {
		this.xStartDestination = xStartDestination;
	}
	
	public int getyStartDestination() {
		return yStartDestination;
	}
	
	public void setyStartDestination(int yStartDestination) {
		this.yStartDestination = yStartDestination;
	}
	
	public int getxEndDestination() {
		return xEndDestination;
	}
	
	public void setxEndDestination(int xEndDestination) {
		this.xEndDestination = xEndDestination;
	}
	
	public int getyEndDestination() {
		return yEndDestination;
	}
	
	public void setyEndDestination(int yEndDestination) {
		this.yEndDestination = yEndDestination;
	}
}
