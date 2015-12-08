package pl.poznan.put.putnav;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;

import pl.poznan.put.putnav.widgets.TouchImageView;
import pl.poznan.put.putnav.widgets.VerticalSeekBar;


public class BuildingActivity extends AppCompatActivity {

    VerticalSeekBar verticalSeekBar = null;
    ImageView imageView = null;
    int[] images = {R.drawable.nano_0,
            R.drawable.nano_1,
            R.drawable.nano_2,
            R.drawable.nano_3,
            R.drawable.nano_4,
            R.drawable.nano_5
    };

    FrameLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building);

        container = (FrameLayout) findViewById(R.id.picture_container);

        loadImageToContainer(0);

        verticalSeekBar = (VerticalSeekBar) findViewById(R.id.verticalSeekBar);
        verticalSeekBar.setOnSeekBarChangeListener(listenerSeekbar);
        //podajemy ilość pięter, np. 0 - budynek parterowy
        verticalSeekBar.setMaximum(4);
    }

    private void loadImageToContainer(int floor) {
        container.removeAllViews();
        imageView = new TouchImageView(this);
        imageView.setImageResource(images[floor]);
        container.addView(imageView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu;
        //this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

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
