package ftn.booking_app_team_2.bookie.fragments;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.util.Pair;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import ftn.booking_app_team_2.bookie.clients.AccommodationService;
import ftn.booking_app_team_2.bookie.clients.ClientUtils;
import ftn.booking_app_team_2.bookie.databinding.FragmentGuestMainScreenBinding;
import ftn.booking_app_team_2.bookie.model.AccommodationDTO;
import ftn.booking_app_team_2.bookie.tools.AccommodationComparator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GuestMainScreenFragment extends Fragment implements SensorEventListener {
    private FragmentGuestMainScreenBinding binding;

    private List<AccommodationDTO> accommodations = null;
    private boolean isSortedAscending = true;

    private Pair<Long, Long> selectedDateRange;

    private SensorManager sensorManager;

    private long lastSensorUpdateTimestamp;

    private float lastX;
    private float lastY;
    private float lastZ;

    private static final int SHAKE_SPEED_THRESHHOLD = 3000;

    public GuestMainScreenFragment() {

    }

    public static GuestMainScreenFragment newInstance() {
        return new GuestMainScreenFragment();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (accommodations != null)
            return;

        searchAccommodations();

        sensorManager.registerListener(
                this,
                sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION),
                SensorManager.SENSOR_DELAY_NORMAL
        );

        Snackbar
                .make(requireView(),
                "Shake your screen in order to get the best deals first.",
                Snackbar.LENGTH_SHORT)
                .show();
    }

    @Override
    public void onPause() {
        super.onPause();

        sensorManager.unregisterListener(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGuestMainScreenBinding.inflate(inflater, container, false);

        selectedDateRange = new Pair<>(null, null);

        binding.iconButton.setOnClickListener(view -> showDateRangePicker());

        binding.searchButton.setOnClickListener(view -> searchAccommodations());

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
    }

    private void showDateRangePicker() {

        MaterialDatePicker<Pair<Long, Long>> dateRangePicker =
                MaterialDatePicker.Builder.dateRangePicker()
                        .setTitleText("Select a Date Range")
                        .build();

        dateRangePicker.addOnPositiveButtonClickListener(selection -> {
            selectedDateRange = selection;
            displaySelectedDateRange();
        });

        dateRangePicker.addOnNegativeButtonClickListener(view -> {
            selectedDateRange = new Pair<>(null, null);
            binding.DateRangeSearchField.setText("");
        });

        dateRangePicker.show(
                requireActivity().getSupportFragmentManager(), dateRangePicker.toString()
        );
    }

    private void displaySelectedDateRange() {
            if (selectedDateRange != null) {
                Date startDate = new Date(selectedDateRange.first);
                Date endDate = new Date(selectedDateRange.second);

                SimpleDateFormat dateFormat =
                        new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
                String formattedStartDate = dateFormat.format(startDate);
                String formattedEndDate = dateFormat.format(endDate);

                String dateRangeText = formattedStartDate + " - " + formattedEndDate;

                binding.DateRangeSearchField.setText(dateRangeText);
            }
    }

    private void addAllAccommodationViews() {
        if (!isAdded())
            return;

        accommodations.forEach(accommodation -> {
            AccommodationCardFragment fragment = AccommodationCardFragment.newInstance(
                    accommodation.getName(),
                    accommodation.getDescription(),
                    Integer.toString(accommodation.getMinimumGuests()),
                    Integer.toString(accommodation.getMaximumGuests()),
                    accommodation.getId(),
                    accommodation.getImages(),
                    accommodation.getAvailabilityPeriods()
            );

            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.add(binding.accommodationsContainer.getId(), fragment);
            transaction.commit();
        });
    }

    private void removeAllAccommodationViews() {
        if (!isAdded())
            return;

        getChildFragmentManager().getFragments().forEach(fragment -> {
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.remove(fragment);
            fragmentTransaction.commit();
        });
    }

    private void searchAccommodations(){
        Integer guestCount = Objects
                .requireNonNull(binding.GuestCountSearchField.getText())
                .toString()
                .equals("") ?
                null :
                Integer.parseInt(binding.GuestCountSearchField.getText().toString());

        Long startDateInSeconds =
                selectedDateRange.first != null ? selectedDateRange.first : null;
        Long endDateInSeconds =
                selectedDateRange.second != null ? selectedDateRange.second : null;

        AccommodationService service = ClientUtils.getAccommodationService(getContext());
        Call<Collection<AccommodationDTO>> call =
                service.getSearchedAccommodations(
                        null,
                        guestCount,
                        startDateInSeconds,
                        endDateInSeconds
                );

        call.enqueue(new Callback<Collection<AccommodationDTO>>() {
            @Override
            public void onResponse(@NonNull Call<Collection<AccommodationDTO>> call,
                                   @NonNull Response<Collection<AccommodationDTO>> response) {
                if (response.isSuccessful()) {
                    removeAllAccommodationViews();

                    assert response.body() != null;
                    accommodations = new ArrayList<>(response.body());

                    addAllAccommodationViews();
                } else {
                    Log.e("TAG","NE RADI");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Collection<AccommodationDTO>> call,
                                  @NonNull Throwable t) {
                Log.e("TAG",t.toString());
            }
        });
    }

    private void sortAccommodationsByPrice() {
        if (!isAdded() && accommodations == null)
            return;

        removeAllAccommodationViews();

        if (isSortedAscending)
            Collections.reverse(accommodations);
        else
            accommodations.sort(new AccommodationComparator());

        isSortedAscending = !isSortedAscending;

        addAllAccommodationViews();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            long currentTimestamp = Instant.now().toEpochMilli();

            long timestampDifference = currentTimestamp - lastSensorUpdateTimestamp;
            if (timestampDifference <= 100)
                return;

            lastSensorUpdateTimestamp = currentTimestamp;

            float[] values = sensorEvent.values;

            float speed = Math.abs(
                    values[0] + values[1] + values[2] - lastX - lastY - lastZ
            ) / timestampDifference * 10000;
            if (speed > SHAKE_SPEED_THRESHHOLD) {
                sortAccommodationsByPrice();
            }

            lastX = values[0];
            lastY = values[1];
            lastZ = values[2];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
