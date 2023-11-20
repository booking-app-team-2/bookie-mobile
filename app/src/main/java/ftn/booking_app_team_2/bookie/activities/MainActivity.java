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
import ftn.booking_app_team_2.bookie.tools.SessionManager;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private NavController navController;
    private BottomNavigationView bottomNavigationView;

    private void setupNavbarBasedOnUserType() {
        SessionManager sessionManager = new SessionManager(this);
        String userType = sessionManager.getUserType();
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

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        bottomNavigationView = binding.bottomNavigationView;

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().
                findFragmentById(binding.fragmentContainerView.getId());
        assert navHostFragment != null;
        navController = navHostFragment.getNavController();

        setupNavbarBasedOnUserType();
    }

}