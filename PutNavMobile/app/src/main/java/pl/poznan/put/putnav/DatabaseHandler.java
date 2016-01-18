package pl.poznan.put.putnav;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends OrmLiteSqliteOpenHelper {

    private static String DATABASE_NAME = "database.db";
    private static int DATABASE_VERSION = 1;

    ArrayList<MapPoint> mapPoints;
    ArrayList<MapPointsArcs> mapPointsArcs;

    private Dao<MapPoint, Integer> mapPointIntegerDao = null;
    private Dao<MapPointsArcs, Integer> mapPointsArcsDao = null;
    private Dao<Building, Integer> buildingIntegerDao = null;
    private Dao<Department, Integer> departmentIntegerDao = null;
    private Dao<Room, Integer> roomIntegerDao = null;
    private Dao<Map, Integer> mapIntegerDao = null;


    public DatabaseHandler(Context context, String databaseName, SQLiteDatabase.CursorFactory factory, int databaseVersion) throws SQLException {
        super(context, databaseName, factory, databaseVersion);
        this.mapPoints = new ArrayList<MapPoint>();
        this.mapPoints = (ArrayList<MapPoint>) getMapPointIntegerDao().queryForAll();
        this.mapPointsArcs = new ArrayList<MapPointsArcs>();
        this.mapPointsArcs = (ArrayList<MapPointsArcs>) getMapPointsArcsDao().queryForAll();
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }

    public Dao<Building, Integer> getBuildingIntegerDao() throws SQLException {
        if (buildingIntegerDao == null) {
            buildingIntegerDao = getDao(Building.class);
        }
        return buildingIntegerDao;
    }

    public Dao<MapPoint, Integer> getMapPointIntegerDao() throws SQLException {
        if (mapPointIntegerDao == null) {
            mapPointIntegerDao = getDao(MapPoint.class);
        }
        return mapPointIntegerDao;
    }

    public Dao<MapPointsArcs, Integer> getMapPointsArcsDao() throws SQLException {
        if (mapPointsArcsDao == null) {
            mapPointsArcsDao = getDao(MapPointsArcs.class);
        }
        return mapPointsArcsDao;
    }

    public ArrayList<MapPoint> getMapPoints() {
        return mapPoints;
    }

    public ArrayList<MapPointsArcs> getMapPointsArcs() {
        return mapPointsArcs;
    }

    public void setupArcsInMapPoints() {
        for (MapPointsArcs arcs : mapPointsArcs) {
            //id to reference
        }
        // point.add edge1
        //mapPoints.get()
        // point.add edge2


    }
}
