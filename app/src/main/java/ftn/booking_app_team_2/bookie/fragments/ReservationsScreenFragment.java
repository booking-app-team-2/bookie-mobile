package ftn.booking_app_team_2.bookie.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
import ftn.booking_app_team_2.bookie.databinding.FragmentReservationsScreenBinding;
import ftn.booking_app_team_2.bookie.model.AccommodationDTO;
import ftn.booking_app_team_2.bookie.model.ReservationGuest;
import ftn.booking_app_team_2.bookie.model.ReservationOwner;
import ftn.booking_app_team_2.bookie.model.ReservationStatus;
import ftn.booking_app_team_2.bookie.tools.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationsScreenFragment extends Fragment {
    private FragmentReservationsScreenBinding binding;

    private String userRole;
    private Long userId;

    private Collection<ReservationOwner> reservationsOwner = null;
    private Collection<ReservationGuest> reservationsGuest = null;
    private Collection<AccommodationDTO> guestFavouriteAccommodations = null;

    private static final String START_TIMESTAMP_KEY = "start_timestamp";
    private static final String END_TIMESTAMP_KEY = "end_timestamp";

    private TextInputEditText accommodationName;
    private Pair<Long, Long> selectedTimestampRange = new Pair<>(null, null);
    private CheckBox waitingCheckBox;
    private CheckBox acceptedCheckBox;
    private CheckBox declinedCheckBox;
    private CheckBox cancelledCheckBox;

    public ReservationsScreenFragment() { }

    public static ReservationsScreenFragment newInstance() {
        return new ReservationsScreenFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        SessionManager sessionManager = new SessionManager(requireContext());
        userRole = sessionManager.getUserType();
        userId = sessionManager.getUserId();
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
        removeAllReservationViews();
        if (userRole.equals("Guest")){
            searchReservationsGuest();
            searchFavouriteAccommodations();
        }
        else if (userRole.equals("Owner"))
            searchReservationsOwner();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && savedInstanceState != null) {
            selectedTimestampRange = new Pair<>(
                    savedInstanceState.getSerializable(START_TIMESTAMP_KEY, Long.class),
                    savedInstanceState.getSerializable(END_TIMESTAMP_KEY, Long.class)
            );
        }
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
                        endDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                )
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

    private void removeAllReservationViews() {
        if (!isAdded())
            return;

        getChildFragmentManager().getFragments().forEach(childFragment -> {
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.remove(childFragment);
            fragmentTransaction.commit();
        });
    }

    private void addAllAccommodationViews(){
        if(!isAdded())
            return;

        guestFavouriteAccommodations.forEach(accommodation -> {
            AccommodationCardFragment accommodationCardFragment = AccommodationCardFragment
                    .newInstance(
                            accommodation.getName(),
                            accommodation.getDescription(),
                            Integer.toString(accommodation.getMinimumGuests()),
                            Integer.toString(accommodation.getMaximumGuests()),
                            accommodation.getId(),
                            accommodation.getImages(),
                            accommodation.getAvailabilityPeriods()
                    );

            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.add(binding.guestFavouriteAccommodationsContainer.getId(), accommodationCardFragment);
            transaction.commit();
        });
    }

    private void addAllReservationViews() {
        if (!isAdded())
            return;

        if (userRole.equals("Guest")) {
            reservationsGuest.forEach(reservationGuest -> {
                ReservationFragment reservationFragment = ReservationFragment.newInstance(
                        reservationGuest.getId(),
                        reservationGuest.getAccommodationNameDTO(),
                        reservationGuest.getNumberOfGuests(),
                        reservationGuest.getPeriodDTO(),
                        reservationGuest.getPrice(),
                        reservationGuest.getStatus()
                );

                FragmentTransaction fragmentTransaction =
                        getChildFragmentManager().beginTransaction();
                fragmentTransaction.add(binding.reservationsContainer.getId(), reservationFragment);
                fragmentTransaction.commit();
            });

        }
        else if (userRole.equals("Owner")) {
            reservationsOwner.forEach(reservationOwner -> {
                ReservationFragment reservationFragment = ReservationFragment.newInstance(
                        reservationOwner.getId(),
                        reservationOwner.getAccommodationNameDTO(),
                        reservationOwner.getNumberOfGuests(),
                        reservationOwner.getPeriodDTO(),
                        reservationOwner.getPrice(),
                        reservationOwner.getReserveeBasicInfoDTO(),
                        reservationOwner.getStatus()
                );

                FragmentTransaction fragmentTransaction =
                        getChildFragmentManager().beginTransaction();
                fragmentTransaction.add(binding.reservationsContainer.getId(), reservationFragment);
                fragmentTransaction.commit();
            });
        }
    }

    protected void searchFavouriteAccommodations(){

        Call<Collection<AccommodationDTO>> call =
                ClientUtils.guestService.getFavouriteAccommodations(userId);

        call.enqueue(new Callback<Collection<AccommodationDTO>>() {
            @Override
            public void onResponse(@NonNull Call<Collection<AccommodationDTO>> call,
                                   @NonNull Response<Collection<AccommodationDTO>> response) {
                if (response.code() == 200) {

                    assert response.body() != null;
                    guestFavouriteAccommodations = response.body();

                    addAllAccommodationViews();
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
            public void onFailure(@NonNull Call<Collection<AccommodationDTO>> call,
                                  @NonNull Throwable t) {
                Snackbar.make(
                        requireView(),
                        "Error reaching the server.",
                        Snackbar.LENGTH_SHORT
                ).show();
            }
        });

    }
    protected void searchReservationsGuest() {
        Call<Collection<ReservationGuest>> call =
                ClientUtils.reservationService.searchAndFilterGuest(
                        Objects.requireNonNull(accommodationName.getText()).toString(),
                        selectedTimestampRange.first,
                        selectedTimestampRange.second,
                        getStatuses()
                );

        call.enqueue(new Callback<Collection<ReservationGuest>>() {
            @Override
            public void onResponse(@NonNull Call<Collection<ReservationGuest>> call,
                                   @NonNull Response<Collection<ReservationGuest>> response) {
                if (response.code() == 200) {

                    assert response.body() != null;
                    reservationsGuest = response.body();

                    addAllReservationViews();
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
            public void onFailure(@NonNull Call<Collection<ReservationGuest>> call,
                                  @NonNull Throwable t) {
                Snackbar.make(
                        requireView(),
                        "Error reaching the server.",
                        Snackbar.LENGTH_SHORT
                ).show();
            }
        });
    }

    protected void searchReservationsOwner() {
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
                    reservationsOwner = response.body();

                    addAllReservationViews();
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
        binding = FragmentReservationsScreenBinding
                .inflate(inflater, container, false);

        accommodationName = binding.accommodationName;
        waitingCheckBox = binding.waitingCheckBox;
        acceptedCheckBox = binding.acceptedCheckBox;
        declinedCheckBox = binding.declinedCheckBox;
        cancelledCheckBox = binding.cancelledCheckBox;

        binding.reservationPeriodButton.setOnClickListener(view -> showReservationPeriodPicker());
        if (userRole.equals("Guest"))
            binding.searchBtn.setOnClickListener(view -> searchReservationsGuest());
        else if (userRole.equals("Owner"))
            binding.searchBtn.setOnClickListener(view -> searchReservationsOwner());

        return binding.getRoot();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(START_TIMESTAMP_KEY, selectedTimestampRange.first);
        outState.putSerializable(END_TIMESTAMP_KEY, selectedTimestampRange.second);
    }
}