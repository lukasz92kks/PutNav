package pl.poznan.put.putnav;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
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
import java.util.List;

import pl.poznan.put.putnav.widgets.TouchImageView;
import pl.poznan.put.putnav.widgets.VerticalSeekBar;


public class BuildingActivity extends AppCompatActivity {

    AutoCompleteTextView aCTVFrom;
    AutoCompleteTextView aCTVTo;
    FrameLayout container;

    DatabaseHandler db;
    ArrayList<MapPoint> mapPoints;
    ArrayList<MapPoint> originMapPoints;
    ArrayList<MapPointsArcs> mapPointsArcs;

    RouteFinder routeFinder;
    ArrayList<MapPoint> route;
    ArrayList<MapPoint> routeCpy;
    ArrayList<Room> rooms;
    ArrayList<Map> maps;
    ArrayList<Building> buildings = new ArrayList<>();
    List<Object> lista = new ArrayList<>();

    MapPoint mapPointFrom;
    MapPoint mapPointTo;

    VerticalSeekBar verticalSeekBar = null;
    ImageView imageView = null;
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();

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

    private SharedPreferences sharedPreferences;
    private static final String PREFERENCES_NAME = "appPreferences";
    private static final String PREFERENCE_DISABLED = "disabled";
    boolean disabled;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building);
        loadPreferences();
        loadDb();
        init();

        Log.i(BuildingActivity.class.getSimpleName(), "ile budynkow: " + Integer.toString(buildings.size()));
    }

    public void loadPreferences() {
        sharedPreferences = getSharedPreferences(PREFERENCES_NAME, AppCompatActivity.MODE_PRIVATE);
        if (sharedPreferences.contains("exists")) {
            disabled = sharedPreferences.getBoolean(PREFERENCE_DISABLED, false);
        }
    }

    public void init() {

        container = (FrameLayout) findViewById(R.id.picture_container);

        verticalSeekBar = (VerticalSeekBar) findViewById(R.id.verticalSeekBar);
        verticalSeekBar.setOnSeekBarChangeListener(listenerSeekbar);

        aCTVFrom = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
        aCTVTo = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView2);


        for (MapPoint m : mapPoints) {
            lista.add(m);
        }

        for (Room r : rooms) {
            //Log.i(BuildingActivity.class.getSimpleName(), "pokoj : " + r.getName());
            lista.add(r);
        }

        for (MapPoint m : mapPoints) {
            if (m.getRoom() != null) {
                if (m.getRoom().getId() == 1809) {
                    Log.i(BuildingActivity.class.getSimpleName(), ".............");
                }
            }
        }


        ArrayAdapter<Object> adapter = new ArrayAdapter<Object>(
                this, android.R.layout.simple_dropdown_item_1line, lista);
        aCTVFrom.setAdapter(adapter);
        ArrayAdapter<Object> adapter2 = new ArrayAdapter<Object>(
                this, android.R.layout.simple_dropdown_item_1line, lista);
        aCTVTo.setAdapter(adapter2);
        /*
        ArrayAdapter<MapPoint> adapter = new ArrayAdapter<MapPoint>(
                this, android.R.layout.simple_dropdown_item_1line, mapPoints);
        aCTVFrom.setAdapter(adapter);
        ArrayAdapter<MapPoint> adapter2 = new ArrayAdapter<MapPoint>(
                this, android.R.layout.simple_dropdown_item_1line, mapPoints);
        aCTVTo.setAdapter(adapter2);
        */
        aCTVFrom.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                if (arg0.getItemAtPosition(arg2) instanceof Room) {
                    Room r = (Room) arg0.getItemAtPosition(arg2);
                    for (MapPoint m : mapPoints) {
                        if (m.getRoom() != null) {
                            if (m.getRoom().getId() == r.getId()) {
                                mapPointFrom = m;
                                Log.i(BuildingActivity.class.getSimpleName(), "znalazło: " + mapPointFrom.getId());
                            }
                        }
                    }
                    //mapPointFrom = r.getFirstMapPoint();
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
                    for (MapPoint m : mapPoints) {
                        if (m.getRoom() != null) {
                            if (m.getRoom().getId() == r.getId()) {
                                mapPointTo = m;
                                Log.i(BuildingActivity.class.getSimpleName(), "znalazło: " + mapPointFrom.getId());
                            }
                        }
                    }
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
        loadImageToContainer(0);

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    imageView.getImageMatrix().invert(matrix);
                    final float[] coords = new float[]{event.getX(), event.getY()};
                    matrix.postTranslate(imageView.getScrollX(), imageView.getScrollY());
                    matrix.mapPoints(coords);
                    Log.i(BuildingActivity.class.getSimpleName(), "X: " + coords[0] + "Y: " + coords[1]);
                    int x = (int) coords[0] / 2;
                    int y = (int) coords[1] / 2;
                    double distance = 0;
                    for (MapPoint m : mapPoints) {
                        if (m.getType() == 7) {
                            distance = Math.sqrt((double) ((x - m.getX()) * (x - m.getX()) + (y - m.getY()) * (y - m.getY())));
                            if (distance < 30) {
                                Log.i(BuildingActivity.class.getSimpleName(), "distance: " + distance + " " + m.getBuilding().getId());
                            }
                        }
                    }
                }
                return false;
            }
        });

    }

    private void loadDb() {
        db = OpenHelperManager.getHelper(this, DatabaseHandler.class);
        try {
            rooms = new ArrayList<>(db.getRoomDao().queryForAll());
            mapPoints = new ArrayList<>(db.getMapPointDao().queryForAll());
            mapPointsArcs = new ArrayList<>(db.getMapPointsArcsDao().queryForAll());
            buildings = new ArrayList<>(db.getBuildingDao().queryForAll());

            //maps = new ArrayList<>(db.getMapDao().queryForAll());
            //buildings = new ArrayList<>(db.getBuildingDao().queryForAll());

            //dla każdej krawędzi przeliczamy wagi
            for (MapPointsArcs mpa : mapPointsArcs) {
                mpa.setPoint1(mapPoints);
                mpa.setPoint2(mapPoints);
                mpa.calculateWeight(disabled);
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

        // null ewentialnie jakis obiekt ktory zwraca db jak nie znajdzie (jeśli to nie null)
        if(mapPointFrom == null || mapPointTo == null) return;

        mapPoints = new ArrayList<>(originMapPoints); //usunięcie śmieci po poprzednim wyszukiwaniu
        //ew. w ten sposób:
        /*
        for (MapPoint m : mapPoints){
            m.setPrevious(null);
            m.setDistance(Double.longBitsToDouble(0x7ff0000000000000L));
        }
        */
        lines.clear();
        routeFinder = new RouteFinder(mapPoints, mapPointsArcs);

        // wyszukanie trasy
        // route - cała trasa, routeCpy - kopia robocza route
        route = routeFinder.findPath(mapPointFrom, mapPointTo);
        routeCpy = new ArrayList<MapPoint>(route);
        for (MapPoint r : route) {
            Log.i(BuildingActivity.class.getSimpleName(), "ID " + r.getId());
        }

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
