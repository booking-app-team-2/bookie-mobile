package ftn.booking_app_team_2.bookie.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import ftn.booking_app_team_2.bookie.clients.ClientUtils;
import ftn.booking_app_team_2.bookie.databinding.FragmentHostReservationsScreenBinding;
import ftn.booking_app_team_2.bookie.model.ReservationOwner;
import ftn.booking_app_team_2.bookie.model.ReservationStatus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HostReservationsScreenFragment extends Fragment {
    private FragmentHostReservationsScreenBinding binding;

    private List<ReservationOwner> reservationsOwner = null;

    private TextInputEditText accommodationName;
    private Pair<Long, Long> selectedTimestampRange = new Pair<>(null, null);
    private CheckBox waitingCheckBox;
    private CheckBox acceptedCheckBox;
    private CheckBox declinedCheckBox;
    private CheckBox cancelledCheckBox;

    public HostReservationsScreenFragment() { }

    public static HostReservationsScreenFragment newInstance() {
        return new HostReservationsScreenFragment();
    }

    private List<ReservationStatus> getStatuses() {
        List<ReservationStatus> statuses = new ArrayList<>();

        if (waitingCheckBox.isChecked())
            statuses.add(ReservationStatus.Waiting);

        if (acceptedCheckBox.isChecked())
            statuses.add(ReservationStatus.Accepted);

        if (declinedCheckBox.isChecked())
            statuses.add(ReservationStatus.Declined);

        if (cancelledCheckBox.isChecked())
            statuses.add(ReservationStatus.Cancelled);

        return statuses;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (reservationsOwner != null) {
            return;
        }

        searchReservations();
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

    private void searchReservations() {
        Call<Collection<ReservationOwner>> call =
                ClientUtils.reservationService.searchAndFilterOwner(
                        Objects.requireNonNull(accommodationName.getText()).toString(),
                        selectedTimestampRange.first,
                        selectedTimestampRange.second,
                        getStatuses()
                );

        call.enqueue(new Callback<Collection<ReservationOwner>>() {
            @Override
            public void onResponse(@NonNull Call<Collection<ReservationOwner>> call,
                                   @NonNull Response<Collection<ReservationOwner>> response) {
                if (response.code() == 200) {
                    assert response.body() != null;
                    reservationsOwner = new ArrayList<>(response.body());
                } else {
                    assert response.errorBody() != null;
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        Snackbar.make(
                                requireView(),
                                jsonObject.getString("message"),
                                Snackbar.LENGTH_SHORT
                        ).show();
                    } catch (Exception ex) {
                        Log.d(
                                "Bookie",
                                ex.getMessage() != null ? ex.getMessage() : "Unknown error"
                        );
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Collection<ReservationOwner>> call,
                                  @NonNull Throwable t) {
                Snackbar.make(
                        requireView(),
                        "Error reaching the server.",
                        Snackbar.LENGTH_SHORT
                ).show();
            }
        });
    }

    @Override
    public View onCreateView(@NonNull  LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHostReservationsScreenBinding
                .inflate(inflater, container, false);

        accommodationName = binding.accommodationName;
        waitingCheckBox = binding.waitingCheckBox;
        acceptedCheckBox = binding.acceptedCheckBox;
        declinedCheckBox = binding.declinedCheckBox;
        cancelledCheckBox = binding.cancelledCheckBox;

        binding.reservationPeriodButton.setOnClickListener(view -> showReservationPeriodPicker());
        binding.searchBtn.setOnClickListener(view -> searchReservations());

        return binding.getRoot();
    }
}