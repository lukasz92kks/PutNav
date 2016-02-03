package pl.poznan.put.putnav;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pl.poznan.put.putnav.widgets.TouchImageView;
import pl.poznan.put.putnav.widgets.VerticalSeekBar;


public class BuildingActivity extends AppCompatActivity implements View.OnTouchListener {

    AutoCompleteTextView aCTVFrom;
    AutoCompleteTextView aCTVTo;
    FrameLayout container;

    DatabaseHandler db;
    ArrayList<MapPoint> mapPoints;
    ArrayList<MapPoint> originMapPoints;
    ArrayList<MapPointsArcs> mapPointsArcs;

    RouteFinder routeFinder;
    ArrayList<MapPoint> route;
    ArrayList<Room> rooms;
    ArrayList<Map> maps;
    ArrayList<Building> buildings = new ArrayList<>();
    List<Object> lista = new ArrayList<>();

    MapPoint mapPointFrom;
    MapPoint mapPointTo;

    VerticalSeekBar verticalSeekBar = null;
    ImageView imageView = null;

    ArrayList<Line> lines = new ArrayList<Line>();

    int currentMapId; //id zasobu np. R.resources.cd_parter
    int currentPathMapId; // id aktualnej mapy tablicy pathMaps
    ArrayList<Map> pathMaps; // kolejne mapy wyznaczonej trasy

