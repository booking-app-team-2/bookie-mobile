package ftn.booking_app_team_2.bookie.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import ftn.booking_app_team_2.bookie.R;

public class LoginRegisterActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor lightSensor;
    private static final int SENSOR_SENSITIVITY = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (sensorManager != null)
            lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
    }

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
            if (
                    sensorEvent.values[0] >= -SENSOR_SENSITIVITY &&
                            sensorEvent.values[0] <= SENSOR_SENSITIVITY
            ) {
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
}