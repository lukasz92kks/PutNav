package pl.poznan.put.putnav;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.EditText;

import java.net.MalformedURLException;
import java.net.URL;

public class PreferencesActivity extends AppCompatActivity {

    private static final String PREFERENCES_NAME = "appPreferences";
    private static final String PREFERENCE_DISABLED = "disabled";

    private Button btn6;
    private Button btn7;
    private Button buttonUpdate;
    private CheckBox checkBox;
    private EditText textViewServer;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        sharedPreferences = getSharedPreferences(PREFERENCES_NAME, 0);
        init();
        loadPreferences();
    }

    public void loadPreferences() {

        if (sharedPreferences.contains("exists")) {
            boolean disabled = sharedPreferences.getBoolean(PREFERENCE_DISABLED, false);
            checkBox.setChecked(disabled);

            textViewServer.setText(sharedPreferences.getString("serverAddress", "brak"));
        }
    }

    public void init() {
        btn6 = (Button) findViewById(R.id.button6); //save
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveChanges();
            }
        });

        btn7 = (Button) findViewById(R.id.button7); //cancel
        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonUpdate = (Button) findViewById(R.id.buttonUpdate); //cancel
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });

        checkBox = (CheckBox) findViewById(R.id.checkBox1);

        textViewServer = (EditText) findViewById(R.id.serverAddress);
    }

    public void saveChanges() {
        boolean a = checkBox.isChecked();
        int language = 0;
        SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();
        preferencesEditor.putBoolean(PREFERENCE_DISABLED, a);
        preferencesEditor.putBoolean("exists", true);
        preferencesEditor.putInt("language", language);
        preferencesEditor.putString("serverAddress", "http://putnav.cba.pl");
        preferencesEditor.commit();
        finish();
    }

    private void update() {
        PackageUpdater task = new PackageUpdater(PreferencesActivity.this);
        try {
            task.execute(new URL(textViewServer.getText().toString()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
