package pl.poznan.put.putnav;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
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
    ArrayList<Room> rooms;
    ArrayList<Map> maps;
    List<Map> currentBuildingMaps;
    ArrayList<Building> buildings = new ArrayList<>();
    List<Object> lista = new ArrayList<>();

    String appDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "putnavadmin").getAbsolutePath();
    String mapsDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "putnavadmin/maps").getAbsolutePath();
    private SharedPreferences sharedPreferences;
    private static final String PREFERENCES_NAME = "appPreferences";
    private static final String PREFERENCE_DISABLED = "disabled";
    boolean disabled;

    Matrix matrix = new Matrix();

    MapPoint mapPointFrom;
    MapPoint mapPointTo;

    VerticalSeekBar verticalSeekBar = null;
    ImageView imageView = null;

    double scale = 0;
    int idOfCurrentMap = 0;
    Map currentMap;
    Path path;

    MapPoint secondPointOnMap;
    MapPoint lastPointOnMap;

    Button buttonGoIn; //wejdz do budynku
    Button buttonAboutBuilding; //o budynku
    Button buttonSetAsStartPoint;

    Building chosenBuilding;

    String wc = new String("WC");

    ArrayList<Line> lines = new ArrayList<Line>();

    int currentMapId; //id zasobu np. R.resources.cd_parter
    int currentPathMapId; // id aktualnej mapy tablicy pathMaps
    String currentMapFile;
    ArrayList<Map> pathMaps; // kolejne mapy wyznaczonej trasy

    Map chosenMap;

    //HashMap<String, Integer> mapsHash = new HashMap<>();

    boolean navigationMode = false;

    Button nextMapButton;
    Button previousMapButton;
    Button escapeNavigationModeButton;
    Button floorUpButton;
    Button floodDownButton;

    TextView aboutCurrentMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building);
        aboutCurrentMap = (TextView) findViewById(R.id.textViewCurrentMap);

        imageView = new TouchImageView(this);
        loadDb();
        init();
        getWindow().setFormat(PixelFormat.RGB_565);

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

        buttonGoIn = (Button) findViewById(R.id.button5); //wejdz do budynku
        buttonAboutBuilding = (Button) findViewById(R.id.button9); //o budynku
        buttonSetAsStartPoint = (Button) findViewById(R.id.button10); // ustaw jako punkt startowy

        buttonGoIn.setEnabled(false);
        buttonGoIn.setVisibility(View.INVISIBLE);
        buttonAboutBuilding.setEnabled(false);
        buttonAboutBuilding.setVisibility(View.INVISIBLE);
        buttonSetAsStartPoint.setEnabled(false);
        buttonSetAsStartPoint.setVisibility(View.INVISIBLE);

        verticalSeekBar = (VerticalSeekBar) findViewById(R.id.verticalSeekBar);
        verticalSeekBar.setOnSeekBarChangeListener(listenerSeekbar);

        aCTVFrom = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
        aCTVTo = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView2);

        verticalSeekBar = (VerticalSeekBar) findViewById(R.id.verticalSeekBar);
        verticalSeekBar.setOnSeekBarChangeListener(listenerSeekbar);

        nextMapButton = (Button) findViewById(R.id.buttonNextMap);
        previousMapButton = (Button) findViewById(R.id.buttonPreviousMap);
        escapeNavigationModeButton = (Button) findViewById(R.id.buttonEscapeNavigationMode);
        navigationModeOff();

        for (MapPoint m : mapPoints) {
            lista.add(m);
        }

        for (Room r : rooms) {
            lista.add(r);
        }


        currentBuildingMaps = new ArrayList<>();

        ArrayAdapter<Object> adapter = new ArrayAdapter<Object>(
                this, android.R.layout.simple_dropdown_item_1line, lista);
        aCTVFrom.setAdapter(adapter);
        ArrayAdapter<Object> adapter2 = new ArrayAdapter<Object>(
                this, android.R.layout.simple_dropdown_item_1line, lista);
        aCTVTo.setAdapter(adapter2);

        aCTVFrom.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if (arg0.getItemAtPosition(arg2) instanceof Room) {
                    Room r = (Room) arg0.getItemAtPosition(arg2);
                    for (MapPoint m : mapPoints) {
                        if (m.getRoom() != null) {
                            if (m.getRoom().getId() == r.getId()) {
                                mapPointFrom = m;
                            }
                        }
                    }
                    //mapPointFrom = r.getFirstMapPoint();
                } else if (arg0.getItemAtPosition(arg2) instanceof MapPoint) {
                    mapPointFrom = (MapPoint) arg0.getItemAtPosition(arg2);
                }
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
                            }
                        }
                    }
                } else if (arg0.getItemAtPosition(arg2) instanceof MapPoint) {
                    mapPointTo = (MapPoint) arg0.getItemAtPosition(arg2);
                }
            }
        });


        //podajemy ilość pięter, np. 0 - budynek parterowy (powinien znikać ten pasek)
        verticalSeekBar.setMaximum(0);


        // ladowanie kampusu
        // for each maps gdzie pole campus jest 1(albo !=, > 0)
        goOutsideFunc();

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    imageView.getImageMatrix().invert(matrix);
                    final float[] coords = new float[]{event.getX(), event.getY()};
                    matrix.postTranslate(imageView.getScrollX(), imageView.getScrollY());
                    matrix.mapPoints(coords);
                    Log.i(BuildingActivity.class.getSimpleName(), "X: " + coords[0] + "Y: " + coords[1]);
                    int x = (int) coords[0];
                    int y = (int) coords[1];
                    double distance = 0;
                    Log.i(BuildingActivity.class.getSimpleName(), "mapa: " + currentMapId);
                    boolean hit = false;
                    if (currentMap.getCampus() == 1) { // mapa kampusu
                        for (MapPoint m : mapPoints) {
                            if (m.getType() == 7) {
                                distance = Math.sqrt((double) ((x - m.getX()) * (x - m.getX()) + (y - m.getY()) * (y - m.getY())));
                                if (distance < 30) {
                                    hit = true;
                                    Log.i(BuildingActivity.class.getSimpleName(), "distance: " + distance + " " + m.getBuilding().getId());
                                    chosenBuilding = m.getBuilding();
                                    buttonSetAsStartPoint.setEnabled(true);
                                    buttonSetAsStartPoint.setVisibility(View.VISIBLE);
                                    buttonGoIn.setEnabled(true);
                                    buttonGoIn.setVisibility(View.VISIBLE);
                                    buttonAboutBuilding.setEnabled(true);
                                    buttonAboutBuilding.setVisibility(View.VISIBLE);
                                    break;
                                }else {
                                    hit = false;
                                }
                            }
                        }
                        if (!hit) {
                            hideTouchableButtons();
                        }
                    } else if (currentMap.getCampus() == 0) { // mapa budynku
                        for (MapPoint m : mapPoints) {
                            if (m.getMap().getId() == currentMap.getId()) { //TODO id obecnej mapy; dobrze?
                                if (m.getType() == 3) { // sprawdzamy czy kliknelismy na wyjscie
                                    distance = Math.sqrt((double) ((x - m.getX()) * (x - m.getX()) + (y - m.getY()) * (y - m.getY())));
                                    Log.i(BuildingActivity.class.getSimpleName(), "dist: " + distance);
                                    if (distance < 30) {
                                        goOutsideFunc();
                                    }
                                } else if (m.getType() == 2) {
                                    distance = Math.sqrt((double) ((x - m.getX()) * (x - m.getX()) + (y - m.getY()) * (y - m.getY())));
                                    if (distance < 30) {
                                        ArrayList<MapPointsArcs> edges = m.getEdges();
                                        for (MapPointsArcs mpa : edges) {
                                            if (mpa.getPoint1().getMap().getId() != mpa.getPoint2().getMap().getId()) {
                                                if (mpa.getPoint1().getId() == m.getId()) {
                                                    chosenMap = mpa.getPoint2().getMap();
                                                } else {
                                                    chosenMap = mpa.getPoint1().getMap();
                                                }
                                                //Log.i(BuildingActivity.class.getSimpleName(), "Rysuję drzwi : X = " + mapPoint.getX() + ", Y = " + mapPoint.getY());
                                            }
                                        }
                                        changeBuilding();
                                    }
                                }

                                 /* else if (m.getType() == 4) { // sprawdzamy czy kliknęliśmy na chody
                                    distance = Math.sqrt((double) ((x - m.getX()) * (x - m.getX()) + (y - m.getY()) * (y - m.getY())));
                                    if (distance < 10) { //raczej mała liczba, bo musza byc schody UP i DOWN (czy nie?), wiec zeby sie nie nakladalo
                                        //zmiana mapy UP lub down  // sprawdzic czy mozna najpierw (parter, lub max pietro)
                                    }
                                } */
                            }
                        }

                    }
                }

                if(chosenBuilding != null)
                    Log.i(BuildingActivity.class.getSimpleName(), "Zaznaczony budynek: " + chosenBuilding.getName());

                return false;
            }
        });

    }

    private void CopyRAWtoSDCard(int id, String path) throws IOException {
        InputStream in = getResources().openRawResource(id);
        FileOutputStream out = new FileOutputStream(path);
        byte[] buff = new byte[1024];
        int read = 0;
        try {
            while ((read = in.read(buff)) > 0) {
                out.write(buff, 0, read);
            }
        } finally {
            in.close();
            out.close();
        }
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

            File dir = new File(appDir);
            if(!dir.exists()) {
                dir.mkdir();
                Log.i(BuildingActivity.class.getSimpleName(), "mkdir " + dir + " exists " + dir.exists());
                dir = new File(mapsDir);
                dir.mkdir();
                Log.i(BuildingActivity.class.getSimpleName(), "mkdir " + dir  + " exists " + dir.exists());
                try {
                    CopyRAWtoSDCard(R.drawable.bt_1_pietro, new File(mapsDir, maps.get(0).getFileName()).getAbsolutePath());
                    CopyRAWtoSDCard(R.drawable.bt_parter, new File(mapsDir, maps.get(1).getFileName()).getAbsolutePath());
                    CopyRAWtoSDCard(R.drawable.bt_2_pietro, new File(mapsDir, maps.get(2).getFileName()).getAbsolutePath());
                    CopyRAWtoSDCard(R.drawable.cw_parter, new File(mapsDir, maps.get(3).getFileName()).getAbsolutePath());
                    CopyRAWtoSDCard(R.drawable.cw_1_pietro, new File(mapsDir, maps.get(4).getFileName()).getAbsolutePath());
                    CopyRAWtoSDCard(R.drawable.kampus, new File(mapsDir, maps.get(5).getFileName()).getAbsolutePath());
                    CopyRAWtoSDCard(R.drawable.bl_parter, new File(mapsDir, maps.get(6).getFileName()).getAbsolutePath());
                    CopyRAWtoSDCard(R.drawable.bl_pietro_1, new File(mapsDir, maps.get(7).getFileName()).getAbsolutePath());
                    CopyRAWtoSDCard(R.drawable.bl_pietro_2, new File(mapsDir, maps.get(8).getFileName()).getAbsolutePath());
                    CopyRAWtoSDCard(R.drawable.bl_przyziemie, new File(mapsDir, maps.get(9).getFileName()).getAbsolutePath());
                    CopyRAWtoSDCard(R.drawable.bm_parter, new File(mapsDir, maps.get(10).getFileName()).getAbsolutePath());
                    CopyRAWtoSDCard(R.drawable.bm_pietro_1, new File(mapsDir, maps.get(11).getFileName()).getAbsolutePath());
                    CopyRAWtoSDCard(R.drawable.bm_pietro_2, new File(mapsDir, maps.get(12).getFileName()).getAbsolutePath());
                    CopyRAWtoSDCard(R.drawable.bm_pietro_3, new File(mapsDir, maps.get(13).getFileName()).getAbsolutePath());
                    CopyRAWtoSDCard(R.drawable.bm_pietro_4, new File(mapsDir, maps.get(14).getFileName()).getAbsolutePath());
                    CopyRAWtoSDCard(R.drawable.bm_pietro_5, new File(mapsDir, maps.get(15).getFileName()).getAbsolutePath());
                    CopyRAWtoSDCard(R.drawable.bm_pietro_6, new File(mapsDir, maps.get(16).getFileName()).getAbsolutePath());
                    CopyRAWtoSDCard(R.drawable.bm_pietro_7, new File(mapsDir, maps.get(17).getFileName()).getAbsolutePath());
                    CopyRAWtoSDCard(R.drawable.bm_pietro_8, new File(mapsDir, maps.get(18).getFileName()).getAbsolutePath());
                    CopyRAWtoSDCard(R.drawable.el_parter, new File(mapsDir, maps.get(19).getFileName()).getAbsolutePath());
                    CopyRAWtoSDCard(R.drawable.el_pietro_1, new File(mapsDir, maps.get(20).getFileName()).getAbsolutePath());
                    CopyRAWtoSDCard(R.drawable.el_pietro_2, new File(mapsDir, maps.get(21).getFileName()).getAbsolutePath());
                    CopyRAWtoSDCard(R.drawable.el_pietro_3, new File(mapsDir, maps.get(22).getFileName()).getAbsolutePath());
                    CopyRAWtoSDCard(R.drawable.el_pietro_4, new File(mapsDir, maps.get(23).getFileName()).getAbsolutePath());
                    CopyRAWtoSDCard(R.drawable.el_pietro_5, new File(mapsDir, maps.get(24).getFileName()).getAbsolutePath());
                    CopyRAWtoSDCard(R.drawable.el_pietro_6, new File(mapsDir, maps.get(25).getFileName()).getAbsolutePath());
                    CopyRAWtoSDCard(R.drawable.el_pietro_7, new File(mapsDir, maps.get(26).getFileName()).getAbsolutePath());
                    CopyRAWtoSDCard(R.drawable.el_pietro_8, new File(mapsDir, maps.get(27).getFileName()).getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            /*mapsHash.put(maps.get(0).getFileName(), R.drawable.bt_1_pietro);
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
            mapsHash.put(maps.get(19).getFileName(), R.drawable.el_parter);
            mapsHash.put(maps.get(20).getFileName(), R.drawable.el_pietro_1);
            mapsHash.put(maps.get(21).getFileName(), R.drawable.el_pietro_2);
            mapsHash.put(maps.get(22).getFileName(), R.drawable.el_pietro_3);
            mapsHash.put(maps.get(23).getFileName(), R.drawable.el_pietro_4);
            mapsHash.put(maps.get(24).getFileName(), R.drawable.el_pietro_5);
            mapsHash.put(maps.get(25).getFileName(), R.drawable.el_pietro_6);
            mapsHash.put(maps.get(26).getFileName(), R.drawable.el_pietro_7);
            mapsHash.put(maps.get(27).getFileName(), R.drawable.el_pietro_8);*/

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
        currentMap = getCurrentMap();
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
        aCTVFrom.setText(aCTVTo.getText().toString());
        aCTVTo.setText(tmpFrom);
        MapPoint tmp = mapPointFrom;
        mapPointFrom = mapPointTo;
        mapPointTo = tmp;


    }

    public void searchPath(View view) {

        // null ewentialnie jakis obiekt ktory zwraca db jak nie znajdzie (jeśli to nie null)
        if(mapPointFrom == null || mapPointTo == null) return;

        //mapPoints = new ArrayList<>(originMapPoints); //Złe!!!


        for (MapPoint m : mapPoints){
            m.setPrevious(null);
            m.setDistance(Double.longBitsToDouble(0x7ff0000000000000L));
        }

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
            if (lastMap != null && lastMap.getFileName().equals(mp.getMap().getFileName()) || mp.getType() == 5)
                continue;

            pathMaps.add(mp.getMap());
            lastMap = mp.getMap();
        }

        currentPathMapId = 0;
        Map a = pathMaps.get(currentPathMapId);
        Log.i(BuildingActivity.class.getSimpleName(), "mapa: " + a.getId());
        changeMap(a.getFileName());


        for (Map m : pathMaps) {
            Log.i(BuildingActivity.class.getSimpleName(), "mapy: " + m.getId());
        }

        scale = 1.0;
        fillLines();

        drawMap();

        navigationModeOn();
    }

    private void fillLines(){
        lines.clear();
        // currentMapPoints - lista punkow na danym piętrze
        ArrayList<MapPoint> currentMapPoints = new ArrayList<MapPoint>();

        for (MapPoint mp : route) {
            if (mp.getMap().getId() == currentMapId) {
                currentMapPoints.add(mp);
            }
        }

        // zmienna pomocnicza do foreach-a do wyznaczenia linii z p1 do p2
        MapPoint mplast = null;

        for (MapPoint mp : currentMapPoints) {
            if (mplast == null) {
                mplast = mp;
                continue;
            }
            lines.add(new Line((int) (scale * mplast.getX()), (int) (scale * mplast.getY()), (int) (scale * mp.getX()), (int) (scale * mp.getY())));
            mplast = mp;
        }
        lastPointOnMap = mplast;
        if (currentMapPoints.size() > 1) {
            secondPointOnMap = currentMapPoints.get(1);
        }
    }

    public double[] quadraticEquationRoot(double a, double b, double c) {
        double root1, root2;
        root1 = (-b + Math.sqrt(b * b - 4 * a * c)) / (2 * a);
        root2 = (-b - Math.sqrt(b * b - 4 * a * c)) / (2 * a);
        return new double[]{root1, root2};
    }

    public double[] getFactors(MapPoint mp1, MapPoint mp2) {
        double x1 = (double) mp1.getX();
        double y1 = (double) mp1.getY();
        double x2 = (double) mp2.getX();
        double y2 = (double) mp2.getY();
        double tmpY = y2 - y1;
        double tmpX = x2 - x1;
        double a = tmpY / tmpX;
        double b = y1 - x1 * a;

        return new double[]{a, b};
    }

    public void buildTriangleStart(MapPoint mp1, MapPoint mp2) {

        Point a;
        Point b;
        Point c;

        if (mp1.getX() == mp2.getX()) {
            if (mp2.getY() > mp1.getY()) {
                a = new Point(mp2.getX(), mp1.getY() + 55);
                b = new Point(mp2.getX() - 22, mp1.getY());
                c = new Point(mp2.getX() + 22, mp1.getY());
            } else {
                a = new Point(mp2.getX(), mp1.getY() - 55);
                b = new Point(mp2.getX() - 22, mp1.getY());
                c = new Point(mp2.getX() + 22, mp1.getY());
            }

        } else {

            double[] factors1 = getFactors(mp1, mp2);
            double x1 = (double) mp1.getX();
            double y1 = (double) mp1.getY();
            double[] roots1 = quadraticEquationRoot(
                    1.0 + factors1[0] * factors1[0],
                    -2 * x1 + 2 * factors1[0] * factors1[1] - 2 * factors1[0] * y1,
                    x1 * x1 + y1 * y1 + factors1[1] * factors1[1] - 2 * y1 * factors1[1] - 3000);
            double xt0 = 0;
            if (mp1.getX() > mp2.getX()) {
                xt0 = Math.min(roots1[0], roots1[1]);
            } else {
                xt0 = Math.max(roots1[0], roots1[1]);
            }
            double yt0 = xt0 * factors1[0] + factors1[1];
            if (factors1[0] == 0) {
                factors1[0] += 0.00000001;
            }
            double[] factors2 = {-1 / factors1[0], y1 + 1 / factors1[0] * x1};
            double[] roots2 = quadraticEquationRoot(
                    1.0 + factors2[0] * factors2[0],
                    -2 * x1 + 2 * factors2[0] * factors2[1] - 2 * factors2[0] * y1,
                    x1 * x1 + y1 * y1 + factors2[1] * factors2[1] - 2 * y1 * factors2[1] - 500);
            double xt1 = Math.max(roots2[0], roots2[1]);
            double xt2 = Math.min(roots2[0], roots2[1]);
            double yt1 = xt1 * factors2[0] + factors2[1];
            double yt2 = xt2 * factors2[0] + factors2[1];

            a = new Point((int) xt0, (int) yt0);
            b = new Point((int) xt1, (int) yt1);
            c = new Point((int) xt2, (int) yt2);
        }
        path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(a.x, a.y);
        path.lineTo(b.x, b.y);
        path.lineTo(c.x, c.y);
        path.lineTo(a.x, a.y);
        path.close();
    }


    public void buildTriangleStop(MapPoint mp1, MapPoint mp2) {

        Point a;
        Point b;
        Point c;

        if (mp1.getX() == mp2.getX()) {
            if (mp2.getY() > mp1.getY()) {
                a = new Point(mp2.getX(), mp2.getY() + 33);
                b = new Point(mp2.getX() - 22, mp2.getY());
                c = new Point(mp2.getX() + 22, mp2.getY());
            } else {
                a = new Point(mp2.getX(), mp2.getY() - 33);
                b = new Point(mp2.getX() - 22, mp2.getY());
                c = new Point(mp2.getX() + 22, mp2.getY());
            }

        } else {

            double[] factors1 = getFactors(mp2, mp1);
            double x1 = (double) mp2.getX();
            double y1 = (double) mp2.getY();
            double[] roots1 = quadraticEquationRoot(
                    1.0 + factors1[0] * factors1[0],
                    -2 * x1 + 2 * factors1[0] * factors1[1] - 2 * factors1[0] * y1,
                    x1 * x1 + y1 * y1 + factors1[1] * factors1[1] - 2 * y1 * factors1[1] - 3000);
            double xt0 = 0;
            if (mp1.getX() < mp2.getX()) {
                xt0 = Math.max(roots1[0], roots1[1]);
            } else {
                xt0 = Math.min(roots1[0], roots1[1]);
            }
            double yt0 = xt0 * factors1[0] + factors1[1];
            if (factors1[0] == 0) {
                factors1[0] += 0.00000001;
            }
            double[] factors2 = {-1 / factors1[0], y1 + 1 / factors1[0] * x1};   // - +
            double[] roots2 = quadraticEquationRoot(
                    1.0 + factors2[0] * factors2[0],
                    -2 * x1 + 2 * factors2[0] * factors2[1] - 2 * factors2[0] * y1,
                    x1 * x1 + y1 * y1 + factors2[1] * factors2[1] - 2 * y1 * factors2[1] - 500);
            double xt1 = Math.max(roots2[0], roots2[1]);
            double xt2 = Math.min(roots2[0], roots2[1]);
            double yt1 = xt1 * factors2[0] + factors2[1];
            double yt2 = xt2 * factors2[0] + factors2[1];
            a = new Point((int) xt0, (int) yt0);
            b = new Point((int) xt1, (int) yt1);
            c = new Point((int) xt2, (int) yt2);
        }

        path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(a.x, a.y);
        path.lineTo(b.x, b.y);
        path.lineTo(c.x, c.y);
        path.lineTo(a.x, a.y);
        path.close();


    }

    // rysowanie tego co jest w tablicy (ArrayList<MapPoint>) route
    private void drawMap(){

        //imageView = new TouchImageView(this);
        container.removeAllViews();
        //((BitmapDrawable)imageView.getDrawable()).getBitmap().recycle();
        //imageView = new TouchImageView(this);
        Log.i(BuildingActivity.class.getSimpleName(), "currentmapId: " + currentMapId);
        //imageView.setImageResource(currentMapId);
        //imageView.setImageBitmap(decodeResource(getResources(), currentMapId));
        //Bitmap m = decodeSampledBitmapFromResource(getResources(), currentMapId, 2000, 2000);     // DLA HASH MAP
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inPreferredConfig = Bitmap.Config.RGB_565;
        bmOptions.inMutable = true;
        Bitmap m = BitmapFactory.decodeFile(currentMapFile, bmOptions);
        Log.i(BuildingActivity.class.getSimpleName(), "config: " + m.getConfig());
        //imageView.setImageBitmap(m); <---- po co już teraz?
        // tworzenie kopii na której rysujemy linie

        //Bitmap lineOnBmp = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        //Bitmap copy = Bitmap.createBitmap(lineOnBmp);
        //Bitmap mutableBitmap = copy.copy(Bitmap.Config.RGB_565, true);
        //Bitmap mutableBitmap = convertToMutable(m);
        //Log.i(BuildingActivity.class.getSimpleName(), "config : " + m.getConfig());

        Canvas canvasCopy = new Canvas(m);

        // właściwości linii
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(8);

        // nakładanie linii na obrazek
        for (Line line : lines) {
            canvasCopy.drawLine(line.getStartX(),line.getStartY(), line.getStopX(), line.getStopY(), paint);
        }

        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAntiAlias(true);


        if (secondPointOnMap != null || lastPointOnMap != null) {
            if (secondPointOnMap != null || secondPointOnMap.getPrevious() != null || lastPointOnMap.getPrevious() != null || lastPointOnMap != null) {
                buildTriangleStart(secondPointOnMap.getPrevious(), secondPointOnMap);
                canvasCopy.drawPath(path, paint);

                buildTriangleStop(lastPointOnMap.getPrevious(), lastPointOnMap);
                canvasCopy.drawPath(path, paint);
            }
        }

        // TODO: trzeba wyskalowac + na kampusie sie nie pojawiaja

        // bitmapa z ikona drzwi
        Bitmap outDoorsBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.drzwi);
        Bitmap wcBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.wc);
        Bitmap buildingBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.drzwi); // TODO zmienić obrazek
        Bitmap liftBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.winda);

        // typ 3 -> outdoors
        for (MapPoint mapPoint : mapPoints) {
            if (currentMap.getId() == mapPoint.getMap().getId()) {
                if (mapPoint.getType() == 3) {
                    canvasCopy.drawBitmap(outDoorsBitmap, mapPoint.getX() - outDoorsBitmap.getWidth() / 2, mapPoint.getY() - outDoorsBitmap.getHeight() / 2, null);
                    Log.i(BuildingActivity.class.getSimpleName(), "Rysuję drzwi : X = " + mapPoint.getX() + ", Y = " + mapPoint.getY());
                } else if (mapPoint.getType() == 6 && mapPoint.getRoom().getFunction().equals(wc)) {
                    canvasCopy.drawBitmap(wcBitmap, mapPoint.getX() - wcBitmap.getWidth() / 2, mapPoint.getY() - wcBitmap.getHeight() / 2, null);
                    Log.i(BuildingActivity.class.getSimpleName(), "Rysuję kibel : X = " + mapPoint.getX() + ", Y = " + mapPoint.getY());
                } else if (mapPoint.getType() == 5) {
                    canvasCopy.drawBitmap(liftBitmap, mapPoint.getX() - liftBitmap.getWidth() / 2, mapPoint.getY() - liftBitmap.getHeight() / 2, null);
                    Log.i(BuildingActivity.class.getSimpleName(), "Rysuję windę : X = " + mapPoint.getX() + ", Y = " + mapPoint.getY());
                } else if (mapPoint.getType() == 2) { //drzwi pomiedzy budynkami
                    ArrayList<MapPointsArcs> edges = mapPoint.getEdges();
                    for (MapPointsArcs mpa : edges) {
                        if (mpa.getPoint1().getMap().getId() != mpa.getPoint2().getMap().getId()) {
                            canvasCopy.drawBitmap(outDoorsBitmap, mapPoint.getX() - outDoorsBitmap.getWidth() / 2, mapPoint.getY() - outDoorsBitmap.getHeight() / 2, null);
                            Log.i(BuildingActivity.class.getSimpleName(), "Rysuję drzwi : X = " + mapPoint.getX() + ", Y = " + mapPoint.getY());
                        }
                    }
                } else if (mapPoint.getBuilding() != null) {
                    canvasCopy.drawBitmap(buildingBitmap, mapPoint.getX() - buildingBitmap.getWidth() / 2, mapPoint.getY() - buildingBitmap.getHeight() / 2, null);
                    Log.i(BuildingActivity.class.getSimpleName(), "Rysuję budynek : X = " + mapPoint.getX() + ", Y = " + mapPoint.getY());
                }
            }
        }


        if (m.getHeight() > 4096 || m.getWidth() > 4096) {

            float aspect_ratio = ((float) m.getHeight()) / ((float) m.getWidth());

            Bitmap scaled;
            if (aspect_ratio < 1) {
                scaled = Bitmap.createScaledBitmap(m, 4096, (int) ((4096) * aspect_ratio), true);
            } else {
                scaled = Bitmap.createScaledBitmap(m, (int) (4096.0 / aspect_ratio), 4096, true);
            }
            /*Bitmap scaledBitmap = Bitmap.createBitmap(mutableBitmap, 0, 0,
                    (int) (4096 * 0.9),
                    (int) ((4096* 0.9) * aspect_ratio));
                    //Log.i(BuildingActivity.class.getSimpleName(), "max: " + GL10.GL_MAX_TEXTURE_SIZE);
                <------ nie kasować!
            */

            imageView.setImageBitmap(scaled);
        } else {
            imageView.setImageBitmap(m);
        }
        if (currentMap.getBuilding() == null) {
            aboutCurrentMap.setText("Kampus Piotrowo");
        } else {
            aboutCurrentMap.setText("Budynek: " + currentMap.getBuilding().getName()
                    + "\n Piętro: " + currentMap.getFloor());
        }

        container.addView(imageView);

    }

    public void nextMap(View view) {
        Log.i(BuildingActivity.class.getSimpleName(), "map id: " + currentMapId);
        if (currentPathMapId < pathMaps.size() - 1)
            currentPathMapId++;
        else
            return;

        changeMap(pathMaps.get(currentPathMapId).getFileName());
        Log.i(BuildingActivity.class.getSimpleName(), "map id: " + currentMapId);
        fillLines();
        drawMap();
    }

    public void previousMap(View view) {
        if (currentPathMapId > 0)
            currentPathMapId--;
        else
            return;

        changeMap(pathMaps.get(currentPathMapId).getFileName());
        fillLines(); //sprawdzic
        drawMap();
    }

    public void changeBuilding() {
        for (Map map : maps) {
            if (map.getBuilding() != null) {
                Log.i(BuildingActivity.class.getSimpleName(), "mapa z fora: " + map.getBuilding().getId());
                Log.i(BuildingActivity.class.getSimpleName(), "wybrany: " + chosenBuilding.getName());
                // TODO: aktualnie włączna mape gdzie floor = 0 (czyli czasem zamiast parteru, przyziemie)
                // trzeba wykorzystać mapPoint sąsiadujący z drzwiami wejściowymi i tam tam getMap()
                if (map.getId() == chosenMap.getId()) {
                    Log.i(BuildingActivity.class.getSimpleName(), "ZNALEZIONY BUDYNEK: " + map.getBuilding().getName());
                    changeMap(map.getFileName());
                }
            }
        }

        hideTouchableButtons();
        drawMap();
    }

    public void goInside(View view) {
        goInsideFunc();
    }

    public void goInsideFunc() {
        Log.i(BuildingActivity.class.getSimpleName(), "..........................................");
        // zmiana mapy na podstawie chosenBuilding -> domyslnie wybieramy parter tego budynku
        for (Map map : maps) {
            if (map.getBuilding() != null) {
                Log.i(BuildingActivity.class.getSimpleName(), "mapa z fora: " + map.getBuilding().getId());
                Log.i(BuildingActivity.class.getSimpleName(), "mapa z fora: " + map.getBuilding().getName());

                Log.i(BuildingActivity.class.getSimpleName(), "wybrany: " + chosenBuilding.getName());
                // TODO: aktualnie włączna mape gdzie floor = 0 (czyli czasem zamiast parteru, przyziemie)
                // trzeba wykorzystać mapPoint sąsiadujący z drzwiami wejściowymi i tam tam getMap()
                if (map.getBuilding() != null && map.getBuilding().getId() == chosenBuilding.getId() && map.getFloor() == 0) {
                    Log.i(BuildingActivity.class.getSimpleName(), "ZNALEZIONY BUDYNEK: " + map.getBuilding().getName());
                    changeMap(map.getFileName());
                }
            }
        }

        hideTouchableButtons();
        drawMap();

    }

    public void goOutside(View view) {
        goOutsideFunc();
    }

    private void goOutsideFunc(){
        Log.i(BuildingActivity.class.getSimpleName(), "wychodze ");
        // zmiana mapy na kampus
        for (Map map : maps) {
            if (map.getCampus() == 1) {
                changeMap(map.getFileName());
            }
        }
        verticalSeekBar.setMaximum(0);
        hideTouchableButtons();

        drawMap();
    }

    private void hideTouchableButtons(){
        buttonSetAsStartPoint.setEnabled(false);
        buttonSetAsStartPoint.setVisibility(View.INVISIBLE);
        buttonGoIn.setEnabled(false);
        buttonGoIn.setVisibility(View.INVISIBLE);
        buttonAboutBuilding.setEnabled(false);
        buttonAboutBuilding.setVisibility(View.INVISIBLE);
    }

    private Map getCurrentMap(){
        for(Map map : maps){
            if(currentMapId == map.getId())
                return map;
        }
        return null;
    }

    private void navigationModeOn(){
        navigationMode = true;
        nextMapButton.setEnabled(true);
        nextMapButton.setVisibility(View.VISIBLE);
        previousMapButton.setEnabled(true);
        previousMapButton.setVisibility(View.VISIBLE);
        escapeNavigationModeButton.setEnabled(true);
        escapeNavigationModeButton.setVisibility(View.VISIBLE);
        //chowamy pasek boczny
        if (getCurrentMap().getBuilding() != null && getCurrentMap().getBuilding().getNumberOfFloors() > 1) {
            verticalSeekBar.setEnabled(false);
            verticalSeekBar.setVisibility(View.INVISIBLE);
        }
        //schowaj te do wpisywania sal
    }

    private void navigationModeOff(){
        navigationMode = false;
        nextMapButton.setEnabled(false);
        nextMapButton.setVisibility(View.INVISIBLE);
        previousMapButton.setEnabled(false);
        previousMapButton.setVisibility(View.INVISIBLE);
        escapeNavigationModeButton.setEnabled(false);
        escapeNavigationModeButton.setVisibility(View.INVISIBLE);
        verticalSeekBar.setEnabled(true);
        verticalSeekBar.setVisibility(View.VISIBLE);
        lines.clear();
        //TODO currentmapy ITP też wyczyścić!
    }

    public void escapeNavigation(View view){
        navigationModeOff();
    }

    private void changeMap(String key){
        Map oldMap = currentMap;
        for(Map map : maps) {
            if(map.getFileName().equals(key)) {
                currentMapId = map.getId();
                Log.i(BuildingActivity.class.getSimpleName(), key);
                break;
            }
        }
        currentMap = getCurrentMap();
        currentMapFile = new File(mapsDir, currentMap.getFileName()).getAbsolutePath();
        // ustawienia suwaka po zmianie budynku

        // wychodzimy z budynku do kampusu
        // jeżeli oldMap.getBuilding jest nullem, tzn ze nastąpiła wejście do budynku z kampusu) i drugi
        // warunek nie powinien być sprawdzany (oldMap.getBuilding().getId() -> nullPointerExeption)
        // jeżeli nie jest nullem to można spokojnie sprawdzać drugi warunek(przejście BT <-> CW)
        if (currentMap.getCampus() == 1) {
            verticalSeekBar.setMaximum(0);
            currentBuildingMaps.clear();
        } else if (oldMap.getBuilding() == null || oldMap.getBuilding().getId() != currentMap.getBuilding().getId()) {
            currentBuildingMaps.clear();

            verticalSeekBar.setMaximum(currentMap.getBuilding().getNumberOfFloors() - 1);
            Log.i(BuildingActivity.class.getSimpleName(), "ILOSC PIĘTER BUDYNKU: " + currentMap.getBuilding().getNumberOfFloors());

            for (Map map : maps) {
                if (map.getBuilding() != null && map.getBuilding().getId() == currentMap.getBuilding().getId()) {
                    currentBuildingMaps.add(map);
                    Log.i(BuildingActivity.class.getSimpleName(), "Dodanie mapy do tablicy: " + map.getFileName());
                }
            }

            //sortowanie wg pięter

            Collections.sort(currentBuildingMaps);

        }
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    public static Bitmap convertToMutable(Bitmap imgIn) {
        try {
            //this is the file going to use temporally to save the bytes.
            // This file will not be a image, it will store the raw image data.
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "temp.tmp");

            //Open an RandomAccessFile
            //Make sure you have added uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
            //into AndroidManifest.xml file
            RandomAccessFile randomAccessFile = null;
            try {
                randomAccessFile = new RandomAccessFile(file, "rw");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            // get the width and height of the source bitmap.
            int width = imgIn.getWidth();
            int height = imgIn.getHeight();
            Bitmap.Config type = imgIn.getConfig();

            //Copy the byte to the file
            //Assume source bitmap loaded using options.inPreferredConfig = Config.ARGB_8888;
            FileChannel channel = randomAccessFile.getChannel();
            MappedByteBuffer map = null;
            try {
                map = channel.map(FileChannel.MapMode.READ_WRITE, 0, imgIn.getRowBytes() * height);
            } catch (IOException e) {
                e.printStackTrace();
            }
            imgIn.copyPixelsToBuffer(map);
            //recycle the source bitmap, this will be no longer used.
            imgIn.recycle();
            System.gc();// try to force the bytes from the imgIn to be released

            //Create a new bitmap to load the bitmap again. Probably the memory will be available.
            imgIn = Bitmap.createBitmap(width, height, type);
            map.position(0);
            //load it back from temporary
            imgIn.copyPixelsFromBuffer(map);
            //close the temporary file and channel , then delete that also
            channel.close();
            randomAccessFile.close();

            // delete the temp file
            file.delete();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imgIn;
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
            changeMap(currentBuildingMaps.get(seekBar.getProgress()).getFileName());
            drawMap();
            Log.i(BuildingActivity.class.getSimpleName(), "Zmiana piętra na: " + seekBar.getProgress());
        }

    };

}
