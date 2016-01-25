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
import java.util.ArrayList;
import java.util.List;
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
import pl.poznan.put.nav.admin.managers.EntitiesManager;

public class MapPanel extends JPanel implements MouseListener, MouseMotionListener {

	private static final long serialVersionUID = -689713982784590342L;
	private static final String mapsPath = "temp/maps/";

	private PropertiesPanel propertiesPanel = AppFactory.getPropertiesPanel();
	private EntitiesManager em = AppFactory.getEntitiesManager();
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
		System.out.println("MapPanel");
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
								 mapPanel.em.getActiveMap().getFloor());
			
			mapPanel.em.getActiveBuilding().getRooms().add(room);
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
			point.setMap(em.getActiveMap());
			if(building != null)
				point.setBuilding(building);
			if(room != null)
				point.setRoom(room);
			em.getActiveMap().addMapPoint(point);
			repaint();
			
			return point;
		}
		return null;
	}
	
	private MapPoint getClickedMapPoint(int x, int y) {
		for(MapPoint point : em.getActiveMap().getMapPoints()) {
			if( point.getX() <= x && (point.getX() + imagePointWidth) >= x &&
				point.getY() <= y && (point.getY() + imagePointWidth) >= y )
				return point;
		}
		
		return null;
	}
	
	public void deleteActiveMapPoint() {
		if(activeMapPoint != null) {
			for(MapPoint p : em.getActiveMap().getMapPoints()) {
				if(p.getSuccessors().contains(activeMapPoint)) {
					p.removeSuccessor(activeMapPoint);
				}
			}
			em.getActiveMap().removeMapPoint(activeMapPoint);
			propertiesPanel.setActiveMapPoint(null);
			repaint();
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		if(em.getActiveMap() != null) {
			if(drawArea == null)
				drawArea = new DrawArea(0, 0, this.getWidth(), this.getHeight(), 0, 0, this.getWidth(), this.getHeight());
			g.clearRect(0, 0, this.getWidth(), this.getHeight());
			g.drawImage(new ImageIcon(mapsPath + em.getActiveMap().getMapFile()).getImage(), drawArea.getxStartDestination(), drawArea.getyStartDestination(),
								  drawArea.getxEndDestination(), drawArea.getyEndDestination(), 
								  drawArea.getxStartSource(), drawArea.getyStartSource(),
								  drawArea.getxEndSource(), drawArea.getyEndSource(), this);
			
			for(MapPoint p : em.getActiveMap().getMapPoints()) {
				int x = p.getX() - drawArea.getxStartSource();
				int y = p.getY() - drawArea.getyStartSource();
				int type = p.getType();
				
				if(type > 0) {
					if(type == MapPointTypes.NAVIGATION && mode != MapPanelModes.EDIT_FLOORS_CONNECTIONS && mode != MapPanelModes.REMOVE_FLOORS_CONNECTIONS)
						g.drawImage(naviPoint, x, y, this);
					else if(type == MapPointTypes.DOOR && mode != MapPanelModes.EDIT_FLOORS_CONNECTIONS && mode != MapPanelModes.REMOVE_FLOORS_CONNECTIONS)
						g.drawImage(doorPoint, x, y, this);
					else if(type == MapPointTypes.OUTDOOR && mode != MapPanelModes.EDIT_FLOORS_CONNECTIONS && mode != MapPanelModes.REMOVE_FLOORS_CONNECTIONS)
						g.drawImage(outdoorPoint, x, y, this);
					else if(type == MapPointTypes.LIFT && (startArc == null || (startArc != null && mode == MapPanelModes.EDIT_POINTS_CONNECTIONS) || (startArc != null && mode == MapPanelModes.EDIT_FLOORS_CONNECTIONS) || (startArc != null && mode == MapPanelModes.REMOVE_FLOORS_CONNECTIONS && startArc.getSuccessors().contains(p)))  )
						g.drawImage(liftPoint, x, y, this);
					else if(type == MapPointTypes.STAIRS && (startArc == null || (startArc != null && mode == MapPanelModes.EDIT_POINTS_CONNECTIONS) || (startArc != null && mode == MapPanelModes.EDIT_FLOORS_CONNECTIONS) || (startArc != null && mode == MapPanelModes.REMOVE_FLOORS_CONNECTIONS && startArc.getSuccessors().contains(p)))  )
						g.drawImage(stairsPoint, x, y, this);
					else if(type == MapPointTypes.BUILDING && mode != MapPanelModes.EDIT_FLOORS_CONNECTIONS && mode != MapPanelModes.REMOVE_FLOORS_CONNECTIONS)
						g.drawImage(buildingPoint, x, y, this);
					else if(type == MapPointTypes.ROOM && mode != MapPanelModes.EDIT_FLOORS_CONNECTIONS && mode != MapPanelModes.REMOVE_FLOORS_CONNECTIONS)
						g.drawImage(roomPoint, x, y, this);
					
					if(p.equals(activeMapPoint)) {
						g.setColor(Color.RED);
						g.drawRect(p.getX()-drawArea.getxStartSource(), p.getY()-drawArea.getyStartSource(), imagePointWidth, imagePointWidth);
					}
				}
				if(!(mode == MapPanelModes.REMOVE_FLOORS_CONNECTIONS || mode == MapPanelModes.EDIT_FLOORS_CONNECTIONS))
					if(p.getSuccessors() != null)
					for(MapPoint successor : p.getSuccessors()) {
						if(p.getMap().equals(successor.getMap()))
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
			if(mode == MapPanelModes.REMOVE_POINTS_CONNECTIONS && startArc == start && endArc == end)
				g2.setColor(Color.RED);
			else
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

	private Map mapToReturn;
	private boolean isAlreadyOneClick = false;
	private MouseEvent eventClick;
	@Override
	public void mouseClicked(MouseEvent event) {
		if(em.getActiveMap() != null) {
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
		        			} else if(mode == MapPanelModes.EDIT_FLOORS_CONNECTIONS || mode == MapPanelModes.REMOVE_FLOORS_CONNECTIONS) {
		        				
		        				activeMapPoint = getClickedMapPoint(eventClick.getX()+drawArea.getxStartSource(), eventClick.getY()+drawArea.getyStartSource());
		        				if(activeMapPoint != null && 
		        				  (activeMapPoint.getType() == MapPointTypes.STAIRS || activeMapPoint.getType() == MapPointTypes.LIFT)) {
		        					
		        					if(startArc != null) {
		        						
		        						MapPoint endPoint = activeMapPoint;
		        						if(mode == MapPanelModes.EDIT_FLOORS_CONNECTIONS) {
			        						if(endPoint != null && !startArc.equals(endPoint) && !startArc.getSuccessors().contains(endPoint)) {
			        							
		        								startArc.addSuccessor(endPoint);
		        								endPoint.addSuccessor(startArc);
			        							
			        							startArc = null;
			        							em.setActiveMap(mapToReturn);
			        						}
		        						} else if(mode == MapPanelModes.REMOVE_FLOORS_CONNECTIONS) {
		        							if(endPoint != null && !startArc.equals(endPoint) && startArc.getSuccessors().contains(endPoint)) {
			        							
		        								startArc.removeSuccessor(endPoint);
		        								endPoint.removeSuccessor(startArc);
			        							
			        							startArc = null;
			        							em.setActiveMap(mapToReturn);
			        						}
		        						}
		        					} else {
			        					List<String> mapNames = new ArrayList<String>();
			        					for(Map map : em.getActiveBuilding().getMaps()) {
			        						if(!map.equals(em.getActiveMap())) {
			        							mapNames.add(map.getMapFile());
			        						}
			        					}
			        					
			        					String result = null;
			        					if(mode == MapPanelModes.EDIT_FLOORS_CONNECTIONS) {
			        					result = (String) JOptionPane.showInputDialog(null, 
			        					        "Wybierz mape do polaczenia:", "Polacz pietra",
			        					        JOptionPane.QUESTION_MESSAGE, 
			        					        new ImageIcon("images/arrowfloor.png"), 
			        					        mapNames.toArray(), mapNames.get(0));
			        					} else if(mode == MapPanelModes.REMOVE_FLOORS_CONNECTIONS) {
			        						result = (String) JOptionPane.showInputDialog(null, 
				        					        "Wybierz mape do usuniecia polaczenia:", "Usun polaczenie pieter",
				        					        JOptionPane.QUESTION_MESSAGE, 
				        					        new ImageIcon("images/delete-arrowfloor.png"), 
				        					        mapNames.toArray(), mapNames.get(0));
			        					}
			        					
			        					if(result != null) {
			        						startArc = activeMapPoint;
			        						for(Map map : em.getActiveBuilding().getMaps()) {
				        						if(map.getMapFile().equals(result)) {
				        							mapToReturn = em.getActiveMap();
				        							em.setActiveMap(map);
				        							break;
				        						}
				        					}
			        					}
		        					}
		        				}
		        			} else if(mode == MapPanelModes.REMOVE_FLOORS_CONNECTIONS) {
		        				
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
		if(em.getActiveMap() != null) {
			pressedPoint = new Point(event.getX(), event.getY());
			if(movableMapPoint == null) {
				movableMapPoint = getClickedMapPoint(event.getX()+drawArea.getxStartSource(), event.getY()+drawArea.getyStartSource());
				activeMapPoint = movableMapPoint;
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent event) {
		if(em.getActiveMap() != null) {
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
				} else if(mode == MapPanelModes.REMOVE_POINTS_CONNECTIONS) {
					MapPoint endPoint = getClickedMapPoint(event.getX()+drawArea.getxStartSource(), event.getY()+drawArea.getyStartSource());
					if(endPoint != null && !movableMapPoint.equals(endPoint) && movableMapPoint.getSuccessors().contains(endPoint)) {
						movableMapPoint.removeSuccessor(endPoint);
						endPoint.removeSuccessor(movableMapPoint);
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
		if(em.getActiveMap() != null) {
			if(movableMapPoint != null) {
				if(mode == MapPanelModes.EDIT_POINTS) {
					movableMapPoint.move(event.getX()+drawArea.getxStartSource(), event.getY()+drawArea.getyStartSource());
					propertiesPanel.setActiveMapPoint(movableMapPoint);
				} else if(mode == MapPanelModes.EDIT_POINTS_CONNECTIONS || mode == MapPanelModes.REMOVE_POINTS_CONNECTIONS) {
					startArc = movableMapPoint;
					endArc = new Point(event.getX()+drawArea.getxStartSource(), event.getY()+drawArea.getyStartSource());
				} else if(mode == MapPanelModes.EDIT_FLOORS_CONNECTIONS) {
					
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
	
	public void clearStartArc() {
		startArc = null;
	}
	
	public void refresh() {
		repaint();
	}
	
	private void recalculateDrawArea(int cursorX, int cursorY) {
		int diffrentX = pressedPoint.x - cursorX;
		int diffrentY = pressedPoint.y - cursorY;
		int newxStart = drawArea.getxStartSource() + (int)(0.1*diffrentX);
		int newyStart = drawArea.getyStartSource() + (int)(0.1*diffrentY);
		int newxEnd = drawArea.getxEndSource() + (int)(0.1*diffrentX);
		int newyEnd = drawArea.getyEndSource() + (int)(0.1*diffrentY);
		
		Image mapImage = new ImageIcon(mapsPath + em.getActiveMap().getMapFile()).getImage();
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
