package ftn.booking_app_team_2.bookie.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ftn.booking_app_team_2.bookie.R;
import ftn.booking_app_team_2.bookie.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    private void setupNavbarBasedOnUserType() {
        /*
            TODO: Remove placeholder, below. Get user type via SessionManager, once logging in has
                been implemented.
        */
        String userType = "guest";
        int graphResId;
        int resId;
        if (userType.equals("guest")) {
            graphResId = R.navigation.guest_navigation_graph;
            resId = R.menu.guest_bottom_navigation_menu;
        } else if (userType.equals("host")) {
            graphResId = R.navigation.host_navigation_graph;
            resId = R.menu.host_bottom_navigation_menu;
        } else {
            graphResId = R.navigation.admin_navigation_graph;
            resId = R.menu.admin_bottom_navigation_menu;
        }

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().
                findFragmentById(binding.fragmentContainerView.getId());
        assert navHostFragment != null;
        NavController navController = navHostFragment.getNavController();
        NavInflater inflater = navController.getNavInflater();
        NavGraph graph = inflater.inflate(graphResId);
        navController.setGraph(graph);

        BottomNavigationView bottomNavigationView = binding.bottomNavigationView;
        bottomNavigationView.getMenu().clear();
        bottomNavigationView.inflateMenu(resId);

        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupNavbarBasedOnUserType();
    }
}