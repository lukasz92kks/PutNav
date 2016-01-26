package pl.poznan.put.putnav;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.sql.SQLException;
import java.util.ArrayList;

import pl.poznan.put.putnav.widgets.TouchImageView;
import pl.poznan.put.putnav.widgets.VerticalSeekBar;


public class BuildingActivity extends AppCompatActivity {

    AutoCompleteTextView aCTVFrom;
    AutoCompleteTextView aCTVTo;
    ArrayList<Room> allRooms;
    ArrayList<SearchItem> toBeShown = new ArrayList<SearchItem>();
    FrameLayout container;

    DatabaseHandler db;

    VerticalSeekBar verticalSeekBar = null;
    ImageView imageView = null;
    int[] images = {R.drawable.nano_0,
            R.drawable.nano_1,
            R.drawable.nano_2,
            R.drawable.nano_3,
            R.drawable.nano_4,
            R.drawable.nano_5
    };

    ArrayList<Line> lines = new ArrayList<Line>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building);

        init();

    }

    public void init() {

        db = OpenHelperManager.getHelper(this, DatabaseHandler.class);
        try {
            allRooms = new ArrayList<Room>(db.getRoomDao().queryForAll());
            Toast.makeText(BuildingActivity.this, "rooms: " + allRooms.size(), Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // TODO: funkcja która wypełnia tablice lines (koniecznie przed funkcją loadImageToContainer)

        container = (FrameLayout) findViewById(R.id.picture_container);

        loadImageToContainer(0);

        aCTVFrom = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
        aCTVTo = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView2);
        SearchAdapter adapter = new SearchAdapter(this, R.layout.listview_item_row, toBeShown);

        aCTVFrom.setAdapter(adapter);
        aCTVTo.setAdapter(adapter);
        //testy

        for (int i = 0; i < 5; i++) {
            int id = allRooms.get(i).getId();
            String type = "Room";
            String name = allRooms.get(i).getName();
            SearchItem sI = new SearchItem(id, type, name);
            toBeShown.add(sI);
        }

        //


        aCTVFrom.addTextChangedListener(myWatcher);
        aCTVTo.addTextChangedListener(myWatcher);


        verticalSeekBar = (VerticalSeekBar) findViewById(R.id.verticalSeekBar);
        verticalSeekBar.setOnSeekBarChangeListener(listenerSeekbar);
        //podajemy ilość pięter, np. 0 - budynek parterowy
        verticalSeekBar.setMaximum(4);

    }

    public final TextWatcher myWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //String regex = "(?i:.*" + s.toString() + ".*)";
            /*


            */

            toBeShown.clear();
            for (int i = 0; i < 5; i++) {
                int id = allRooms.get(i).getId();
                String type = "Room";
                String name = allRooms.get(i).getName();
                SearchItem sI = new SearchItem(id, type, name);
                toBeShown.add(sI);
            }
        }


        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void loadImageToContainer(int floor) {
        container.removeAllViews();
        imageView = new TouchImageView(this);

        imageView.setImageResource(images[floor]);

        //tworzenie kopii na której rysujemy linie

        Bitmap lineOnBmp = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        Bitmap copy = Bitmap.createBitmap(lineOnBmp);
        Bitmap mutableBitmap = copy.copy(Bitmap.Config.ARGB_8888, true);

        Canvas canvasCopy = new Canvas(mutableBitmap);

        //właściwości linii
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(10);

        //nakładanie linii na obrazek
        for (Line line : lines) {
            canvasCopy.drawLine(line.getStartX(),line.getStartY(), line.getStopX(), line.getStopY(), paint);
        }

        imageView.setImageBitmap(mutableBitmap);

        container.addView(imageView);
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
