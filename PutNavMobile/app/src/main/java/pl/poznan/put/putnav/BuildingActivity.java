package pl.poznan.put.putnav;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import java.io.File;

import pl.poznan.put.putnav.widgets.TouchImageView;
import pl.poznan.put.putnav.widgets.VerticalSeekBar;


public class BuildingActivity extends AppCompatActivity {

    ObjectHandler objectHandler = null;

    AutoCompleteTextView startingPointName;
    AutoCompleteTextView destinationPointName;
    FrameLayout container;
    FrameLayout navigationModeOnContainer;
    FrameLayout navigationModeOffContainer;

    RelativeLayout touchableMenuContainer;

    ImageButton buttonPreviousMap;
    ImageButton buttonNextMap;
    ImageButton buttonActivate;
    ImageButton buttonDeactivate;
    ImageButton exitBuilding;

    TextView currentPath;
    TextView textViewFloorNumber;
    TextView aboutCurrentMap;

    VerticalSeekBar verticalSeekBar = null;
    TouchImageView backgroundMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building);

        objectHandler = new ObjectHandler(this);

        assignWidgets();
        setStartingSettings();
        setSearchEngine();
        goToCampus();
        setListener();
        getWindow().setFormat(PixelFormat.RGB_565);
    }

    private void setSearchEngine() {
        ArrayAdapter<Object> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_dropdown_item_1line, objectHandler.getObjects());
        startingPointName.setAdapter(adapter);
        destinationPointName.setAdapter(adapter);
        setStartingPointListener();
        setDestinationPointListener();
    }

    private void assignWidgets() {
        backgroundMap = new TouchImageView(this);
        aboutCurrentMap = (TextView) findViewById(R.id.textViewCurrentMap);
        aboutCurrentMap.setVisibility(View.VISIBLE);
        container = (FrameLayout) findViewById(R.id.picture_container);
        navigationModeOnContainer = (FrameLayout) findViewById(R.id.navigation_on_container);
        navigationModeOffContainer = (FrameLayout) findViewById(R.id.navigation_off_container);
        touchableMenuContainer = (RelativeLayout) findViewById(R.id.touchable_menu_container);
        buttonNextMap = (ImageButton) findViewById(R.id.buttonNextMap);
        buttonPreviousMap = (ImageButton) findViewById(R.id.buttonPreviousMap);
        buttonActivate = (ImageButton) findViewById(R.id.activate_door);
        buttonActivate.setVisibility(View.INVISIBLE);
        buttonDeactivate = (ImageButton) findViewById(R.id.deactivate_door);
        buttonDeactivate.setVisibility(View.INVISIBLE);
        exitBuilding = (ImageButton) findViewById(R.id.exitBuilding);
        currentPath = (TextView) findViewById(R.id.textViewCurrentPath);
        textViewFloorNumber = (TextView) findViewById(R.id.textViewFloorNumber);
        exitBuilding.setVisibility(View.INVISIBLE);
        startingPointName = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
        destinationPointName = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView2);
        verticalSeekBar = (VerticalSeekBar) findViewById(R.id.verticalSeekBar);
        verticalSeekBar.setOnSeekBarChangeListener(listenerSeekBar);
        verticalSeekBar = (VerticalSeekBar) findViewById(R.id.verticalSeekBar);
        verticalSeekBar.setOnSeekBarChangeListener(listenerSeekBar);
    }

    private void setDestinationPointListener() {
        destinationPointName.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                if (arg0.getItemAtPosition(position) instanceof Room) {
                    Room chosenRoom = (Room) arg0.getItemAtPosition(position);
                    objectHandler.setRoomAsDestinationPoint(chosenRoom);
                } else if (arg0.getItemAtPosition(position) instanceof Building) {
                    Building chosenBuilding = (Building) arg0.getItemAtPosition(position);
                    objectHandler.setBuildingAsDestinationPoint(chosenBuilding);
                }
                hideKeyboard(destinationPointName.getApplicationWindowToken());
            }
        });
    }

    private void setStartingPointListener() {
        startingPointName.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                if (arg0.getItemAtPosition(position) instanceof Room) {
                    Room chosenRoom = (Room) arg0.getItemAtPosition(position);
                    objectHandler.setRoomAsStartingPoint(chosenRoom);

                } else if (arg0.getItemAtPosition(position) instanceof Building) {
                    Building chosenBuilding = (Building) arg0.getItemAtPosition(position);
                    objectHandler.setBuildingAsStartingPoint(chosenBuilding);
                }
                hideKeyboard(startingPointName.getApplicationWindowToken());
                //backgroundMap.setZoom(2.0f, 300, 0);
            }
        });
    }

    private void hideKeyboard(IBinder applicationWindowToken) {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(applicationWindowToken, 0);
    }

    private void setStartingSettings() {
        objectHandler.setNavigationMode(false);
        navigationModeOffContainer.setVisibility(View.VISIBLE);
        navigationModeOnContainer.setVisibility(View.INVISIBLE);
        startingPointName.setVisibility(View.VISIBLE);
        destinationPointName.setVisibility(View.VISIBLE);
    }

    private void showActivateButtons(MapPoint mapPoint) {
        if (mapPoint.isDeactivated()) {
            buttonActivate.setEnabled(true);
            buttonActivate.setVisibility(View.VISIBLE);

        } else {
            buttonDeactivate.setEnabled(true);
            buttonDeactivate.setVisibility(View.VISIBLE);
        }
    }

    public void setListener() {
        backgroundMap.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    final float[] coordinates = getTouchPointCoords(event);
                    int x = (int) coordinates[0];
                    int y = (int) coordinates[1];

                    if (objectHandler.getCurrentMap().isCampus()) {
                        searchForBuildingInArea(x, y);
                    } else if (!objectHandler.getCurrentMap().isCampus()) {
                        searchForSpecialPointsInBuilding(x, y);
                    }
                }
                return false;
            }
        });
    }

    private float[] getTouchPointCoords(MotionEvent event) {
        Matrix matrix = new Matrix();
        backgroundMap.getImageMatrix().invert(matrix);
        final float[] coords = new float[]{event.getX(), event.getY()};
        matrix.postTranslate(backgroundMap.getScrollX(), backgroundMap.getScrollY());
        matrix.mapPoints(coords);
        return coords;
    }

    private void searchForSpecialPointsInBuilding(int x, int y) {
        MapPoint mapPoint = objectHandler.findSpecialPoint(x, y);
        if (mapPoint == null) {
            return;
        }

        switch (mapPoint.getType()) {
            //OUTDOOR and LIFT can be deactivated
            case OUTDOOR:
            case LIFT:
                objectHandler.setChosenMapPoint(mapPoint);
                showActivateButtons(mapPoint);
            case DOOR:
                changeBuilding(mapPoint);
        }
        hideActivateButtons();
    }

    private void searchForBuildingInArea(int x, int y) {
        if (objectHandler.findBuildingSuccessful(x,y)){
            touchableMenuContainer.setVisibility(View.VISIBLE);
        } else {
            hideTouchableButtons();
        }
    }

    public void deactivateMapPoint(View view) {
        objectHandler.deactivateMapPoint();
        hideActivateButtons();
    }

    public void activateMapPoint(View view) {
        objectHandler.activateMapPoint();
        hideActivateButtons();
    }

    public void hideActivateButtons() {
        buttonDeactivate.setEnabled(false);
        buttonDeactivate.setVisibility(View.INVISIBLE);
        buttonActivate.setVisibility(View.INVISIBLE);
        buttonActivate.setEnabled(false);
    }

    public String getDepartments() {
        String s = "";
        s += "Wydziały:\n";
        s += "Wydział Informatyki\n";
        return s;
    }

    public void aboutBuilding(View view) {
        Building building = objectHandler.getChosenBuilding();
        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.popup_about_building, null);
        final PopupWindow popupWindow = new PopupWindow(
                popupView,
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        TextView textViewBuilding = (TextView) popupView.findViewById(R.id.textViewBuilding);
        textViewBuilding.setText(building.getName() + "\n" + building.getAddress() + "\n" + getDepartments());

        Button btnDismiss = (Button) popupView.findViewById(R.id.buttonClose);
        ImageView imageViewBuilding = (ImageView) popupView.findViewById(R.id.imageViewBuilding);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inPreferredConfig = Bitmap.Config.RGB_565;
        bmOptions.inMutable = true;
        String photoFile = objectHandler.findPhoto();
        if (photoFile != null) {
            File file = new File(objectHandler.getImagesDir(), photoFile);
            Log.i(BuildingActivity.class.getSimpleName(), file.getAbsolutePath() + " " + file.exists());
            Bitmap buildingBitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), bmOptions);
            imageViewBuilding.setImageBitmap(buildingBitmap);
        }
        btnDismiss.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        popupWindow.setFocusable(true);
        popupWindow.update();
    }

    public void reversePlaces(View view) {
        objectHandler.reverseMapPoints();
        reverseSearchNames();
    }

    private void reverseSearchNames() {
        String temporaryName = startingPointName.getText().toString();
        startingPointName.setText(destinationPointName.getText().toString());
        destinationPointName.setText(temporaryName);
    }

    public void searchPath(View view) {
        if (objectHandler.areBothPointsNull()) {
            return;
        }

        if (objectHandler.isOnlyOneUniqueMapPoint()) {
            findPointOnMap();
        } else {
            findRouteBetweenTwoPoints();
        }
    }

    private void findPointOnMap() {
        Map targetMap = objectHandler.findMap();
        objectHandler.setNewCurrentMap(targetMap);
        drawMap();
    }

    private void findRouteBetweenTwoPoints() {
        objectHandler.clearExistingPath();
        objectHandler.findNewRoute();
        if (objectHandler.isRouteEmpty()) return;

        navigationModeOn();
        objectHandler.addMapsOfChosenRoute();
        objectHandler.loadFirstMapOfRoute();
        objectHandler.fillLines();
        drawMap();
        setRouteDescription();
    }

    private void setRouteDescription() {
        String mp1 = objectHandler.getStartingPointName();
        String mp2 = objectHandler.getDestinationPointName();
        String currentRoute = "Z: " + mp1 + "\nDO: " + mp2;
        currentPath.setText(currentRoute);
    }

    private void drawMap() {
        Bitmap mapBitmap = objectHandler.createReadyBitmap();
        setMapLabel();
        container.removeAllViews();
        backgroundMap.setImageBitmap(mapBitmap);
        container.addView(backgroundMap);

//        RectF drawableRect = new RectF(0, 0, backgroundMap.getWidth(), backgroundMap.getHeight());
//        RectF viewRect = new RectF(0, 0, backgroundMap.getWidth(), backgroundMap.getHeight());
//        Matrix matrix = new Matrix();
//        matrix.setRectToRect(drawableRect, viewRect, Matrix.ScaleToFit.CENTER);
//        backgroundMap.setImageMatrix(matrix);
    }

    private void setMapLabel() {
        if (objectHandler.getCurrentMap().getBuilding() == null) {
            aboutCurrentMap.setText("Kampus Piotrowo");
        } else {
            aboutCurrentMap.setText("Budynek: " + objectHandler.getCurrentMap().getBuilding().getName()
                    + "\n Piętro: " + (objectHandler.getCurrentMap().getFloor()));
        }
    }

    public void setStartPoint(View view) {
        objectHandler.setStartingPoint();
        startingPointName.setText(objectHandler.getChosenBuilding().getName());
        backgroundMap.resetZoom();
    }

    public void nextMap(View view) {
        if (!objectHandler.isNextMap())
            return;

        objectHandler.incrementCurrentPathMapNumber();
        Map newMap = objectHandler.getCurrentPathMap();
        objectHandler.setNewCurrentMap(newMap);
        objectHandler.fillLines();
        buttonPreviousMap.setVisibility(View.VISIBLE);
        buttonPreviousMap.setEnabled(true);
        if (!objectHandler.isNextMap()) {
            buttonNextMap.setVisibility(View.INVISIBLE);
            buttonNextMap.setEnabled(false);
        }
        drawMap();
    }

    public void previousMap(View view) {
        if (!objectHandler.isPreviousMap())
            return;

        objectHandler.decrementCurrentPathMapNumber();
        Map newMap = objectHandler.getCurrentPathMap();
        objectHandler.setNewCurrentMap(newMap);
        objectHandler.fillLines();
        buttonNextMap.setVisibility(View.VISIBLE);
        buttonNextMap.setEnabled(true);
        if (!objectHandler.isPreviousMap()) {
            buttonPreviousMap.setVisibility(View.INVISIBLE);
            buttonPreviousMap.setEnabled(false);
        }
        drawMap();
    }

    private void changeBuilding(MapPoint mapPoint) {
        Map newMap = objectHandler.FindMapOfBuildingOnTheOtherSideOfDoor(mapPoint);
        objectHandler.setNewCurrentMap(newMap);
        hideTouchableButtons();
        drawMap();
        setVerticalSeekBar();
    }

    public void goInside(View view) {
        Map newMap = objectHandler.findMapOfChosenBuilding();
        objectHandler.setNewCurrentMap(newMap);
        hideTouchableButtons();
        drawMap();
        setVerticalSeekBar();
        exitBuilding.setEnabled(true);
        exitBuilding.setVisibility(View.VISIBLE);
    }

    public void goOutside(View view) {
        goToCampus();
    }

    private void goToCampus() {
        Map newMap = objectHandler.findCampusMap();
        objectHandler.setNewCurrentMap(newMap);
        hideTouchableButtons();
        drawMap();
        setVerticalSeekBar();
        exitBuilding.setEnabled(false);
        exitBuilding.setVisibility(View.INVISIBLE);
    }

    private void hideTouchableButtons() {
        touchableMenuContainer.setVisibility(View.INVISIBLE);
    }

    private void navigationModeOn() {
        objectHandler.setNavigationMode(true);
        buttonNextMap.setEnabled(true);
        buttonNextMap.setVisibility(View.VISIBLE);
        buttonPreviousMap.setVisibility(View.INVISIBLE);
        buttonPreviousMap.setEnabled(false);
        navigationModeOnContainer.setVisibility(View.VISIBLE);
        navigationModeOffContainer.setVisibility(View.INVISIBLE);
        startingPointName.setVisibility(View.INVISIBLE);
        destinationPointName.setVisibility(View.INVISIBLE);
    }

    private void setVerticalSeekBar() {
        if (objectHandler.getCurrentMap().isCampus()){
            objectHandler.clearCurrentBuildingMaps();
            verticalSeekBar.setMaximum(0);
        } else {
            objectHandler.addCurrentBuildingMaps();
            int floor = objectHandler.getCurrentMap().getFloor() + objectHandler.getDifference();
            verticalSeekBar.setMaximum(objectHandler.getCurrentMap().getBuilding().getNumberOfFloors() - 1);
            verticalSeekBar.setProgressAndThumb(floor);
        }
    }

    private void navigationModeOff() {
        objectHandler.setNavigationMode(false);
        navigationModeOffContainer.setVisibility(View.VISIBLE);
        navigationModeOnContainer.setVisibility(View.INVISIBLE);
        startingPointName.setVisibility(View.VISIBLE);
        destinationPointName.setVisibility(View.VISIBLE);
        objectHandler.setSecondPointOnMap(null);
        objectHandler.setLastPointOnMap(null);
        objectHandler.clearLines();
        drawMap();
        setVerticalSeekBar();
    }

    public void escapeNavigation(View view) {
        navigationModeOff();
    }

    private SeekBar.OnSeekBarChangeListener listenerSeekBar = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            int currentFloor = progress - objectHandler.getDifference();
            textViewFloorNumber.setText(String.valueOf(currentFloor));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            int floor = objectHandler.getCurrentMap().getFloor();
            textViewFloorNumber.setText(String.valueOf(floor));
            textViewFloorNumber.setVisibility(View.VISIBLE);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            textViewFloorNumber.setVisibility(View.INVISIBLE);
            Map newMap = objectHandler.getCurrentBuildingMaps().get(seekBar.getProgress());
            if (newMap.getId() != objectHandler.getCurrentMap().getId()) {
                objectHandler.setNewCurrentMap(newMap);
                drawMap();
                setVerticalSeekBar();
            }
        }
    };
}