    int[] images = {R.drawable.kampus,
            R.drawable.nano_0,
            R.drawable.nano_1,
            R.drawable.nano_2,
            R.drawable.nano_3,
            R.drawable.nano_4,
            R.drawable.nano_5
    };
    HashMap<String, Integer> mapsHash = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building);
        loadDb();
        init();

        Log.i(BuildingActivity.class.getSimpleName(), "ile budynkow: " + Integer.toString(buildings.size()));
    }

    public void init() {

        container = (FrameLayout) findViewById(R.id.picture_container);

        verticalSeekBar = (VerticalSeekBar) findViewById(R.id.verticalSeekBar);
        verticalSeekBar.setOnSeekBarChangeListener(listenerSeekbar);

        aCTVFrom = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
        aCTVTo = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView2);

        verticalSeekBar = (VerticalSeekBar) findViewById(R.id.verticalSeekBar);
        verticalSeekBar.setOnSeekBarChangeListener(listenerSeekbar);

        for (MapPoint m : mapPoints) {
            lista.add(m);
        }

        for (Room r : rooms) {
            lista.add(r);
        }

        ArrayAdapter<Object> adapter = new ArrayAdapter<Object>(
                this, android.R.layout.simple_dropdown_item_1line, lista);
        aCTVFrom.setAdapter(adapter);
        ArrayAdapter<MapPoint> adapter2 = new ArrayAdapter<MapPoint>(
                this, android.R.layout.simple_dropdown_item_1line, mapPoints);
        aCTVTo.setAdapter(adapter2);

        aCTVFrom.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (arg0.getItemAtPosition(arg2) instanceof Room) {
                    Room r = (Room) arg0.getItemAtPosition(arg2);
                    mapPointFrom = r.getFirstMapPoint();
                } else if (arg0.getItemAtPosition(arg2) instanceof MapPoint) {
                    mapPointFrom = (MapPoint) arg0.getItemAtPosition(arg2);
                }
                Log.i(BuildingActivity.class.getSimpleName(), "id: " + mapPointFrom.getId());
            }
        });

        aCTVTo.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                if (arg0.getItemAtPosition(arg2) instanceof Room) {
                    Room r = (Room) arg0.getItemAtPosition(arg2);
                    mapPointTo = r.getFirstMapPoint();
                } else if (arg0.getItemAtPosition(arg2) instanceof MapPoint) {
                    mapPointTo = (MapPoint) arg0.getItemAtPosition(arg2);
                }
                Log.i(BuildingActivity.class.getSimpleName(), "id: " + mapPointTo.getId());
            }
        });


        //podajemy ilość pięter, np. 0 - budynek parterowy (powinien znikać ten pasek)
        verticalSeekBar.setMaximum(0);


        // ladowanie kampusu
        // foreach maps gdzie pole campus jest 1(albo !=, > 0)
        for (Map map : maps){
            if(map.getCampus() == 1)
                currentMapId = mapsHash.get(map.getFileName());
        }

        drawMap();

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    int x = (int) event.getX();
                    int y = (int) event.getY();
                    Log.i(BuildingActivity.class.getSimpleName(), "x: " + x + "y: " + y);
                    int[] viewCoords = new int[2];
                    imageView.getLocationOnScreen(viewCoords);
                    int iX = x - viewCoords[0]; // viewCoords[0] is the X coordinate
                    int iY = y - viewCoords[1];
                    Log.i(BuildingActivity.class.getSimpleName(), "ximg: " + iX + "yimg: " + iY);


                    // Transform to relative coordinates
                    /*
                    float[] point = new float[2];
                    point[0] = e.getX();
                    point[1] = e.getY();
                    inverseMatrix.mapPoints(point);
                    */
                }
                return false;
            }
        });

    }

    private void loadDb() {
        db = OpenHelperManager.getHelper(this, DatabaseHandler.class);
        try {

            mapPoints = new ArrayList<>(db.getMapPointDao().queryForAll());
            mapPointsArcs = new ArrayList<>(db.getMapPointsArcsDao().queryForAll());
            buildings = new ArrayList<>(db.getBuildingDao().queryForAll());
            rooms = new ArrayList<>(db.getRoomDao().queryForAll());
            maps = new ArrayList<>(db.getMapDao().queryForAll());
            //buildings = new ArrayList<>(db.getBuildingDao().queryForAll());


            //TODO:tymczasowy hash z mapami zastapic normalnym wczytywaniem z pliku
            mapsHash.put(maps.get(0).getFileName(), R.drawable.bt_1_pietro);
            mapsHash.put(maps.get(1).getFileName(), R.drawable.bt_parter);
            mapsHash.put(maps.get(2).getFileName(), R.drawable.bt_2_pietro);
            mapsHash.put(maps.get(3).getFileName(), R.drawable.cw_parter);
            mapsHash.put(maps.get(4).getFileName(), R.drawable.cw_1_pietro);
            mapsHash.put(maps.get(5).getFileName(), R.drawable.kampus);
            mapsHash.put(maps.get(6).getFileName(), R.drawable.bl_parter);
            mapsHash.put(maps.get(7).getFileName(), R.drawable.bl_pietro_1);
            mapsHash.put(maps.get(8).getFileName(), R.drawable.bl_pietro_2);
            mapsHash.put(maps.get(9).getFileName(), R.drawable.bl_przyziemie);
            mapsHash.put(maps.get(10).getFileName(), R.drawable.bm_parter);
            mapsHash.put(maps.get(11).getFileName(), R.drawable.bm_pietro_1);
            mapsHash.put(maps.get(12).getFileName(), R.drawable.bm_pietro_2);
            mapsHash.put(maps.get(13).getFileName(), R.drawable.bm_pietro_3);
            mapsHash.put(maps.get(14).getFileName(), R.drawable.bm_pietro_4);
            mapsHash.put(maps.get(15).getFileName(), R.drawable.bm_pietro_5);
            mapsHash.put(maps.get(16).getFileName(), R.drawable.bm_pietro_6);
            mapsHash.put(maps.get(17).getFileName(), R.drawable.bm_pietro_7);
            mapsHash.put(maps.get(18).getFileName(), R.drawable.bm_pietro_8);

            //dla każdej krawędzi przeliczamy wagi
            for (MapPointsArcs mpa : mapPointsArcs) {
                mpa.setPoint1(mapPoints);
                mpa.setPoint2(mapPoints);
                mpa.calculateWeight(false);
            }


            for(MapPoint mp : mapPoints) {
                mp.searchEdges(mapPointsArcs);
            }

            originMapPoints = new ArrayList<>(mapPoints);

        } catch (SQLException e) {
            //błąd Bazy Danych
            e.printStackTrace();
        }
    }

    private void loadImageToContainer(int resourceId) {
        currentMapId = resourceId;
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
            //p1
            if (mp.getId() == 116) mapPointFrom = mp;
            //p2
            if (mp.getId() == 112) mapPointTo = mp;

        }

        // null ewentialnie jakis obiekt ktory zwraca db jak nie znajdzie (jeśli to nie null)
        if(mapPointFrom == null || mapPointTo == null) return;

        //mapPoints = new ArrayList<>(originMapPoints); //usunięcie śmieci po poprzednim wyszukiwaniu
        //ew. w ten sposób:
        /*
        for (MapPoint m : mapPoints){
            m.setPrevious(null);
            m.setDistance(Double.longBitsToDouble(0x7ff0000000000000L));
        }
        */
        lines.clear();
        routeFinder = new RouteFinder(mapPoints, mapPointsArcs);
        pathMaps = new ArrayList<>();

        // wyszukanie trasy
        // route - cała trasa
        route = routeFinder.findPath(mapPointFrom, mapPointTo);

        if (route.isEmpty()) return;

        // wypełnienie listy map na ktorych bedzie trasa
        Map lastMap = null;
        for (MapPoint mp : route) {
            if(lastMap != null && lastMap.getFileName().equals(mp.getMap().getFileName()))
                continue;

            pathMaps.add(mp.getMap());
            lastMap = mp.getMap();
        }

        currentPathMapId = 0;

        currentMapId = mapsHash.get(pathMaps.get(currentPathMapId).getFileName());

        fillLines();

        drawMap();
    }

    private void fillLines(){
        // currentMapPoints - lista punkow na danym piętrze
        ArrayList<MapPoint> currentMapPoints = new ArrayList<MapPoint>();

        for(MapPoint mp : route){
            if(mapsHash.get(mp.getMap().getFileName()) == currentMapId) {
                currentMapPoints.add(mp);
            }
        }

        // zmienna pomocnicza do foreach-a do wyznaczenia linii z p1 do p2
        MapPoint mplast = null;

        for (MapPoint mp : currentMapPoints){
            if (mplast == null) {
                mplast = mp;
                continue;
            }
            lines.add(new Line(2*mplast.getX(), 2*mplast.getY(), 2*mp.getX(), 2*mp.getY()));
            mplast = mp;
        }
    }

    // rysowanie tego co jest w tablicy (ArrayList<MapPoint>) route
    private void drawMap(){
        container.removeAllViews();
        imageView = new TouchImageView(this);

        imageView.setImageResource(currentMapId);

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

    public void nextMap(View view){
        if(currentPathMapId < pathMaps.size()-1)
            currentPathMapId++;
        else
            return;

        currentMapId = mapsHash.get(pathMaps.get(currentPathMapId).getFileName());
        fillLines();
        drawMap();
    }

    public void previousMap(View view){
        if(currentMapId > 0)
            currentPathMapId--;
        else
            return;

        currentMapId = mapsHash.get(pathMaps.get(currentPathMapId).getFileName());
        fillLines();
        drawMap();
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        Log.i(BuildingActivity.class.getSimpleName(), "ggggggggg");
        return false;
    }
}
