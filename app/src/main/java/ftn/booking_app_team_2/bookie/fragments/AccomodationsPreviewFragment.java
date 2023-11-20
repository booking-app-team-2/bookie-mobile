package ftn.booking_app_team_2.bookie.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import ftn.booking_app_team_2.bookie.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccomodationsPreviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccomodationsPreviewFragment extends Fragment {

    private ImageView imageView;
    public AccomodationsPreviewFragment() {

    }
    public static AccomodationsPreviewFragment newInstance() {
        AccomodationsPreviewFragment fragment = new AccomodationsPreviewFragment();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      View view=inflater.inflate(R.layout.fragment_accomodations_preview, container, false);
      return view;

    }
}