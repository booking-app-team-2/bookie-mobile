package ftn.booking_app_team_2.bookie.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import ftn.booking_app_team_2.bookie.R;
import ftn.booking_app_team_2.bookie.databinding.FragmentGuestMainScreenBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GuestMainScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GuestMainScreenFragment extends Fragment {
    private ImageView imageView;
    private FragmentGuestMainScreenBinding binding;

    public GuestMainScreenFragment() {

    }
    public static GuestMainScreenFragment newInstance() {
        GuestMainScreenFragment fragment = new GuestMainScreenFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGuestMainScreenBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.accommodationCard.detailsBtn.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.navigateToAccommodationDetailsScreen);
        });

        return root;
    }
}