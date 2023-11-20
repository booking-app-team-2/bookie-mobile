package ftn.booking_app_team_2.bookie.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import ftn.booking_app_team_2.bookie.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button=findViewById(R.id.button2);
        button.setOnClickListener(v -> {
            Intent intent=new Intent(MainActivity.this,MainScreenActivity.class);
            startActivity(intent);
        });

    }
}