package pl.poznan.put.putnav;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    DatabaseHandler db;
    SQLiteDatabase DB;
    List<MapPoint> nn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(MainActivity.class.getSimpleName(), "przed...");
        func();
        Log.i(MainActivity.class.getSimpleName(), "po...");




    }

    public void func() {
        db = OpenHelperManager.getHelper(this, DatabaseHandler.class);
        try {
            Log.i(MainActivity.class.getSimpleName(), "tu...");
            nn = db.getMapPointIntegerDao().queryForAll();
            Log.i(MainActivity.class.getSimpleName(), "ile: " + Integer.toString(nn.size()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //----------------------------------------------------------------

    public void openBuildingActivity(View view) {
        startActivity(new Intent(this, BuildingActivity.class));
    }

    public void fast1() {

    }

}
