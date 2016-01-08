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
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import pl.poznan.put.nav.admin.entities.Building;
import pl.poznan.put.nav.admin.entities.Map;
import pl.poznan.put.nav.admin.entities.MapPoint;
import pl.poznan.put.nav.admin.entities.MapPointTypes;
import pl.poznan.put.nav.admin.entities.MapPointsArcs;
import pl.poznan.put.nav.admin.entities.Room;
import pl.poznan.put.nav.admin.managers.AppFactory;

public class MapPanel extends JPanel implements MouseListener, MouseMotionListener {

	private static final long serialVersionUID = -689713982784590342L;
	private static final String mapsPath = "temp/maps/";

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
	
	private Building addBuilding() {
		PropertiesPanel panel = new PropertiesPanel();
		int result = JOptionPane.showConfirmDialog(null, panel.createBuildingBox(), 
	               "Dodaj budynek", JOptionPane.OK_CANCEL_OPTION, 1, new ImageIcon("images/building.png"));
		
		if(result == JOptionPane.YES_OPTION) {
			Building building = new Building(panel.getBuildingNameTextField().getText(),
											 panel.getAddressTextField().getText(),
											 Integer.parseInt(panel.getNumOfFloorsTextField().getText()));
			propertiesPanel.getBuildings().add(building);
			return building;
		}
		else
			return null;
	}
	
	private Room addRoom() {
		PropertiesPanel panel = new PropertiesPanel();
		int result = JOptionPane.showConfirmDialog(null, panel.createRoomBox(), 
	               "Dodaj pomieszczenie", JOptionPane.OK_CANCEL_OPTION, 1, new ImageIcon("images/room.png"));
		
		if(result == JOptionPane.YES_OPTION) {
			MapPanel mapPanel = AppFactory.getMapPanel();
			
			Room room = new Room(panel.getRoomNameTextField().getText(),
								 panel.getFunctionTextField().getText(),
								 mapPanel.getMap().getFloor());
			
			mapPanel.getMap().getBuilding().getRooms().add(room);
			return room;
		}
		else
			return null;
	}
	
	private MapPoint addMapPoint(int x, int y) {
		boolean addPoint = false;
		Building building = null;
		Room room = null;
		
		if (activeAddMapPointType == MapPointTypes.BUILDING) {
			building = addBuilding();
		} else if (activeAddMapPointType == MapPointTypes.ROOM) {
			room = addRoom();
		} else {
			addPoint = true;
		}
		
		if(addPoint || building != null || room != null) {
			MapPoint point = new MapPoint();
			point.setX(x);
			point.setY(y);
			point.setType(activeAddMapPointType);
			point.setMap(map);
			if(building != null)
				point.setBuilding(building);
			if(room != null)
				point.setRoom(room);
			getMap().addMapPoint(point);
			repaint();
			
			return point;
		}
		return null;
	}
	
	private MapPoint getClickedMapPoint(int x, int y) {
		for(MapPoint point : getMap().getMapPoints()) {
			if( point.getX() <= x && (point.getX() + imagePointWidth) >= x &&
				point.getY() <= y && (point.getY() + imagePointWidth) >= y )
				return point;
		}
		
		return null;
	}
	
	private MapPointsArcs getClickedMapPointsArc(int x, int y) {
		double wrongTouch = 0.03;
		for(MapPoint p : map.getMapPoints()) {
			for(MapPoint s : p.getSuccessors()) {
				Point A = new Point(p.getX() + imagePointWidth / 2, p.getY() + imagePointWidth / 2);
				Point B = new Point(s.getX() + imagePointWidth / 2, s.getY() + imagePointWidth / 2);
				Point C = new Point(x, y);
				
				// wspolczynniki
				double wspAB = (double)(B.y - A.y) / (double)(B.x - A.x);
				double wspAC = (double)(C.y - A.y) / (double)(C.x - A.x);
				
				System.out.println("A(" + A.x + "," + A.y + ") " +
						" B(" + B.x + "," + B.y + ") " +
						" C(" + C.x + "," + C.y + ") ");
				System.out.println("a: " + wspAB + "   b: " + wspAC);
				
				wrongTouch = wrongTouch * wspAB;
				// czy punkt lezy na prostej
				if( (wspAB >= wspAC-wrongTouch && wspAB <= wspAC+wrongTouch) ||
					(wspAC >= wspAB-wrongTouch && wspAC <= wspAB+wrongTouch) ){
					MapPointsArcs arc = new MapPointsArcs(p, s);
					return arc;
				}
			}
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
			g.drawImage(new ImageIcon(mapsPath + getMap().getMapFile()).getImage(), drawArea.getxStartDestination(), drawArea.getyStartDestination(),
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
				if(p.getSuccessors() != null)
				for(MapPoint successor : p.getSuccessors()) {
					drawArc(g, p, successor);
				}
			}
			// draw connection arc
			if(startArc != null && endArc != null) {
				drawArc(g, startArc, endArc);
			}
		}
	}
	
	private void drawArc(Graphics g, MapPoint start, MapPoint end) {
		if(start != null && end != null) 
			drawArc(g, start, new Point(end.getX() + imagePointWidth/2, end.getY() + imagePointWidth/2));
	}
	
	private void drawArc(Graphics g, MapPoint start, Point end) {
		if(start != null && end != null) {
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
		        					MapPointsArcs arc = null;//getClickedMapPointsArc(eventClick.getX()+drawArea.getxStartSource(), eventClick.getY()+drawArea.getyStartSource());
		        					if(arc != null) {
		        						System.out.println("Klikniete polaczenie");
		        					} else {
		        						activeMapPoint = addMapPoint(eventClick.getX()+drawArea.getxStartSource(), eventClick.getY()+drawArea.getyStartSource());
		        					}
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
					endArc = null;
					startArc = null;
					repaint();
				}
				movableMapPoint = null;
			}
		}
	}
	
	private MapPoint startArc;
	private Point endArc;
	@Override
	public void mouseDragged(MouseEvent event) {
		if(map != null) {
			if(movableMapPoint != null) {
				if(mode == MapPanelModes.EDIT_POINTS) {
					movableMapPoint.move(event.getX()+drawArea.getxStartSource(), event.getY()+drawArea.getyStartSource());
					propertiesPanel.setActiveMapPoint(movableMapPoint);
				} else {
					startArc = movableMapPoint;
					endArc = new Point(event.getX()+drawArea.getxStartSource(), event.getY()+drawArea.getyStartSource());
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
		
		Image mapImage = new ImageIcon(mapsPath + getMap().getMapFile()).getImage();
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
