package ftn.booking_app_team_2.bookie.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.core.util.Pair;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

import ftn.booking_app_team_2.bookie.R;
import ftn.booking_app_team_2.bookie.clients.ClientUtils;
import ftn.booking_app_team_2.bookie.databinding.FragmentGuestMainScreenBinding;
import ftn.booking_app_team_2.bookie.model.AccommodationDTO;
import ftn.booking_app_team_2.bookie.clients.AccommodationService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GuestMainScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GuestMainScreenFragment extends Fragment {
    private ImageView imageView;
    private FragmentGuestMainScreenBinding binding;

    private Pair<Long, Long> selectedDateRange;

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

        selectedDateRange=new Pair<Long,Long>(null,null);
        binding.iconButton.setOnClickListener(view -> {
            showDateRangePicker();
        });

        binding.searchButton.setOnClickListener(view ->{
            searchAccommodations();
        });
        binding.accommodationsContainer.removeAllViews();
        return root;
    }

    private void showDateRangePicker() {

        MaterialDatePicker<Pair<Long, Long>> dateRangePicker =
                MaterialDatePicker.Builder.dateRangePicker()
                        .setTitleText("Select a Date Range")
                        .build();

        dateRangePicker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
                    @Override
                    public void onPositiveButtonClick(Pair<Long, Long> selection) {
                        // Save the selected date range
                        selectedDateRange = selection;
                        displaySelectedDateRange();
                        // Perform actions with the selected date range
                        // (e.g., update UI, save to storage, etc.)
                    }
                });

        dateRangePicker.addOnNegativeButtonClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectedDateRange=new Pair<Long,Long>(null,null);
                        binding.DateRangeSearchField.setText("");
                    }
                });

        dateRangePicker.show(requireFragmentManager(), dateRangePicker.toString());
    }

    private void displaySelectedDateRange() {
            if (selectedDateRange != null) {
                // Convert milliseconds to Date objects
                Date startDate = new Date(selectedDateRange.first);
                Date endDate = new Date(selectedDateRange.second);

                // Format the Date objects to a readable date format
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
                String formattedStartDate = dateFormat.format(startDate);
                String formattedEndDate = dateFormat.format(endDate);

                // Display the selected date range in a TextView or any other UI element
                String dateRangeText = formattedStartDate + " - " + formattedEndDate;

                // Update your TextView or UI element with the formatted date range text
                binding.DateRangeSearchField.setText(dateRangeText);
            }
    }

    private void searchAccommodations(){

        Integer guestCount = binding.GuestCountSearchField.getText().toString().equals("") ? null : Integer.parseInt(binding.GuestCountSearchField.getText().toString());
        Long startDateInSeconds = selectedDateRange.first != null ? selectedDateRange.first / 1000 : null;
        Long endDateInSeconds = selectedDateRange.second != null ? selectedDateRange.second / 1000 : null;
        Call<Collection<AccommodationDTO>> call = ClientUtils.accommodationService.getSearchedAccommodations(
                null,
                guestCount,
                startDateInSeconds,
                endDateInSeconds
        );
        call.enqueue(new Callback<Collection<AccommodationDTO>>() {
            @Override
            public void onResponse(Call<Collection<AccommodationDTO>> call, Response<Collection<AccommodationDTO>> response) {
                if (response.isSuccessful()) {
                    binding.accommodationsContainer.removeAllViews();
                    Collection<AccommodationDTO> accommodations = response.body();
                    assert accommodations != null;
                    for(AccommodationDTO accommodationDTO:accommodations){
                        AccommodationCardFragment fragment = AccommodationCardFragment.newInstance(
                                accommodationDTO.getName(),
                                accommodationDTO.getDescription(),
                                Integer.toString(accommodationDTO.getMinimumGuests()),
                                Integer.toString(accommodationDTO.getMaximumGuests()),
                                accommodationDTO.getId()
                        );

                        // Use FragmentTransaction to add the fragment to the layout
                        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                        transaction.add(R.id.accommodations_container, fragment); // Assuming R.id.accommodations_container is the container in your layout
                        transaction.addToBackStack(null);
                        transaction.commit();
                    }
                } else {
                    Log.e("TAG","NE RADI");
                }
            }

            @Override
            public void onFailure(Call<Collection<AccommodationDTO>> call, Throwable t) {
                Log.e("TAG",t.toString());
            }
        });

    }

}
