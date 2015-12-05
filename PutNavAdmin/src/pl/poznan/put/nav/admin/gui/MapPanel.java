package pl.poznan.put.nav.admin.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import pl.poznan.put.nav.admin.entities.Map;
import pl.poznan.put.nav.admin.entities.MapPoint;
import pl.poznan.put.nav.admin.entities.MapPointTypes;
import pl.poznan.put.nav.admin.managers.AppFactory;

public class MapPanel extends JPanel implements MouseListener, MouseMotionListener {

	private static final long serialVersionUID = -689713982784590342L;

	private PropertiesPanel propertiesPanel = AppFactory.getPropertiesPanel();
	private Image buildingPoint = null;
	private Image roomPoint = null;
	private Image naviPoint = null;
	private Image doorPoint = null;
	private Image outdoorPoint = null;
	private Image liftPoint = null;
	private Image stairsPoint = null;
	private Map map;
	private MapPoint movableMapPoint = null;	// for drag and drop
	private MapPoint activeMapPoint = null;		// selected map point
	private int activeAddMapPointType = 0;		// for adding new point
	private int imagePointWidth;				// image point size - 1 dimension
	private Point pressedPoint;
	private DrawArea drawArea;
	private int mode = MapPanelModes.EDIT_POINTS;
	
	public MapPanel() {
		addMouseListener(this);
		addMouseMotionListener(this);
		
		buildingPoint = new ImageIcon("images/building.png").getImage();
		roomPoint = new ImageIcon("images/room.png").getImage();
		naviPoint = new ImageIcon("images/navi.png").getImage();
		doorPoint = new ImageIcon("images/door.png").getImage();
		outdoorPoint = new ImageIcon("images/outdoor.png").getImage();
		liftPoint = new ImageIcon("images/lift.png").getImage();
		stairsPoint = new ImageIcon("images/stairs.png").getImage();
		
		imagePointWidth = naviPoint.getWidth(this);
	}
	
