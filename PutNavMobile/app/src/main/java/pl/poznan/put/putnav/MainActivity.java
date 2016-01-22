package pl.poznan.put.putnav;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

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
    ArrayList<MapPoint> nn;

    ArrayList<MapPointsArcs> mpa;
    RouteFinder routeFinder;
    ArrayList<MapPoint> route;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(MainActivity.class.getSimpleName(), "przed...");
        func();
        Log.i(MainActivity.class.getSimpleName(), "po...");
        List<MapPoint> tmp = nn;



    }

    public void func() {
        db = OpenHelperManager.getHelper(this, DatabaseHandler.class);
        try {
            Log.i(MainActivity.class.getSimpleName(), "tu...");
            nn = new ArrayList<MapPoint>(db.getMapPointIntegerDao().queryForAll());
            mpa = new ArrayList<MapPointsArcs>(db.getMapPointsArcsDao().queryForAll());

            for(MapPointsArcs m : mpa)
            {
                m.setPoint1(nn);
                m.setPoint2(nn);
                m.calculateWeight();

               // Toast.makeText(MainActivity.this, "weight: " + Double.toString(m.getWeight()), Toast.LENGTH_SHORT).show();
            }


            for(MapPoint m : nn)
            {
                m.searchEdges(mpa);
                //Toast.makeText(MainActivity.this, "distance: " + Double.toString(m.getDistance()), Toast.LENGTH_SHORT).show();
            }

            for(MapPoint m : nn)
            {
                for(MapPointsArcs a : m.getEdges())
                {
                    //Toast.makeText(MainActivity.this, a.getPoint1().getId() + " : " + a.getPoint2().getId(), Toast.LENGTH_SHORT).show();
                }
            }

            Log.i(MainActivity.class.getSimpleName(), "ile: " + Integer.toString(nn.size()));
            Log.i(MainActivity.class.getSimpleName(), "ile: " + Integer.toString(mpa.size()));





        } catch (SQLException e) {
            e.printStackTrace();
        }

        routeFinder = new RouteFinder(nn, mpa);

        //Toast.makeText(MainActivity.this, "StartId: " + Integer.toString(nn.get(3).getId()), Toast.LENGTH_SHORT).show();
        //Toast.makeText(MainActivity.this, "GoalId: " + Integer.toString(nn.get(8).getId()), Toast.LENGTH_SHORT).show();



        route = routeFinder.findPath(nn.get(3), nn.get(8));
        Toast.makeText(MainActivity.this, "RouteSize: " + Integer.toString(route.size()), Toast.LENGTH_SHORT).show();



        for(MapPoint mp : route)
        {
            Toast.makeText(MainActivity.this, "Route: " +Integer.toString(mp.getId()), Toast.LENGTH_SHORT).show();
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
