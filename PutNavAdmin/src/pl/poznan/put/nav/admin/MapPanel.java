package pl.poznan.put.nav.admin;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class MapPanel extends JPanel implements MouseListener, MouseMotionListener {

	private static final long serialVersionUID = -689713982784590342L;

	private PropertiesPanel propertiesPanel = AppFactory.getPropertiesPanel();
	private Image buildingPoint = null;
	private Image naviPoint = null;
	private Image doorPoint = null;
	private Image liftPoint = null;
	private Image stairsPoint = null;
	private Map map;
	private Map campusMap;
	private Map btMap;
	private MapPoint movableMapPoint = null;	// for drag and drop
	private MapPoint activeMapPoint = null;		// selected map point
	private int activeAddMapPointType = 0;		// for adding new point
	private int imagePointWidth;				// image point size - 1 dimension
	private Point pressedPoint;
	private DrawArea drawArea;
	
	public MapPanel() {
		addMouseListener(this);
		addMouseMotionListener(this);
		
		btMap = new Map((new ImageIcon("images/bt_1_pietro.png")).getImage());
		campusMap = new Map((new ImageIcon("images/kampus.png")).getImage());
		setMap(btMap);
		buildingPoint = new ImageIcon("images/building.png").getImage(); 
		naviPoint = new ImageIcon("images/navi.png").getImage();
		doorPoint = new ImageIcon("images/door.png").getImage();
		liftPoint = new ImageIcon("images/lift.png").getImage();
		stairsPoint = new ImageIcon("images/stairs.png").getImage();
		
		imagePointWidth = naviPoint.getWidth(this);
	}
	
	private MapPoint addMapPoint(int x, int y) {
		MapPoint point = new MapPoint(x, y, activeAddMapPointType);
		getMap().addMapPoint(point);
		repaint();
		
		return point;
	}
	
	private MapPoint getClickedMapPoint(int x, int y) {
		for(MapPoint point : getMap().getMapPoints()) {
			if( point.getX() <= x && (point.getX() + imagePointWidth) >= x &&
				point.getY() <= y && (point.getY() + imagePointWidth) >= y )
				return point;
		}
		
		return null;
	}
	
	public void deleteActiveMapPoint() {
		if(activeMapPoint != null) {
			getMap().removeMapPoint(activeMapPoint);
			propertiesPanel.setActiveMapPoint(null);
			repaint();
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		if(drawArea == null)
			drawArea = new DrawArea(0, 0, this.getWidth(), this.getHeight(), 0, 0, this.getWidth(), this.getHeight());
		g.clearRect(0, 0, this.getWidth(), this.getHeight());
		g.drawImage(getMap().getMapImage(), drawArea.getxStartDestination(), drawArea.getyStartDestination(),
							  drawArea.getxEndDestination(), drawArea.getyEndDestination(), 
							  drawArea.getxStartSource(), drawArea.getyStartSource(),
							  drawArea.getxEndSource(), drawArea.getyEndSource(), this);
		
		for(MapPoint p : getMap().getMapPoints()) {
			int x = p.getX() - drawArea.getxStartSource();
			int y = p.getY() - drawArea.getyStartSource();
			int type = p.getType();
			
			if(type > 0) {
				if(type == MapPointTypes.NAVIGATION) 
					g.drawImage(naviPoint, x, y, this);
				else if(type == MapPointTypes.DOOR) 
					g.drawImage(doorPoint, x, y, this);
				else if(type == MapPointTypes.LIFT) 
					g.drawImage(liftPoint, x, y, this);
				else if(type == MapPointTypes.STAIRS) 
					g.drawImage(stairsPoint, x, y, this);
				else if(type == MapPointTypes.BUILDING)
					g.drawImage(buildingPoint, x, y, this);
				
				if(p.equals(activeMapPoint)) {
					g.setColor(Color.RED);
					g.drawRect(p.getX()-drawArea.getxStartSource(), p.getY()-drawArea.getyStartSource(), imagePointWidth, imagePointWidth);
				}
			}
		}
	}

	private boolean isAlreadyOneClick = false;
	private MouseEvent eventClick;
	@Override
	public void mouseClicked(MouseEvent event) {
		if (isAlreadyOneClick) {
			if(activeMapPoint != null) {
				map = map.equals(campusMap) ? btMap : campusMap;
				drawArea = null;
		        isAlreadyOneClick = false;
			}
	    } else {
	        isAlreadyOneClick = true;
	        eventClick = event;
	        Timer t = new Timer("doubleclickTimer", false);
	        t.schedule(new TimerTask() {

	            @Override
	            public void run() {
	            	if (isAlreadyOneClick) {
	            		isAlreadyOneClick = false;
	        			activeMapPoint = getClickedMapPoint(eventClick.getX()+drawArea.getxStartSource(), eventClick.getY()+drawArea.getyStartSource());
	        			if(activeMapPoint == null) {
	        				activeMapPoint = addMapPoint(eventClick.getX()+drawArea.getxStartSource(), eventClick.getY()+drawArea.getyStartSource());
	        			}
	        			propertiesPanel.setActiveMapPoint(activeMapPoint);
	            	}
	            }
	        }, 500);
	    }
		
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
			movableMapPoint = getClickedMapPoint(event.getX()+drawArea.getxStartSource(), event.getY()+drawArea.getyStartSource());
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
			movableMapPoint.move(event.getX()+drawArea.getxStartSource(), event.getY()+drawArea.getyStartSource());
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
		
		if(newxStart >= 0 && newxEnd <= getMap().getMapImage().getWidth(this)) {
			drawArea.setxStartSource(newxStart);
			drawArea.setxEndSource(newxEnd);
		}
		if(newyStart >= 0 && newyEnd <= getMap().getMapImage().getHeight(this)) {
			drawArea.setyStartSource(newyStart);
			drawArea.setyEndSource(newyEnd);
		}
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
		//ActionsPanel actionsPanel = AppFactory.getActionsPanel();
		//actionsPanel.setActiveMap(map);
	}
}