	private MapPoint addMapPoint(int x, int y) {
		MapPoint point = new MapPoint(1, x, y, activeAddMapPointType);
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
			for(MapPoint p : map.getMapPoints()) {
				if(p.getSuccessors().contains(activeMapPoint)) {
					p.removeSuccessor(activeMapPoint);
				}
			}
			getMap().removeMapPoint(activeMapPoint);
			propertiesPanel.setActiveMapPoint(null);
			repaint();
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		if(map != null) {
			if(drawArea == null)
				drawArea = new DrawArea(0, 0, this.getWidth(), this.getHeight(), 0, 0, this.getWidth(), this.getHeight());
			g.clearRect(0, 0, this.getWidth(), this.getHeight());
			g.drawImage(new ImageIcon(getMap().getMapFile().getAbsolutePath()).getImage(), drawArea.getxStartDestination(), drawArea.getyStartDestination(),
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
					else if(type == MapPointTypes.OUTDOOR)
						g.drawImage(outdoorPoint, x, y, this);
					else if(type == MapPointTypes.LIFT)
						g.drawImage(liftPoint, x, y, this);
					else if(type == MapPointTypes.STAIRS)
						g.drawImage(stairsPoint, x, y, this);
					else if(type == MapPointTypes.BUILDING)
						g.drawImage(buildingPoint, x, y, this);
					else if(type == MapPointTypes.ROOM)
						g.drawImage(roomPoint, x, y, this);
					
					if(p.equals(activeMapPoint)) {
						g.setColor(Color.RED);
						g.drawRect(p.getX()-drawArea.getxStartSource(), p.getY()-drawArea.getyStartSource(), imagePointWidth, imagePointWidth);
					}
				}
				for(MapPoint successor : p.getSuccessors()) {
					drawArrow(g, p, successor);
				}
			}
			// draw connection arrow
			if(startArrow != null && endArrow != null) {
				drawArrow(g, startArrow, endArrow);
			}
		}
	}
	
	private void drawArrow(Graphics g, MapPoint start, MapPoint end) {
		drawArrow(g, start, new Point(end.getX() + imagePointWidth/2, end.getY() + imagePointWidth/2));
	}
	
	private void drawArrow(Graphics g, MapPoint start, Point end) {
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke(3));
		
		int startx = start.getX() + imagePointWidth/2 - drawArea.getxStartSource();
		int starty = start.getY() + imagePointWidth/2 - drawArea.getyStartSource();
		int endx = end.x - drawArea.getxStartSource();
		int endy = end.y - drawArea.getyStartSource();
		g2.drawLine(startx, starty, endx, endy);
		g2.setStroke(new BasicStroke(1));
	}

	private boolean isAlreadyOneClick = false;
	private MouseEvent eventClick;
	@Override
	public void mouseClicked(MouseEvent event) {
		if(map != null) {
			if (isAlreadyOneClick) {
				if(activeMapPoint != null) {
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
		        			
		            		if(mode == MapPanelModes.EDIT_POINTS) {
		        				activeMapPoint = getClickedMapPoint(eventClick.getX()+drawArea.getxStartSource(), eventClick.getY()+drawArea.getyStartSource());
		        				if(activeMapPoint == null) {
		        					activeMapPoint = addMapPoint(eventClick.getX()+drawArea.getxStartSource(), eventClick.getY()+drawArea.getyStartSource());
		        				}
		        				propertiesPanel.setActiveMapPoint(activeMapPoint);
		        			}
		            	}
		            }
		        }, 500);
		    }
			
			repaint();
		}
	}

	@Override
	public void mouseEntered(MouseEvent event) {
		
	}

	@Override
	public void mouseExited(MouseEvent event) {
		
	}

	@Override
	public void mousePressed(MouseEvent event) {
		if(map != null) {
			pressedPoint = new Point(event.getX(), event.getY());
			if(movableMapPoint == null) {
				movableMapPoint = getClickedMapPoint(event.getX()+drawArea.getxStartSource(), event.getY()+drawArea.getyStartSource());
				activeMapPoint = movableMapPoint;
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent event) {
		if(map != null) {
			if(movableMapPoint != null) {
				if(mode == MapPanelModes.EDIT_POINTS_CONNECTIONS) {
					MapPoint endPoint = getClickedMapPoint(event.getX()+drawArea.getxStartSource(), event.getY()+drawArea.getyStartSource());
					if(endPoint != null && !movableMapPoint.equals(endPoint) && !movableMapPoint.getSuccessors().contains(endPoint)) {
						movableMapPoint.addSuccessor(endPoint);
						endPoint.addSuccessor(movableMapPoint);
						repaint();
					}
					endArrow = null;
					startArrow = null;
					repaint();
				}
				movableMapPoint = null;
			}
		}
	}
	
	private MapPoint startArrow;
	private Point endArrow;
	@Override
	public void mouseDragged(MouseEvent event) {
		if(map != null) {
			if(movableMapPoint != null) {
				if(mode == MapPanelModes.EDIT_POINTS) {
					movableMapPoint.move(event.getX()+drawArea.getxStartSource(), event.getY()+drawArea.getyStartSource());
					propertiesPanel.setActiveMapPoint(movableMapPoint);
				} else {
					startArrow = movableMapPoint;
					endArrow = new Point(event.getX()+drawArea.getxStartSource(), event.getY()+drawArea.getyStartSource());
				}
				repaint();
				
			} else {
				recalculateDrawArea(event.getX(), event.getY());
				repaint();
			}
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
	
	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}
	
	private void recalculateDrawArea(int cursorX, int cursorY) {
		int diffrentX = pressedPoint.x - cursorX;
		int diffrentY = pressedPoint.y - cursorY;
		int newxStart = drawArea.getxStartSource() + (int)(0.1*diffrentX);
		int newyStart = drawArea.getyStartSource() + (int)(0.1*diffrentY);
		int newxEnd = drawArea.getxEndSource() + (int)(0.1*diffrentX);
		int newyEnd = drawArea.getyEndSource() + (int)(0.1*diffrentY);
		
		Image mapImage = new ImageIcon(getMap().getMapFile().getAbsolutePath()).getImage();
		if(newxStart >= 0 && newxEnd <= mapImage.getWidth(this)) {
			drawArea.setxStartSource(newxStart);
			drawArea.setxEndSource(newxEnd);
		}
		if(newyStart >= 0 && newyEnd <= mapImage.getHeight(this)) {
			drawArea.setyStartSource(newyStart);
			drawArea.setyEndSource(newyEnd);
		}
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
		drawArea = null;
		repaint();
	}
}
