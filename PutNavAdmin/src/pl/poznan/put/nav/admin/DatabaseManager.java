package pl.poznan.put.nav.admin;

import java.awt.Image;
import java.sql.*;
import java.util.ArrayList;

public class DatabaseManager implements DatabaseInterface {

	private Connection connection = null;
	private Statement stmt = null;
	private String dbName = "temp\\database.db";
	private String dbDriver = "jdbc:sqlite";
	
	public DatabaseManager() {
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("finally")
	public ArrayList<MapPoint> getAllMapPoints() {
		try {
			connect();
			stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("select * from MapPoints");
			while(rs.next()) {
				System.out.println(rs.getInt(2));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
			return null;
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
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public ArrayList<Building> getAllBuildings() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Building getBuildingById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Building getBuildingByMapPoint(MapPoint mapPoint) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Building getBuildingByMapPoint(int x, int y) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Building getBuildingByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Image> getImagesByBuilding(Building building) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Image getImageById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Image getImageByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Department> getAllDepartments() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Department> getDepartmentsByBuilding(Building building) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Department getDepartmentById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Department getDepartmentByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Map> getAllMaps() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Map> getMapsByBuilding(Building building) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map getMapByFileName(String fileName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<MapPoint> getMapPointsByMap(Map map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Room> getRoomsByBuilding(Building building) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Room> getRoomsByMap(Map map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Room getRoomByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}
}
