package ftn.booking_app_team_2.bookie;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccommodationDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccommodationDetailsFragment extends Fragment {



    public AccommodationDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_accommodation_details, container, false);
    }
}