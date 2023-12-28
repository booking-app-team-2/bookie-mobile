package ftn.booking_app_team_2.bookie.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import ftn.booking_app_team_2.bookie.R;

public class AccommodationCardFragment extends Fragment {
    private static final String ARG_ACCOMMODATION_NAME = "accommodation_name";
    private static final String ARG_ACCOMMODATION_DESCRIPTION = "accommodation_description";
    private static final String ARG_ACCOMMODATION_MIN_GUESTS = "Minimum guests";
    private static final String ARG_ACCOMMODATION_MAX_GUESTS = "Maximum guests";
    private static final String ARG_ACCOMMODATION_ID="Accommodation ID";

    private String accommodationName;
    private String accommodationDescription;
    private String minGuests;
    private String maxGuests;
    private Long accommodationId;

    public AccommodationCardFragment() {
        // Required empty public constructor
    }

    public static AccommodationCardFragment newInstance(String accommodationName, String accommodationDescription,String minGuests,String maxGuests,Long accommodationId) {
        AccommodationCardFragment fragment = new AccommodationCardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ACCOMMODATION_NAME, accommodationName);
        args.putString(ARG_ACCOMMODATION_DESCRIPTION, accommodationDescription);
        args.putString(ARG_ACCOMMODATION_MIN_GUESTS, minGuests);
        args.putString(ARG_ACCOMMODATION_MAX_GUESTS, maxGuests);
        args.putLong(ARG_ACCOMMODATION_ID,accommodationId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            accommodationName = getArguments().getString(ARG_ACCOMMODATION_NAME);
            accommodationDescription = getArguments().getString(ARG_ACCOMMODATION_DESCRIPTION);
            minGuests=getArguments().getString(ARG_ACCOMMODATION_MIN_GUESTS);
            maxGuests=getArguments().getString(ARG_ACCOMMODATION_MAX_GUESTS);
            accommodationId=getArguments().getLong(ARG_ACCOMMODATION_ID);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accommodation_card, container, false);

        TextView nameTextView = view.findViewById(R.id.accommodation_name);
        TextView descriptionTextView = view.findViewById(R.id.accommodation_description);
        TextView guestCount=view.findViewById(R.id.accommodation_guest_count);
        nameTextView.setText(accommodationName);
        descriptionTextView.setText(accommodationDescription);
        guestCount.setText(String.format("%s to %s guests", minGuests, maxGuests));
        view.findViewById(R.id.detailsBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putLong("accommodationId",accommodationId);
                Navigation.findNavController(v).navigate(R.id.navigateToAccommodationDetailsScreen,bundle);
            }
        });

        return view;
    }
}
