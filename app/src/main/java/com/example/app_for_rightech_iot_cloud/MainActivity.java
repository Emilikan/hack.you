package com.example.app_for_rightech_iot_cloud;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    ArrayList<String> factories;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        setContentView(R.layout.activity_main);
        final TextView title = findViewById(R.id.title);
        if (Objects.equals(preferences.getString("theme", "light"), "dark")){
            setTheme(R.style.DarkTheme);
            findViewById(R.id.toolbar).setBackgroundColor(Color.parseColor("#282E33"));
            title.setTextColor(Color.parseColor("#E9E9E9"));
        }
        else{
            setTheme(R.style.AppTheme);
            findViewById(R.id.toolbar).setBackgroundColor(Color.parseColor("#ffffff"));
            title.setTextColor(Color.parseColor("#000000"));
        }

        toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        factories = new ArrayList<>();
        factories.add("Метровагонмаш");
        factories.add("что-то");
        factories.add("fffff");

        final ImageView leftButton = findViewById(R.id.notific);
        final ImageView rightButton = findViewById(R.id.settings);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (title.getText() == "Уведомления"){
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    Fragment fragment = new MainFragment();
                    fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                    leftButton.setImageResource(R.drawable.notification);
                    rightButton.setImageResource(R.drawable.settings);
                    title.setText("Метровагонмаш");
                }
                else {
                    if (title.getText() == "Настройки") {
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        Fragment fragment = new MainFragment();
                        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                        leftButton.setImageResource(R.drawable.notification);
                        rightButton.setImageResource(R.drawable.settings);

                        title.setText("Метровагонмаш");
                    }
                else{
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    Fragment fragment = new Notifications();
                    fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                    leftButton.setImageResource(R.drawable.left_arrow);
                    rightButton.setImageResource(R.drawable.settings);

                    title.setText("Уведомления");
                }
                }
            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (title.getText() == "Уведомления"){
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    Fragment fragment = new Settings();
                    fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                    leftButton.setImageResource(R.drawable.left_arrow);
                    rightButton.setImageResource(R.drawable.notification);

                    title.setText("Настройки");
                }
                else {
                    if (title.getText() == "Настройки") {
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        Fragment fragment = new Notifications();
                        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                        leftButton.setImageResource(R.drawable.left_arrow);
                        rightButton.setImageResource(R.drawable.settings);

                        title.setText("Уведомления");
                    }
                    else{
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        Fragment fragment = new Settings();
                        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
                        leftButton.setImageResource(R.drawable.left_arrow);
                        rightButton.setImageResource(R.drawable.notification);

                        title.setText("Настройки");
                    }
                }
            }
        });
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.alert, null);
                builder.setView(view);
                builder.setCancelable(true);
                Spinner spinner = view.findViewById(R.id.spinner);
                ArrayAdapter<?> adapter =
                        new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, factories);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setGravity(Gravity.CENTER);
                spinner.setAdapter(adapter);

                final AlertDialog dialog = builder.create();
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        title.setText(factories.get(position));

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }

                });
                dialog.show();


            }
        });
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = new MainFragment();
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
    }



    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_notification) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = new Notifications();
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
            getSupportActionBar().setTitle("Уведомления");
        } else if (item.getItemId() == R.id.action_ai) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            Fragment fragment = new Neuronet();
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
            getSupportActionBar().setTitle("Нейросеть");
        }
        return true;
    }*/
}
