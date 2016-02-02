package pl.poznan.put.putnav;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;

public class PreferencesActivity extends AppCompatActivity {

    private static final String PREFERENCES_NAME = "appPreferences";
    private static final String PREFERENCE_DISABLED = "disabled";

    private Button btn6;
    private Button btn7;
    private CheckBox checkBox;
    private RadioButton radioButtonPolish;
    private RadioButton radioButtonEnglish;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        sharedPreferences = getSharedPreferences(PREFERENCES_NAME, AppCompatActivity.MODE_PRIVATE);
        init();
        loadPreferences();


    }

    public void loadPreferences() {
        if (sharedPreferences.contains("exists")) {
            boolean disabled = sharedPreferences.getBoolean(PREFERENCE_DISABLED, false);
            int language = 0;
            language = sharedPreferences.getInt("language", 0);
            checkBox.setChecked(disabled);
            if (language == 1) {
                radioButtonEnglish.setChecked(true);
            } else {
                radioButtonPolish.setChecked(true);
            }
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

        checkBox = (CheckBox) findViewById(R.id.checkBox1);

        radioButtonEnglish = (RadioButton) findViewById(R.id.radio_english);
        radioButtonPolish = (RadioButton) findViewById(R.id.radio_polish);
    }

    public void saveChanges() {
        boolean a = checkBox.isChecked();
        int language = 0;
        if (radioButtonEnglish.isChecked()) {
            language = 1;
        }
        SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();
        preferencesEditor.putBoolean(PREFERENCE_DISABLED, a);
        preferencesEditor.putBoolean("exists", true);
        preferencesEditor.putInt("language", language);
        preferencesEditor.commit();
        finish();
    }

}
