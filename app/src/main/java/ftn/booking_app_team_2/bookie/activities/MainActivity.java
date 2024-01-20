package ftn.booking_app_team_2.bookie.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ftn.booking_app_team_2.bookie.R;
import ftn.booking_app_team_2.bookie.databinding.ActivityMainBinding;
import ftn.booking_app_team_2.bookie.tools.SessionManager;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private NavController navController;
    private BottomNavigationView bottomNavigationView;

    private SensorManager sensorManager;
    private Sensor lightSensor;
    private static final int SENSOR_SENSITIVITY = 2;

    @Override
    protected void onResume() {
        super.onResume();

        if (lightSensor != null)
            sensorManager.registerListener(
                    this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL
            );
    }

    @Override
    protected void onPause() {
        super.onPause();

        sensorManager.unregisterListener(this);
    }

    private boolean isInDarkMode() {
        int mode = AppCompatDelegate.getDefaultNightMode();
        return mode == AppCompatDelegate.MODE_NIGHT_YES ||
                mode == AppCompatDelegate.MODE_NIGHT_UNSPECIFIED;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_LIGHT) {
            if (sensorEvent.values[0] <= SENSOR_SENSITIVITY) {
                if (isInDarkMode())
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            else {
                if (!isInDarkMode())
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void setupNavbarBasedOnUserType() {
        SessionManager sessionManager = new SessionManager(this);
        String userType = sessionManager.getUserType();
        int graphResId;
        int resId;
        if (userType.equals("Guest")) {
            graphResId = R.navigation.guest_navigation_graph;
            resId = R.menu.guest_bottom_navigation_menu;
        } else if (userType.equals("Host")) {
            graphResId = R.navigation.host_navigation_graph;
            resId = R.menu.host_bottom_navigation_menu;
        } else {
            graphResId = R.navigation.admin_navigation_graph;
            resId = R.menu.admin_bottom_navigation_menu;
        }

        NavInflater inflater = navController.getNavInflater();
        NavGraph graph = inflater.inflate(graphResId);
        navController.setGraph(graph);

        bottomNavigationView.getMenu().clear();
        bottomNavigationView.inflateMenu(resId);

        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (sensorManager != null)
            lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        bottomNavigationView = binding.bottomNavigationView;

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().
                findFragmentById(binding.fragmentContainerView.getId());
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();

        setupNavbarBasedOnUserType();
    }
}