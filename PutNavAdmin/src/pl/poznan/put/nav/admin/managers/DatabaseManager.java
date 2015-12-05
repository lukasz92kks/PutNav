package pl.poznan.put.nav.admin.managers;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;

import pl.poznan.put.nav.admin.entities.Building;
import pl.poznan.put.nav.admin.entities.Department;
import pl.poznan.put.nav.admin.entities.Map;
import pl.poznan.put.nav.admin.entities.MapPoint;
import pl.poznan.put.nav.admin.entities.Room;

public class DatabaseManager implements DatabaseInterface {

	private Connection connection = null;
	private String dbName = "temp/database.db";
	private String dbDriver = "jdbc:sqlite";
	
	public DatabaseManager() {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void connect() {
		try {
			connection = DriverManager.getConnection(dbDriver + ":" + dbName);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void disconnect() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public ArrayList<Building> getAllBuildings() {
		ArrayList<Building> buildings = new ArrayList<Building>();
		try {
			connect();
			PreparedStatement pstmt = connection.prepareStatement("select * from Buildings");
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Building building = new Building(rs.getInt("Id"), rs.getString("Name"), 
									rs.getString("Address"), rs.getInt("NumberOfFloors"));
				building.setImages(getImagesByBuilding(building));
				building.setMaps(getMapsByBuilding(building));
				building.setRooms(getRoomsByBuilding(building));
				//building.setDepartments(departments);
				//building.setMapPoints(mapPoints);
				buildings.add(building);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return buildings;
	}

	@Override
	public Building getBuildingById(int id) {
		Building building = null;
		try {
			connect();
			PreparedStatement pstmt = connection.prepareStatement("select * from Buildings where Id = ?");
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				building = new Building(rs.getInt("Id"), rs.getString("Name"), 
									rs.getString("Address"), rs.getInt("NumberOfFloors"));
				building.setImages(getImagesByBuilding(building));
				building.setMaps(getMapsByBuilding(building));
				building.setRooms(getRoomsByBuilding(building));
				//building.setDepartments(departments);
				//building.setMapPoints(mapPoints);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return building;
	}

	@Override
	public Building getBuildingByMapPoint(MapPoint mapPoint) {
		Building building = null;
		try {
			connect();
			PreparedStatement pstmt = connection.prepareStatement(
					"select Buildings.Id, Buildings.Name, Buildings.Address, Buildings.NumberOfFloors " +
					"from Buildings join MapPoints on Buildings.Id = MapPoints.Building " +
					"where MapPoints.Id = ?");
			pstmt.setInt(1, mapPoint.getId());
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				building = new Building(rs.getInt("Id"), rs.getString("Name"), 
									rs.getString("Address"), rs.getInt("NumberOfFloors"));
				building.setImages(getImagesByBuilding(building));
				building.setMaps(getMapsByBuilding(building));
				building.setRooms(getRoomsByBuilding(building));
				//building.setDepartments(departments);
				//building.setMapPoints(mapPoints);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return building;
	}

	@Override
	public Building getBuildingByName(String name) {
		Building building = null;
		try {
			connect();
			PreparedStatement pstmt = connection.prepareStatement("select * from Buildings where Name = ?");
			pstmt.setString(1, name);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				building = new Building(rs.getInt("Id"), rs.getString("Name"), 
									rs.getString("Address"), rs.getInt("NumberOfFloors"));
				building.setImages(getImagesByBuilding(building));
				building.setMaps(getMapsByBuilding(building));
				building.setRooms(getRoomsByBuilding(building));
				//building.setDepartments(departments);
				//building.setMapPoints(mapPoints);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return building;
	}

	@Override
	public ArrayList<File> getImagesByBuilding(Building building) {
		ArrayList<File> images = new ArrayList<File>();
		try {
			connect();
			PreparedStatement pstmt = connection.prepareStatement("select FileName from Images where Building = ?");
			pstmt.setInt(1, building.getId());
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				File image = new File("temp/maps/" + rs.getString("FileName"));
				images.add(image);
			}
			pstmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return images;
	}

	@Override
	public File getImageById(int id) {
		File image = null;
		try {
			connect();
			PreparedStatement pstmt = connection.prepareStatement("select FileName from Images where id = ?");
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				image = new File("temp/maps/" + rs.getString("FileName"));
			}
			pstmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return image;
	}

	@Override
	public ArrayList<Department> getAllDepartments() {
		ArrayList<Department> departments = new ArrayList<Department>();
		try {
			connect();
			PreparedStatement pstmt = connection.prepareStatement("select * from Departments");
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Department department = new Department(rs.getInt("Id"), rs.getString("Name"));
				//department.setBuildings(buildings);
				departments.add(department);
			}
			pstmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return departments;
	}

	@Override
	public ArrayList<Department> getDepartmentsByBuilding(Building building) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Department getDepartmentById(int id) {
		Department department = null;
		try {
			connect();
			PreparedStatement pstmt = connection.prepareStatement("select * from Departments where id = ?");
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				department = new Department(rs.getInt("Id"), rs.getString("Name"));
				//department.setBuildings(buildings);
			}
			pstmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return department;
	}

	@Override
	public Department getDepartmentByName(String name) {
		Department department = null;
		try {
			connect();
			PreparedStatement pstmt = connection.prepareStatement("select * from Departments where name = ?");
			pstmt.setString(1, name);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				department = new Department(rs.getInt("Id"), rs.getString("Name"));
				//department.setBuildings(buildings);
			}
			pstmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return department;
	}

	@Override
	public ArrayList<Map> getAllMaps() {
		ArrayList<Map> maps = new ArrayList<Map>();
		try {
			connect();
			PreparedStatement pstmt = connection.prepareStatement("select * from Maps");
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Integer floor = rs.getInt("Floor");
				Map map = null;
				
				if(!rs.wasNull()) {
					map = new Map(rs.getInt("Id"), 0, 
						 	 (new File("temp/maps/" + rs.getString("FileName"))));
				} else {
					map = new Map(rs.getInt("Id"), floor, 
							(new File("temp/maps/" + rs.getString("FileName"))));
				}
				map.setMapPoints(getMapPointsByMap(map));
				//map.setBuilding(building);
				maps.add(map);
			}
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return maps;
	}

	@Override
	public ArrayList<Map> getMapsByBuilding(Building building) {
		ArrayList<Map> maps = new ArrayList<Map>();
		try {
			connect();
			PreparedStatement pstmt = connection.prepareStatement(
					"select Maps.Id, Maps.Building, Maps.Floor, Maps.FileName " +
					"from Buildings join Maps on Buildings.Id = Maps.Building " +
					"where Buildings.Id = ?");
			pstmt.setInt(1, building.getId());
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Integer floor = rs.getInt("Floor");
				Map map = null;
				
				if(!rs.wasNull()) {
					map = new Map(rs.getInt("Id"), 0, 
						 	 (new File("temp/maps/" + rs.getString("FileName"))));
				} else {
					map = new Map(rs.getInt("Id"), floor, 
							(new File("temp/maps/" + rs.getString("FileName"))));
				}
				map.setMapPoints(getMapPointsByMap(map));
				//map.setBuilding(building);
				maps.add(map);
			}
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return maps;
	}

	@Override
	public Map getMapByFileName(String fileName) {
		Map map = null;
		try {
			connect();
			PreparedStatement pstmt = connection.prepareStatement("select * from Maps where FileName = ?");
			pstmt.setString(1, fileName);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				Integer floor = rs.getInt("Floor");
				
				if(!rs.wasNull()) {
					map = new Map(rs.getInt("Id"), 0, 
						 	 (new File("temp/maps/" + rs.getString("FileName"))));
				} else {
					map = new Map(rs.getInt("Id"), floor, 
							(new File("temp/maps/" + rs.getString("FileName"))));
				}
				map.setMapPoints(getMapPointsByMap(map));
				//map.setBuilding(building);
			}
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return map;
	}

	@Override
	public ArrayList<MapPoint> getMapPointsByMap(Map map) {
		ArrayList<MapPoint> mapPoints = new ArrayList<MapPoint>();
		try {
			connect();
			PreparedStatement pstmt = connection.prepareStatement("select * from MapPoints where Map = ?");
			pstmt.setInt(1, map.getId());
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()) {
				MapPoint mapPoint = new MapPoint(rs.getInt("Id"), rs.getInt("x"), rs.getInt("y"), 
												 rs.getInt("Type"));
				mapPoint.setMap(map);
				//mapPoint.setBuilding(building);
				//mapPoint.setRoom(room);
				//mapPoint.setSuccessors(successors);
				mapPoints.add(mapPoint);
			}
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return mapPoints;
	}

	@Override
	public ArrayList<Room> getRoomsByBuilding(Building building) {
		ArrayList<Room> rooms = new ArrayList<Room>();
		try {
			connect();
			PreparedStatement pstmt = connection.prepareStatement("select * from Rooms where Building = ?");
			pstmt.setInt(1, building.getId());
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String function = rs.getString("Function");
				if(rs.wasNull()) function = "";
				
				Room room = new Room(rs.getInt("Id"), rs.getString("Name"), function, rs.getInt("Floor"));
				//room.setBuilding(building);
				//room.setMapPoints(mapPoints);
				rooms.add(room);
			}
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return rooms;
	}

	@Override
	public ArrayList<Room> getRoomsByMap(Map map) {
		ArrayList<Room> rooms = new ArrayList<Room>();
		try {
			connect();
			PreparedStatement pstmt = connection.prepareStatement(
					"select Rooms.Id, Rooms.Name, Rooms.Function, Rooms.Floor, Rooms.Building " +
					"from Rooms join Buildings on Rooms.Building = Buildings.Id join Maps on Maps.Building = Buildings.Id " +
					"where Maps.Id = ?");
			pstmt.setInt(1, map.getId());
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				String function = rs.getString("Function");
				if(rs.wasNull()) function = "";
				
				Room room = new Room(rs.getInt("Id"), rs.getString("Name"), function, rs.getInt("Floor"));
				//room.setBuilding(building);
				//room.setMapPoints(mapPoints);
				rooms.add(room);
			}
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return rooms;
	}

	@Override
	public Room getRoomByName(String name) {
		Room room = null;
		try {
			connect();
			PreparedStatement pstmt = connection.prepareStatement("select * from Rooms where Name = ?");
			pstmt.setString(1, name);
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				String function = rs.getString("Function");
				if(rs.wasNull()) function = "";
				
				room = new Room(rs.getInt("Id"), rs.getString("Name"), function, rs.getInt("Floor"));
				//room.setBuilding(building);
				//room.setMapPoints(mapPoints);
			}
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return room;
	}
}
