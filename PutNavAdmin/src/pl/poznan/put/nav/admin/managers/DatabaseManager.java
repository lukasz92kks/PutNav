package pl.poznan.put.nav.admin.managers;

import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import pl.poznan.put.nav.admin.entities.Building;
import pl.poznan.put.nav.admin.entities.Department;
import pl.poznan.put.nav.admin.entities.Map;
import pl.poznan.put.nav.admin.entities.MapPoint;
import pl.poznan.put.nav.admin.entities.Photo;
import pl.poznan.put.nav.admin.entities.Room;

public class DatabaseManager implements DatabaseInterface {
	
	private List<Building> buildings;
	
	private static final String PERSISTENCE_UNIT_NAME = "putnav_persistence";
	private static EntityManagerFactory factory;
	private static EntityManager em;
	
	public static void main(String[] args) {
		//factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
	    //em = factory.createEntityManager();
	    
	    
	    Query q = em.createQuery("select m from Maps m");
		List<Map> maps = q.getResultList();
		System.out.println(maps.get(0).getMapFile());
		
		/*em.getTransaction().begin();
		
		MapPoint p = new MapPoint();
		p.setMap(maps.get(0));
		maps.get(0).addMapPoint(p);
		
		em.persist(p);
		em.persist(maps.get(0));
		
		em.getTransaction().commit();*/
	}
	
	public DatabaseManager() {
		System.out.println("DB manager");
		HashMap properties = new HashMap();
		properties.put("javax.persistence.jdbc.driver", "org.sqlite.JDBC");
		properties.put("javax.persistence.jdbc.url", "jdbc:sqlite:temp/database.db");
		properties.put("javax.persistence.jdbc.user", "");
		properties.put("javax.persistence.jdbc.password", "");
		
		factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME, properties);
	    em = factory.createEntityManager();
	    
	    getBuildings();
	}
	
	@SuppressWarnings("unchecked")
	public List<Building> getBuildings() {
		Query q = em.createQuery("select b from Buildings b");
		buildings = q.getResultList();
		
		return buildings;
	}
	
	@Override
	public List<Department> getDepartments() {
		Query q = em.createQuery("select d from Departments d");
		List<Department> departments = q.getResultList();
		
		return departments;
	}
	
	@SuppressWarnings("unchecked")
	public List<Map> getMaps() {
		Query q = em.createQuery("select m from Maps m");
		List<Map> maps = q.getResultList();
		
		return maps;
	}
	
	public void commit() {
		em.getTransaction().begin();
		
		EntitiesManager entitiesManager = AppFactory.getEntitiesManager();
		for(Building building : entitiesManager.getBuildings()) {
			for(Photo photo : building.getPhotos()) {
				em.persist(photo);
			}
			
			for(Room room : building.getRooms()) {
				em.persist(room);
			}
			
			em.persist(building);
		}
		
		for(Department department : entitiesManager.getDepartments()) {
			em.persist(department);
		}
		
		for(Map map : entitiesManager.getMaps()) {
			for(MapPoint point : map.getMapPoints()) {
				em.persist(point);
			}
			em.persist(map);
		}
		
		for(Building building : entitiesManager.getBuildingsToRemove()) {
			em.remove(building);
		}
		
		for(Map map : entitiesManager.getMapsToRemove()) {
			em.remove(map);
		}
		
		for(Department department : entitiesManager.getDepartmentsToRemove()) {
			em.remove(department);
		}
		
		em.getTransaction().commit();
	}
}
