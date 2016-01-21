package pl.poznan.put.putnav;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

public class DatabaseHandler extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "database.db";

    // private static final String DATABASE_PATH =  "/data/data/pl.poznan.put.putnav/databases/";
    private static int DATABASE_VERSION = 1;
    private final Context context;
    private final String DATABASE_PATH;

    private Dao<MapPoint, Integer> mapPointIntegerDao = null;
    private Dao<MapPointsArcs, Integer> mapPointsArcsDao = null;
    private Dao<Building, Integer> buildingIntegerDao = null;
    private Dao<Department, Integer> departmentIntegerDao = null;
    private Dao<Room, Integer> roomIntegerDao = null;
    private Dao<Map, Integer> mapIntegerDao = null;


    public DatabaseHandler(Context context) throws SQLException {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        this.context = context;
        DATABASE_PATH = context.getDatabasePath(DATABASE_NAME).getPath();
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        Log.i(DatabaseHandler.class.getSimpleName(), "wchodze");

        File mFile = context.getDatabasePath(DATABASE_NAME);
        if (mFile.exists())
            mFile.delete(); //to dodałem z PoliGdzie, ale nie pomogło

        String fileName = DATABASE_PATH;
        Log.i(DatabaseHandler.class.getSimpleName(), fileName);
        InputStream in = null;
        try {
            in = context.getAssets().open(DATABASE_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte[] buffer = new byte[1024];
        int length = 0;
        try {
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        Log.i(DatabaseHandler.class.getSimpleName(), "upgraded");


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


}
