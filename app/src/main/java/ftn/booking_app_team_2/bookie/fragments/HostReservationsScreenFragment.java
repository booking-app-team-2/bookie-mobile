package ftn.booking_app_team_2.bookie.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.datepicker.MaterialDatePicker;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import ftn.booking_app_team_2.bookie.databinding.FragmentHostReservationsScreenBinding;

public class HostReservationsScreenFragment extends Fragment {
    private FragmentHostReservationsScreenBinding binding;
    private Pair<Long, Long> selectedTimestampRange = new Pair<>(null, null);

    public HostReservationsScreenFragment() { }

    public static HostReservationsScreenFragment newInstance() {
        return new HostReservationsScreenFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void displaySelectedReservationPeriod() {
        LocalDate startDate = Instant
                .ofEpochMilli(selectedTimestampRange.first)
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        LocalDate endDate = Instant
                .ofEpochMilli(selectedTimestampRange.second)
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        binding.reservationPeriodInput.setText(
                String.format(
                        "%s - %s",
                        startDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        endDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
        );
    }

    private void showReservationPeriodPicker() {
        MaterialDatePicker<Pair<Long, Long>> reservationPeriodPicker = MaterialDatePicker
                .Builder
                .dateRangePicker()
                .setTitleText("Select the period of the reservation")
                .build();

        reservationPeriodPicker.addOnPositiveButtonClickListener(selection -> {
            selectedTimestampRange = selection;
            displaySelectedReservationPeriod();
        });

        reservationPeriodPicker.show(
                requireActivity().getSupportFragmentManager(),
                reservationPeriodPicker.toString()
        );
    }

    @Override
    public View onCreateView(@NonNull  LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHostReservationsScreenBinding
                .inflate(inflater, container, false);

        binding.reservationPeriodButton.setOnClickListener(view -> showReservationPeriodPicker());

        return binding.getRoot();
    }
}