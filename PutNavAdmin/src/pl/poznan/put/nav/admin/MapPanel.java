package pl.poznan.put.nav.admin;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class MapPanel extends JPanel implements MouseListener, MouseMotionListener {

	private static final long serialVersionUID = -689713982784590342L;

	private Image mapImage = null;
	private Image naviPoint = null;
	private Image doorPoint = null;
	private Image liftPoint = null;
	private Image stairsPoint = null;
	private ArrayList<MapPoint> mapPoints = null;
	private MapPoint activeMapPoint = null;
	private int activeMapPointType = 0;
	private int imagePointWidth;
	private PropertiesPanel propertiesPanel = AppFactory.getPropertiesPanel();
	
	public MapPanel() {
		addMouseListener(this);
		addMouseMotionListener(this);
		
		mapImage = new ImageIcon("images/map.jpg").getImage();
		naviPoint = new ImageIcon("images/navi-point.png").getImage();
		doorPoint = new ImageIcon("images/door-point.png").getImage();
		liftPoint = new ImageIcon("images/lift-point.png").getImage();
		stairsPoint = new ImageIcon("images/stairs-point.png").getImage();
		
		mapPoints = new ArrayList<MapPoint>();
		imagePointWidth = naviPoint.getWidth(this);
	}
	
	private MapPoint addMapPoint(int x, int y) {
		MapPoint point = new MapPoint(x, y, activeMapPointType);
		mapPoints.add(point);
		repaint();
		
		return point;
	}
	
	private MapPoint getClickedMapPoint(int x, int y) {
		for(MapPoint point : mapPoints) {
			if( point.getX() <= x && (point.getX() + imagePointWidth) >= x &&
				point.getY() <= y && (point.getY() + imagePointWidth) >= y )
				return point;
		}
		
		return null;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		g.clearRect(0, 0, this.getWidth(), this.getHeight());
		g.drawImage(mapImage,3,4,this);
		
		for(MapPoint p : mapPoints) {
			int type = p.getType();
			
			if(type == MapPointTypes.NAVIGATION) 
				g.drawImage(naviPoint, p.getX(), p.getY(), this);
			else if(type == MapPointTypes.DOOR) 
				g.drawImage(doorPoint, p.getX(), p.getY(), this);
			else if(type == MapPointTypes.LIFT) 
				g.drawImage(liftPoint, p.getX(), p.getY(), this);
			else if(type == MapPointTypes.STAIRS) 
				g.drawImage(stairsPoint, p.getX(), p.getY(), this);
		}
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		MapPoint clickedMapPoint = getClickedMapPoint(event.getX(), event.getY());
		if(clickedMapPoint == null) {
			clickedMapPoint = addMapPoint(event.getX(), event.getY());
		}
		System.out.println(clickedMapPoint.getType());
		propertiesPanel.setActiveMapPoint(clickedMapPoint);
	}

	@Override
	public void mouseEntered(MouseEvent event) {
		
	}

	@Override
	public void mouseExited(MouseEvent event) {
		
	}

	@Override
	public void mousePressed(MouseEvent event) {
		if(activeMapPoint == null) {
			activeMapPoint = getClickedMapPoint(event.getX(), event.getY());
		}
	}

	@Override
	public void mouseReleased(MouseEvent event) {
		if(activeMapPoint != null) {
			activeMapPoint = null;
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent event) {
		if(activeMapPoint != null) {
			activeMapPoint.move(event.getX(), event.getY());
			propertiesPanel.setActiveMapPoint(activeMapPoint);
			repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent event) {
		
	}
	
	public int getActiveMapPointType() {
		return activeMapPointType;
	}

	public void setActiveMapPointType(int activeMapPointType) {
		this.activeMapPointType = activeMapPointType;
	}
}
