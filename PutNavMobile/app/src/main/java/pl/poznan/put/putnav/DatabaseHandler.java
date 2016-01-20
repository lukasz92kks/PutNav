package pl.poznan.put.putnav;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "database.db";
    private static int DATABASE_VERSION = 1;

    ArrayList<MapPoint> mapPoints;
    ArrayList<MapPointsArcs> mapPointsArcs;

    public RuntimeExceptionDao<MapPoint, Integer> getMapPointIntegerRuntimeExceptionDao() {
        if (mapPointIntegerRuntimeExceptionDao == null) {
            getMapPointsArcsIntegerRuntimeExceptionDao();
        }
        return mapPointIntegerRuntimeExceptionDao;
    }

    public RuntimeExceptionDao<MapPointsArcs, Integer> getMapPointsArcsIntegerRuntimeExceptionDao() {
        if (mapPointsArcsIntegerRuntimeExceptionDao == null) {
            getMapPointsArcsIntegerRuntimeExceptionDao();
        }
        return mapPointsArcsIntegerRuntimeExceptionDao;
    }

    private RuntimeExceptionDao<MapPoint, Integer> mapPointIntegerRuntimeExceptionDao = null;
    private RuntimeExceptionDao<MapPointsArcs, Integer> mapPointsArcsIntegerRuntimeExceptionDao = null;

    private Dao<MapPoint, Integer> mapPointIntegerDao = null;
    private Dao<MapPointsArcs, Integer> mapPointsArcsDao = null;
    private Dao<Building, Integer> buildingIntegerDao = null;
    private Dao<Department, Integer> departmentIntegerDao = null;
    private Dao<Room, Integer> roomIntegerDao = null;
    private Dao<Map, Integer> mapIntegerDao = null;


    public DatabaseHandler(Context context) throws SQLException {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        Log.i(DatabaseHandler.class.getSimpleName(), "created");
        try {
            TableUtils.createTable(connectionSource, MapPointsArcs.class);
            TableUtils.createTable(connectionSource, MapPoint.class);
        } catch (SQLException ex) {
            Log.e(DatabaseHandler.class.getSimpleName(), "unable to create", ex);
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        Log.i(DatabaseHandler.class.getSimpleName(), "upgraded");
        try {
            TableUtils.dropTable(connectionSource, MapPointsArcs.class, true);
            TableUtils.dropTable(connectionSource, MapPoint.class, true);
        } catch (SQLException e) {
            Log.e(DatabaseHandler.class.getSimpleName(), "error", e);
            throw new RuntimeException(e);
        }
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

}
