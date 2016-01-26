package pl.poznan.put.putnav;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.ArrayList;

import pl.poznan.put.putnav.widgets.TouchImageView;
import pl.poznan.put.putnav.widgets.VerticalSeekBar;


public class BuildingActivity extends AppCompatActivity {

    AutoCompleteTextView aCTVFrom;
    AutoCompleteTextView aCTVTo;
    FrameLayout container;

    DatabaseHandler db;
    ArrayList<MapPoint> mapPoints;
    ArrayList<MapPointsArcs> mapPointsArcses;

    RouteFinder routeFinder;
    ArrayList<MapPoint> route;
    ArrayList<MapPoint> routeCpy;
    ArrayList<Map> maps;
    ArrayList<Building> buildings;

    MapPoint mapPointFrom;
    MapPoint mapPointTo;

    VerticalSeekBar verticalSeekBar = null;
    ImageView imageView = null;

    ArrayList<Line> lines = new ArrayList<Line>();

    int currentMapId;

    int[] images = {R.drawable.kampus,
            R.drawable.nano_0,
            R.drawable.nano_1,
            R.drawable.nano_2,
            R.drawable.nano_3,
            R.drawable.nano_4,
            R.drawable.nano_5
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building);

        init();
        loadDb();
    }

    public void init() {

        container = (FrameLayout) findViewById(R.id.picture_container);

        aCTVFrom = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
        aCTVTo = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView2);

        verticalSeekBar = (VerticalSeekBar) findViewById(R.id.verticalSeekBar);
        verticalSeekBar.setOnSeekBarChangeListener(listenerSeekbar);

        //podajemy ilość pięter, np. 0 - budynek parterowy (powinien znikać ten pasek)
        verticalSeekBar.setMaximum(0);


        // ladowanie kampusu
        // foreach maps gdzie pole campus jest 1(albo !=, > 0)
        loadImageToContainer(0);

    }

    private void loadDb() {
        db = OpenHelperManager.getHelper(this, DatabaseHandler.class);
        try {

            mapPoints = new ArrayList<>(db.getMapPointDao().queryForAll());
            mapPointsArcses = new ArrayList<>(db.getMapPointsArcsDao().queryForAll());
            //maps = new ArrayList<>(db.getMapDao().queryForAll());
            //buildings = new ArrayList<>(db.getBuildingDao().queryForAll());

            //dla każdej krawędzi przeliczamy wagi
            for(MapPointsArcs mpa : mapPointsArcses) {
                mpa.setPoint1(mapPoints);
                mpa.setPoint2(mapPoints);
                mpa.calculateWeight();
            }


            for(MapPoint mp : mapPoints) {
                mp.searchEdges(mapPointsArcses);
            }

        } catch (SQLException e) {
            //błąd Bazy Danych
            e.printStackTrace();
        }
    }

    private void loadImageToContainer(int mapId) {
        currentMapId = mapId;
        drawMap();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu;
        //this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void reversePlaces(View view) {
        String tmpFrom = aCTVFrom.getText().toString();

        //aCTVFrom.setText("Z: " + aCTVTo.getText().toString());

        aCTVFrom.setText(aCTVTo.getText().toString());
        aCTVTo.setText(tmpFrom);
    }

    public void searchPath(View view) {
        //TODO: do usunięcia

        for(MapPoint mp : mapPoints){
            if (mp.getBuilding() !=  null) {
                //cw
                if (mp.getBuilding().getId() == 2) mapPointFrom = mp;
                //bl
                if (mp.getBuilding().getId() == 5) mapPointTo = mp;
            }
        }

        // null ewentialnie jakis obiekt ktory zwraca db jak nie znajdzie (jeśli to nie null)
        if(mapPointFrom == null || mapPointTo == null) return;

        routeFinder = new RouteFinder(mapPoints, mapPointsArcses);

        // wyszukanie trasy
        // route - cała trasa, routeCpy - kopia robocza route
        route = routeFinder.findPath(mapPointFrom, mapPointTo);
        routeCpy = new ArrayList<MapPoint>(route);

        fillLines();

        drawMap();
    }

    private void fillLines(){
        // routeCurrentMap - droga na danym piętrze (pierwsze punkty tablicy route gdzie mapId jest taki sam)
        // po dodaniu punktu do routeCurrentMap usuwamy ten punkt z routeCpy

        ArrayList<MapPoint> routeCurrentMap = new ArrayList<MapPoint>();
        int firstsPointMapId = -1;

        for(MapPoint mp : routeCpy){
            currentMapId = mp.getMap().getId();

            if(mp.getMap().getId() == currentMapId) {
                routeCurrentMap.add(mp);
                //routeCpy.remove(mp);
            }else{
                break;
            }
        }

        // zmienna pomocnicza do foreach-a
        MapPoint mplast = null;

        for (MapPoint mp : routeCurrentMap){
            if (mplast == null) {
                mplast = mp;
                continue;
            }
            lines.add(new Line(2*mplast.getX(), 2*mplast.getY(), 2*mp.getX(), 2*mp.getY()));
            mplast = mp;
        }

        // wyszukanie odpowiedniego obrazka (mapId z pierwszego punktu routeCurrentMap)
        // i wybranie mapy (currentMapId = Map.id)


    }

    // rysowanie tego co jest w tablicy (ArrayList<MapPoint>) route
    private void drawMap(){
        container.removeAllViews();
        imageView = new TouchImageView(this);

        imageView.setImageResource(images[0]);

        // tworzenie kopii na której rysujemy linie

        Bitmap lineOnBmp = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        Bitmap copy = Bitmap.createBitmap(lineOnBmp);
        Bitmap mutableBitmap = copy.copy(Bitmap.Config.ARGB_8888, true);

        Canvas canvasCopy = new Canvas(mutableBitmap);

        // właściwości linii
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(8);

        // nakładanie linii na obrazek
        for (Line line : lines) {
            canvasCopy.drawLine(line.getStartX(),line.getStartY(), line.getStopX(), line.getStopY(), paint);
        }

        imageView.setImageBitmap(mutableBitmap);

        container.addView(imageView);

    }

    //pasek boczny
    private SeekBar.OnSeekBarChangeListener listenerSeekbar = new SeekBar.OnSeekBarChangeListener()
    {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            //załadowanie nowego obrazka o ile nastąpiła zmiana piętra

            int number = seekBar.getProgress();
            loadImageToContainer(number);
        }

    };
}
