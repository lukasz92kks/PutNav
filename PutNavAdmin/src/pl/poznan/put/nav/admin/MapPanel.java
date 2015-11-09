package pl.poznan.put.nav.admin;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class MapPanel extends JPanel implements MouseListener, MouseMotionListener {

	private static final long serialVersionUID = -689713982784590342L;

	private PropertiesPanel propertiesPanel = AppFactory.getPropertiesPanel();
	private Image mapImage = null;
	private Image naviPoint = null;
	private Image doorPoint = null;
	private Image liftPoint = null;
	private Image stairsPoint = null;
	private ArrayList<MapPoint> mapPoints = null;
	private MapPoint movableMapPoint = null;	// for drag and drop
	private MapPoint activeMapPoint = null;		// selected map point
	private int activeAddMapPointType = 0;		// for adding new point
	private int imagePointWidth;				// image point size - 1 dimension
	private Point pressedPoint;
	private DrawArea drawArea;
	
	public MapPanel() {
		addMouseListener(this);
		addMouseMotionListener(this);
				
		mapImage = new ImageIcon("images/bt_1_pietro.png").getImage();
		naviPoint = new ImageIcon("images/navi.png").getImage();
		doorPoint = new ImageIcon("images/door.png").getImage();
		liftPoint = new ImageIcon("images/lift.png").getImage();
		stairsPoint = new ImageIcon("images/stairs.png").getImage();
		
		mapPoints = new ArrayList<MapPoint>();
		imagePointWidth = naviPoint.getWidth(this);
	}
	
	private MapPoint addMapPoint(int x, int y) {
		MapPoint point = new MapPoint(x, y, activeAddMapPointType);
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
		if(drawArea == null)
			drawArea = new DrawArea(0, 0, this.getWidth(), this.getHeight(), 0, 0, this.getWidth(), this.getHeight());
		g.clearRect(0, 0, this.getWidth(), this.getHeight());
		g.drawImage(mapImage, drawArea.getxStartDestination(), drawArea.getyStartDestination(),
							  drawArea.getxEndDestination(), drawArea.getyEndDestination(), 
							  drawArea.getxStartSource(), drawArea.getyStartSource(),
							  drawArea.getxEndSource(), drawArea.getyEndSource(), this);
		
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
			
			if(p.equals(activeMapPoint)) {
				g.setColor(Color.RED);
				g.drawRect(p.getX(), p.getY(), imagePointWidth, imagePointWidth);
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent event) {
		activeMapPoint = getClickedMapPoint(event.getX(), event.getY());
		if(activeMapPoint == null) {
			activeMapPoint = addMapPoint(event.getX(), event.getY());
		}
		System.out.println(activeMapPoint.getType());
		propertiesPanel.setActiveMapPoint(activeMapPoint);
		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent event) {
		
	}

	@Override
	public void mouseExited(MouseEvent event) {
		
	}

	@Override
	public void mousePressed(MouseEvent event) {
		pressedPoint = new Point(event.getX(), event.getY());
		if(movableMapPoint == null) {
			movableMapPoint = getClickedMapPoint(event.getX(), event.getY());
			activeMapPoint = movableMapPoint;
		}
	}

	@Override
	public void mouseReleased(MouseEvent event) {
		if(movableMapPoint != null) {
			movableMapPoint = null;
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent event) {
		if(movableMapPoint != null) {
			movableMapPoint.move(event.getX(), event.getY());
			propertiesPanel.setActiveMapPoint(movableMapPoint);
			repaint();
			
		} else {
			recalculateDrawArea(event.getX(), event.getY());
			repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent event) {
		
	}
	
	public int getActiveAddMapPointType() {
		return activeAddMapPointType;
	}

	public void setActiveAddMapPointType(int activeAddMapPointType) {
		this.activeAddMapPointType = activeAddMapPointType;
	}
	
	private void recalculateDrawArea(int cursorX, int cursorY) {
		int diffrentX = pressedPoint.x - cursorX;
		int diffrentY = pressedPoint.y - cursorY;
		int newxStart = drawArea.getxStartSource() + (int)(0.1*diffrentX);
		int newyStart = drawArea.getyStartSource() + (int)(0.1*diffrentY);
		int newxEnd = drawArea.getxEndSource() + (int)(0.1*diffrentX);
		int newyEnd = drawArea.getyEndSource() + (int)(0.1*diffrentY);
		
		if(newxStart >= 0 && newxEnd <= mapImage.getWidth(this)) {
			drawArea.setxStartSource(newxStart);
			drawArea.setxEndSource(newxEnd);
			for(MapPoint p : mapPoints) {
				p.setX(p.getX() - (int)(0.1*diffrentX));
			}
		}
		if(newyStart >= 0 && newyEnd <= mapImage.getHeight(this)) {
			drawArea.setyStartSource(newyStart);
			drawArea.setyEndSource(newyEnd);
			for(MapPoint p : mapPoints) {
				p.setY(p.getY() - (int)(0.1*diffrentY));
			}
		}
	}
}
