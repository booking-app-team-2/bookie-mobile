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
 * Use the {@link GuestMainScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GuestMainScreenFragment extends Fragment {

    private ImageView imageView;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      View view=inflater.inflate(R.layout.fragment_guest_main_screen, container, false);
      return view;
    }
}