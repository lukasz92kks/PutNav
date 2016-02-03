package pl.poznan.put.putnav;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

public class DatabaseHandler extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "database.db";
    private static int DATABASE_VERSION = 1;
    private String appDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "putnavadmin").getAbsolutePath();

    private Dao<MapPoint, Integer> mapPointDao = null;
    private Dao<MapPointsArcs, Integer> mapPointsArcsDao = null;
    private Dao<Building, Integer> buildingDao = null;
    private Dao<Department, Integer> departmentDao = null;
    private Dao<Room, Integer> roomDao = null;
    private Dao<Map, Integer> mapDao = null;


    public DatabaseHandler(Context context) throws SQLException {

        super(context, ArchiveFileManager.getDatabasePath(),
                //new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "putnavadmin/database.db").getAbsolutePath(),
                null, DATABASE_VERSION);

        if(!new File(appDir).exists()) {
            File dir = new File(context.getDatabasePath(DATABASE_NAME).getPath());
            dir.mkdirs(); //żeby nie wywalało błędu ENOENT

            //File dir = new File(appDir);
            //dir.mkdir();

            File mFile = context.getDatabasePath(DATABASE_NAME);
            if (mFile.exists()) {
                mFile.delete();
            }

            String fileName = context.getDatabasePath(DATABASE_NAME).getPath();
            InputStream in = null;
            try {
                String s = new File(appDir, DATABASE_NAME).getAbsolutePath();
                //Log.i(DatabaseHandler.class.getName(), s);
                //in = new FileInputStream(new File(appDir, DATABASE_NAME));
                in = context.getAssets().open(DATABASE_NAME);
                FileOutputStream out = null;
                out = new FileOutputStream(fileName);
                byte[] buffer = new byte[1024];
                int length = 0;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer);
                }
                in.close();
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }

    public Dao<MapPoint, Integer> getMapPointDao() throws SQLException {
        if (mapPointDao == null) {
            mapPointDao = getDao(MapPoint.class);
        }
        return mapPointDao;
    }

    public Dao<MapPointsArcs, Integer> getMapPointsArcsDao() throws SQLException {
        if (mapPointsArcsDao == null) {
            mapPointsArcsDao = getDao(MapPointsArcs.class);
        }
        return mapPointsArcsDao;
    }

    public Dao<Building, Integer> getBuildingDao() throws SQLException {
        if (buildingDao == null) {
            buildingDao = getDao(Building.class);
        }
        return buildingDao;
    }

    public Dao<Department, Integer> getDepartmentDao() throws SQLException {
        if (departmentDao == null) {
            departmentDao = getDao(Department.class);
        }
        return departmentDao;
    }

    public Dao<Room, Integer> getRoomDao() throws SQLException {
        if (roomDao == null) {
            roomDao = getDao(Room.class);
        }
        return roomDao;
    }

    public Dao<Map, Integer> getMapDao() throws SQLException {
        if (mapDao == null) {
            mapDao = getDao(Map.class);
        }
        return mapDao;
    }

}
