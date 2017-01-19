package pl.poznan.put.putnav;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Environment;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class ObjectHandler {

    private Context context;
    private static final String OBJECTS_FILE_NAME = "objects.ser";
    private static final String PREFERENCES_NAME = "appPreferences";
    private static final String PREFERENCE_DISABLED = "disabled";
    private String mapsDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "putnavadmin/maps").getAbsolutePath();
    private String imagesDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "putnavadmin/images").getAbsolutePath();
    private String objectsDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "putnavadmin/objects").getAbsolutePath();

    private DatabaseHandler db;

    private ArrayList<MapPoint> mapPoints;
    private ArrayList<MapPointsArcs> mapPointsArcs;
    private ArrayList<Photo> photos;
    private ArrayList<Department> departments;
    private ArrayList<MapPoint> route;
    private ArrayList<Room> rooms;
    private ArrayList<Map> maps;

    private List<Map> currentBuildingMaps = new ArrayList<>();
    private ArrayList<Building> buildings = new ArrayList<>();
    private MapPoint startingPoint;
    private MapPoint destinationPoint;
    private MapPoint secondPointOnMap;
    private MapPoint lastPointOnMap;
    private MapPoint chosenMapPoint;
    private Map currentMap;
    private int currentPathMapNumber;

    List<Object> getObjects() {
        return objects;
    }

    private List<Object> objects = new ArrayList<>();
    private ArrayList<Line> lines = new ArrayList<>();
    private ArrayList<Map> pathMaps;
    private ArrayList<Room> roomsUniqueList;
    private Building chosenBuilding;
    private boolean disabled;
    private boolean navigationMode = false;
    private Drawer drawer;
    private double doorBitmapScale = 1.0;
    private double liftBitmapScale = 3.0;
    private double wcBitmapScale = 2.0;
    private Arrow triangleHelper = new Arrow();

    ObjectHandler(Context context) {
        this.context = context;
        drawer = new Drawer();
        loadPreferences();
        loadObjects();

        copyResourcesIfTheyDontAlreadyExist();
        loadRooms();
        loadBuildings();
    }

    private void loadObjects() {
        if (existSavedObjects()) {
            loadObjectsFromFile();
        } else {
            loadDb();
            setMapPointsTypes();
            calculateArcsWeights();
            addNeighboursForEveryMapPoint();
            saveObjectsToFile();
        }
    }

    private boolean existSavedObjects() {
        File dir = new File(objectsDir + "objects.ser");
        return dir.exists();
    }

    private void saveObjectsToFile() {
        SerializedObjects ob = new SerializedObjects(
                mapPoints,
                mapPointsArcs,
                maps,
                buildings,
                photos,
                departments,
                rooms
        );

        makeDirIfDoesntAlreadyExist();

        try (
                FileOutputStream fileOut = new FileOutputStream(new File(objectsDir + "objects.ser"));
                ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(fileOut))
        ) {
            out.writeObject(ob);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void makeDirIfDoesntAlreadyExist() {
        File dir = new File(objectsDir);
        if (!dir.exists()) {
            try {
                dir.mkdirs();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadObjectsFromFile() {
        try (
                FileInputStream fileIn = new FileInputStream(new File(objectsDir + OBJECTS_FILE_NAME));
                ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(fileIn))
        ) {
            SerializedObjects ob = (SerializedObjects) in.readObject();
            assignObjects(ob);

        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            c.printStackTrace();
        }
    }

    private void assignObjects(SerializedObjects ob) {
        mapPoints = ob.getMapPoints();
        mapPointsArcs = ob.getMapPointsArcs();
        buildings = ob.getBuildings();
        maps = ob.getMaps();
        photos = ob.getPhotos();
        departments = ob.getDepartments();
        rooms = ob.getRooms();
    }

    private void loadDb() {
        db = OpenHelperManager.getHelper(context, DatabaseHandler.class);
        try {
            mapDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void mapDatabase() throws SQLException {
        mapPoints = new ArrayList<>(db.getMapPointDao().queryForAll());
        mapPointsArcs = new ArrayList<>(db.getMapPointsArcsDao().queryForAll());
        buildings = new ArrayList<>(db.getBuildingDao().queryForAll());
        rooms = new ArrayList<>(db.getRoomDao().queryForAll());
        maps = new ArrayList<>(db.getMapDao().queryForAll());
        photos = new ArrayList<>(db.getPhotoDao().queryForAll());
        departments = new ArrayList<>(db.getDepartmentDao().queryForAll());
    }

    private void setMapPointsTypes() {
        for (MapPoint m : mapPoints) {
            m.setType();
        }
    }

    private void calculateArcsWeights() {
        for (MapPointsArcs mpa : mapPointsArcs) {
            mpa.setPoint1(mapPoints);
            mpa.setPoint2(mapPoints);
            mpa.calculateWeight(disabled);
        }
    }

    private void addNeighboursForEveryMapPoint() {
        for (MapPoint mp : mapPoints) {
            mp.searchEdges(mapPointsArcs);
        }
    }

    private void loadRooms() {
        roomsUniqueList = new ArrayList<>();
        for (Room r : rooms) {
            if (!isRoomAlreadyOnList(r)) {
                roomsUniqueList.add(r);
            }
        }

        for (Room r : roomsUniqueList) {
            objects.add(r);
        }
    }

    private boolean isRoomAlreadyOnList(Room r) {
        for (Room current : roomsUniqueList) {
            if (current.getName() != null && current.getName().equals(r.getName())) {
                return true;
            }
        }
        return false;
    }

    private void loadBuildings() {
        for (Building b : buildings) {
            objects.add(b);
        }
    }

    private void loadPreferences() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, 0);
        if (sharedPreferences.contains("exists")) {
            disabled = sharedPreferences.getBoolean(PREFERENCE_DISABLED, false);
        }
    }

    private void copyResourcesIfTheyDontAlreadyExist() {
        File dir = new File(mapsDir);
        if (!dir.exists()) {

            try {
                dir.mkdirs();
            } catch (SecurityException e) {
                e.printStackTrace();
            }

            try {
                for (int i = 0; i < maps.size(); i++) {
                    CopyRAWtoSDCard(context.getResources().getIdentifier(maps.get(i).getDrawableName(), "drawable",
                            context.getPackageName()), new File(mapsDir, maps.get(i).getFileName()).getAbsolutePath());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void CopyRAWtoSDCard(int id, String path) throws IOException {

        byte[] buff = new byte[1024];
        int read;
        try (
                InputStream in = context.getResources().openRawResource(id);
                FileOutputStream out = new FileOutputStream(path)
        ) {
            while ((read = in.read(buff)) > 0) {
                out.write(buff, 0, read);
            }
        }
    }

    void setBuildingAsDestinationPoint(Building b) {
        for (MapPoint m : mapPoints) {
            if (m.getBuilding() != null && m.getBuilding().getId() == b.getId()) {
                destinationPoint = m;
            }
        }
    }

    void setRoomAsDestinationPoint(Room r) {
        for (MapPoint m : mapPoints) {
            if (m.getRoom() != null && m.getRoom().getId() == r.getId()) {
                destinationPoint = m;
            }
        }
    }

    void setBuildingAsStartingPoint(Building chosenBuilding) {
        for (MapPoint m : mapPoints) {
            if (m.getBuilding() != null && m.getBuilding().getId() == chosenBuilding.getId()) {
                startingPoint = m;
            }
        }
    }

    void setRoomAsStartingPoint(Room r) {
        for (MapPoint m : mapPoints) {
            if (m.getRoom() != null && m.getRoom().getId() == r.getId()) {
                startingPoint = m;
            }
        }
    }

    MapPoint findSpecialPoint(int x, int y) {
        for (MapPoint m : mapPoints) {
            if (m.getMap().getId() != currentMap.getId()) continue;

            if (isInAreaOfHit(x, y, m) && (
                    m.getType() == MapPoint.MapPointTypes.OUTDOOR
                            || m.getType() == MapPoint.MapPointTypes.LIFT
                            || m.getType() == MapPoint.MapPointTypes.DOOR
            )) {
                return m;
            }
        }
        return null;

    }

    boolean findBuildingSuccessful(int x, int y) {
        for (MapPoint m : mapPoints) {
            if (m.getTypeId() == 7 && isInAreaOfHit(x, y, m)) {
                chosenBuilding = m.getBuilding();
                return true;
            }
        }
        return false;
    }

    private boolean isInAreaOfHit(int x, int y, MapPoint m) {
        double distance = Math.sqrt((double) ((x - m.getX()) * (x - m.getX()) + (y - m.getY()) * (y - m.getY())));
        return distance < 30.0;
    }

    void deactivateMapPoint() {
        for (MapPoint m : mapPoints) {
            if (m.getId() == chosenMapPoint.getId()) {
                m.setDeactivated(true);
            }
        }
    }

    void activateMapPoint() {
        for (MapPoint m : mapPoints) {
            if (m.getId() == chosenMapPoint.getId()) {
                m.setDeactivated(false);
            }
        }
    }

    String findPhoto() {
        for (Photo photo : photos) {
            if (photo.getBuilding().getId() == chosenBuilding.getId())
                return photo.getFile();
        }
        return null;
    }

    void reverseMapPoints() {
        MapPoint temporaryMapPoint = startingPoint;
        startingPoint = destinationPoint;
        destinationPoint = temporaryMapPoint;
    }

    Map findMap() {
        MapPoint targetPoint;
        if (startingPoint != null) {
            targetPoint = startingPoint;
        } else {
            targetPoint = destinationPoint;
        }
        Map targetMap = new Map();
        for (Map m : maps) {
            if (targetPoint.getMap().getId() == m.getId()) {
                targetMap = m;
            }
        }
        return targetMap;
    }

    boolean areBothPointsNull() {
        return startingPoint == null && destinationPoint == null;
    }

    boolean isOnlyOneUniqueMapPoint() {
        return startingPoint == destinationPoint || startingPoint == null || destinationPoint == null;
    }

    private String getMapPointName(MapPoint m) {
        String s = "";
        if (m.getRoom() != null) {
            s = m.getRoom().getName();
        } else if (m.getBuilding() != null) {
            s = m.getBuilding().getName();
        }
        return s;
    }

    void clearExistingPath() {
        for (MapPoint m : mapPoints) {
            m.setPrevious(null);
            m.setDistance(Double.longBitsToDouble(0x7ff0000000000000L));
        }
        lines.clear();

    }

    void findNewRoute() {
        RouteFinder routeFinder = new RouteFinder(mapPoints, mapPointsArcs);
        pathMaps = new ArrayList<>();
        route = routeFinder.findPath(startingPoint, destinationPoint);
    }

    void addMapsOfChosenRoute() {
        Map lastMap = null;
        for (MapPoint mapPoint : route) {
            if (lastMap != null
                    && lastMap.getId() == mapPoint.getMap().getId()
                    || mapPoint.getType() == MapPoint.MapPointTypes.STAIRS
                    || mapPoint.getType() == MapPoint.MapPointTypes.LIFT)
            {
                continue;
            }

            pathMaps.add(mapPoint.getMap());
            lastMap = mapPoint.getMap();
        }
    }

    void fillLines() {
        clearLines();
        ArrayList<MapPoint> currentMapPoints = new ArrayList<>();
        for (MapPoint mp : route) {
            if (mp.getMap().getId() == currentMap.getId()) {
                currentMapPoints.add(mp);
            }
        }

        MapPoint previousMapPoint = null;

        for (MapPoint currentMapPoint : currentMapPoints) {
            if (previousMapPoint == null) {
                previousMapPoint = currentMapPoint;
                continue;
            }
            lines.add(new Line(
                    previousMapPoint.getX(),
                    previousMapPoint.getY(),
                    currentMapPoint.getX(),
                    currentMapPoint.getY()
            ));
            previousMapPoint = currentMapPoint;
        }
        lastPointOnMap = previousMapPoint;
        if (currentMapPoints.size() > 1) {
            secondPointOnMap = currentMapPoints.get(1);
        }
    }

    void setStartingPoint() {
        for (MapPoint m : mapPoints) {
            if (m.getBuilding() != null && m.getBuilding().getId() == chosenBuilding.getId()) {
                startingPoint = m;

            }
        }
    }

    Map findMapOfChosenBuilding() {
        for (Map map : maps) {
            if(!map.isCampus() && map.getBuilding().getId() == chosenBuilding.getId()){
                return map;
            }
        }
        return null;
    }

    Map findCampusMap() {
        for (Map map : maps) {
            if (map.isCampus()) {
                return map;
            }
        }
        return null;
    }

    void setNewCurrentMap(Map newMap) {
        currentMap = newMap;
    }

    void addCurrentBuildingMaps() {
        currentBuildingMaps.clear();
        for (Map map : maps) {
            if (map.getBuilding() != null && map.getBuilding().getId() == currentMap.getBuilding().getId()) {
                currentBuildingMaps.add(map);
            }
        }
        Collections.sort(currentBuildingMaps);
    }

    void clearLines() {
        lines.clear();
    }

    void clearCurrentBuildingMaps() {
        currentBuildingMaps.clear();
    }

    boolean isNextMap() {
        return currentPathMapNumber < pathMaps.size() - 1;
    }

    boolean isPreviousMap() {
        return currentPathMapNumber > 0;
    }

    boolean isRouteEmpty() {
        return route.isEmpty();
    }

    List<Map> getCurrentBuildingMaps() {
        return currentBuildingMaps;
    }

    Map getCurrentMap() {
        return currentMap;
    }

    void setChosenMapPoint(MapPoint chosenMapPoint) {
        this.chosenMapPoint = chosenMapPoint;
    }

    Building getChosenBuilding() {
        return chosenBuilding;
    }

    void incrementCurrentPathMapNumber() {
        currentPathMapNumber++;
    }

    private String getAbsolutePathToMap() {
        return new File(mapsDir, currentMap.getFileName()).getAbsolutePath();
    }

    void decrementCurrentPathMapNumber() {
        currentPathMapNumber--;
    }

    void setSecondPointOnMap(MapPoint newSecondPointOnMap) {
        secondPointOnMap = newSecondPointOnMap;
    }

    void setLastPointOnMap(MapPoint newLastPointOnMap) {
        lastPointOnMap = newLastPointOnMap;
    }

    String getImagesDir() {
        return imagesDir;
    }

    void setNavigationMode(boolean navigationMode) {
        this.navigationMode = navigationMode;
    }

    int getDifference() {
        if (currentMap.getBuilding().getId() == 3) {
            return 1;
        } else if (currentMap.getBuilding().getId() == 4) {
            return 2;
        } else {
            return 0;
        }
    }

    Bitmap createReadyBitmap() {
        Bitmap mapBitmap = drawer.createMapBitmap(getAbsolutePathToMap());
        Canvas canvasCopy = new Canvas(mapBitmap);
        drawIcons(canvasCopy);

        if (navigationMode) {
            Paint paint = drawer.setLineStyle();
            drawer.drawLines(canvasCopy, paint, lines);
            drawArrow(canvasCopy, paint);
        }


        if (isResolutionTooHigh(mapBitmap)) {
            mapBitmap = drawer.createScaledMap(mapBitmap);
        }
        return mapBitmap;
    }

    private void drawIcons(Canvas canvasCopy) {

        Bitmap outDoorsBitmapTmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.drzwi);
        Bitmap outDoorsBitmap = Bitmap.createScaledBitmap(outDoorsBitmapTmp, (int) (outDoorsBitmapTmp.getWidth() / doorBitmapScale), (int) (outDoorsBitmapTmp.getHeight() / doorBitmapScale), false);
        Bitmap wcBitmapTmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.wc);
        Bitmap wcBitmap = Bitmap.createScaledBitmap(wcBitmapTmp, (int) (wcBitmapTmp.getWidth() / wcBitmapScale), (int) (wcBitmapTmp.getHeight() / wcBitmapScale), false);
        Bitmap buildingBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.drzwi);
        Bitmap liftBitmapTmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.winda);
        Bitmap liftBitmap = Bitmap.createScaledBitmap(liftBitmapTmp, (int) (liftBitmapTmp.getWidth() / liftBitmapScale), (int) (liftBitmapTmp.getHeight() / liftBitmapScale), false);

        for (MapPoint mapPoint : mapPoints) {
            if (mapPoint.getMap().getId() == currentMap.getId()) {
                switch (mapPoint.getType()) {
                    case OUTDOOR:
                        canvasCopy.drawBitmap(outDoorsBitmap, mapPoint.getX() - outDoorsBitmap.getWidth() / 2, mapPoint.getY() - outDoorsBitmap.getHeight() / 2, null);
                        break;
                    case ROOM:
                        if (mapPoint.getRoom().getFunction().equals("WC"))
                            canvasCopy.drawBitmap(wcBitmap, mapPoint.getX() - wcBitmap.getWidth() / 2, mapPoint.getY() - wcBitmap.getHeight() / 2, null);
                        break;
                    case LIFT:
                        canvasCopy.drawBitmap(liftBitmap, mapPoint.getX() - liftBitmap.getWidth() / 2, mapPoint.getY() - liftBitmap.getHeight() / 2, null);
                        break;
                    case DOOR:
                        ArrayList<MapPointsArcs> edges = mapPoint.getEdges();
                        for (MapPointsArcs edge : edges) {
                            if (arePointsOfEdgesOnDifferentMap(edge)) {
                                canvasCopy.drawBitmap(outDoorsBitmap, mapPoint.getX() - outDoorsBitmap.getWidth() / 2, mapPoint.getY() - outDoorsBitmap.getHeight() / 2, null);
                            }
                        }
                        break;
                    case BUILDING:
                        canvasCopy.drawBitmap(buildingBitmap, mapPoint.getX() - buildingBitmap.getWidth() / 2, mapPoint.getY() - buildingBitmap.getHeight() / 2, null);
                        break;
                }
            }
        }
    }

    private void drawArrow(Canvas canvasCopy, Paint paint) {
        Path triangle;
        triangle = triangleHelper.buildTriangleStart(secondPointOnMap.getPrevious(), secondPointOnMap);
        canvasCopy.drawPath(triangle, paint);
        triangle = triangleHelper.buildTriangleStop(lastPointOnMap.getPrevious(), lastPointOnMap);
        canvasCopy.drawPath(triangle, paint);
    }

    void loadFirstMapOfRoute() {
        setNewCurrentMap(pathMaps.get(0));
    }

    private boolean isResolutionTooHigh(Bitmap mapBitmap) {
        return mapBitmap.getHeight() > 4096 || mapBitmap.getWidth() > 4096;
    }

    String getStartingPointName() {
        return getMapPointName(startingPoint);
    }

    String getDestinationPointName() {
        return getMapPointName(destinationPoint);
    }

    Map FindMapOfBuildingOnTheOtherSideOfDoor(MapPoint mapPoint) {
        ArrayList<MapPointsArcs> edges = mapPoint.getEdges();
        for (MapPointsArcs mpa : edges) {
            if (arePointsOfEdgesOnDifferentMap(mpa)) {
                return (mpa.getPoint1().getId() == mapPoint.getId()) ?
                        mpa.getPoint2().getMap() : mpa.getPoint1().getMap();
            }
        }
        return null;
    }

    private boolean arePointsOfEdgesOnDifferentMap(MapPointsArcs mpa) {
        return mpa.getPoint1().getMap().getId() != mpa.getPoint2().getMap().getId();
    }

    Map getCurrentPathMap() {
        return pathMaps.get(currentPathMapNumber);
    }
}

