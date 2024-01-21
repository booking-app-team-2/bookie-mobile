package ftn.booking_app_team_2.bookie.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.textfield.TextInputEditText;

import ftn.booking_app_team_2.bookie.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AvailabilityPeriodFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AvailabilityPeriodFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_START_DATE = "startDate";
    private static final String ARG_END_DATE = "endDate";
    private static final String ARG_PRICE = "price";

    // TODO: Rename and change types of parameters
    private String startDate;
    private String endDate;

    private double price;

    public AvailabilityPeriodFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AvailabilityPeriodFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AvailabilityPeriodFragment newInstance(String startDate,String endDate,Double price) {
        AvailabilityPeriodFragment fragment = new AvailabilityPeriodFragment();
        Bundle args = new Bundle();
        args.putString(ARG_START_DATE,startDate);
        args.putString(ARG_END_DATE,endDate);
        args.putDouble(ARG_PRICE,price);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            startDate=getArguments().getString(ARG_START_DATE);
            endDate=getArguments().getString(ARG_END_DATE);
            price= getArguments().getDouble(ARG_PRICE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_availability_period, container, false);
        TextInputEditText text=view.findViewById(R.id.periodField);
        text.setText(String.format("%s to %s , price:%s", startDate, endDate, price));
        return view;
    }
}