package ftn.booking_app_team_2.bookie.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import ftn.booking_app_team_2.bookie.R;
import ftn.booking_app_team_2.bookie.databinding.FragmentAccommodationCardBinding;
import ftn.booking_app_team_2.bookie.tools.SessionManager;

public class AccommodationCardFragment extends Fragment {
    private String userRole;

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
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        SessionManager sessionManager = new SessionManager(requireContext());
        userRole = sessionManager.getUserType();
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentAccommodationCardBinding binding = FragmentAccommodationCardBinding
                .inflate(inflater, container, false);

        binding.accommodationName.setText(accommodationName);
        binding.accommodationDescription.setText(accommodationDescription);
        binding.accommodationGuestCount.setText(
                String.format("%s to %s guests", minGuests, maxGuests)
        );
        if (userRole.equals("Guest")) {
            binding.ownerInteractionContainer.setVisibility(View.GONE);

            binding.detailsBtn.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putLong("accommodationId", accommodationId);
                Navigation.findNavController(v).navigate(
                        R.id.navigateToAccommodationDetailsScreen, bundle
                );
            });
        } else if (userRole.equals("Owner")) {
            binding.guestInteractionContainer.setVisibility(View.GONE);

            binding.updateAccommodationBtn.setOnClickListener(view -> {

            });
        }

        return binding.getRoot();
    }
}
