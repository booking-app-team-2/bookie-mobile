package ftn.booking_app_team_2.bookie.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Collection;

import ftn.booking_app_team_2.bookie.clients.ClientUtils;
import ftn.booking_app_team_2.bookie.databinding.FragmentAccommodationsScreenBinding;
import ftn.booking_app_team_2.bookie.model.AccommodationDTO;
import ftn.booking_app_team_2.bookie.tools.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccommodationsScreenFragment extends Fragment {
    private FragmentAccommodationsScreenBinding binding;

    private Collection<AccommodationDTO> accommodations = null;

    public AccommodationsScreenFragment() { }

    public static AccommodationsScreenFragment newInstance() {
        return new AccommodationsScreenFragment();
    }

    private void removeAllAccommodationViews() {
        if (!isAdded())
            return;

        getChildFragmentManager().getFragments().forEach(childFragment -> {
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.remove(childFragment);
            transaction.commit();
        });
    }

    private void addAllAccommodationViews() {
        if (!isAdded())
            return;

        accommodations.forEach(accommodation -> {
            AccommodationCardFragment accommodationCardFragment = AccommodationCardFragment
                    .newInstance(
                            accommodation.getName(),
                            accommodation.getDescription(),
                            Integer.toString(accommodation.getMinimumGuests()),
                            Integer.toString(accommodation.getMaximumGuests()),
                            accommodation.getId(),
                            accommodation.getImages()
                    );

            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.add(binding.accommodationsContainer.getId(), accommodationCardFragment);
            transaction.commit();
        });
    }

    private void getAccommodationsForOwner(long ownerId) {
        Call<Collection<AccommodationDTO>> call =
                ClientUtils.accommodationService.getAccommodationsByOwner(ownerId);

        call.enqueue(new Callback<Collection<AccommodationDTO>>() {
            @Override
            public void onResponse(@NonNull Call<Collection<AccommodationDTO>> call,
                                   @NonNull Response<Collection<AccommodationDTO>> response) {
                if (response.code() == 200) {
                    removeAllAccommodationViews();

                    assert response.body() != null;
                    accommodations = response.body();

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

    @Override
    public void onResume() {
        super.onResume();

        SessionManager sessionManager = new SessionManager(requireContext());
        getAccommodationsForOwner(sessionManager.getUserId());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAccommodationsScreenBinding
                .inflate(inflater, container, false);


        binding.createReportBtn.setOnClickListener(view -> {
            final Calendar startDate = Calendar.getInstance();
            final Calendar endDate = Calendar.getInstance();

            AlertDialog.Builder startDateAlertDialog = new AlertDialog.Builder(view.getContext());
            startDateAlertDialog.setTitle("Select First Date");
            startDateAlertDialog.setMessage("Please select the first date.");

            startDateAlertDialog.setPositiveButton("OK", (dialog, which) -> {
                DatePickerDialog startDatePicker = new DatePickerDialog(view.getContext(), (datePicker, year, month, dayOfMonth) -> {
                    startDate.set(year, month, dayOfMonth);
                    AlertDialog.Builder endDateAlertDialog = new AlertDialog.Builder(view.getContext());
                    endDateAlertDialog.setTitle("Select Second Date");
                    endDateAlertDialog.setMessage("Please select the second date.");

                    endDateAlertDialog.setPositiveButton("OK", (endDialog, which1) -> {
                        DatePickerDialog endDatePicker = new DatePickerDialog(view.getContext(), (endDatePickerView, endYear, endMonth, endDayOfMonth) -> {
                            endDate.set(endYear, endMonth, endDayOfMonth);

                            if (endDate.after(startDate)) {
                                new AlertDialog.Builder(view.getContext())
                                        .setTitle("Error!")
                                        .setMessage("Generating reports not yet implemented")
                                        .setPositiveButton("OK", (confirmDialog, which2) -> confirmDialog.dismiss())
                                        .show();
                            } else {
                                new AlertDialog.Builder(view.getContext())
                                        .setTitle("Invalid Dates")
                                        .setMessage("The end date must be after the start date.")
                                        .setPositiveButton("OK", (errorDialog, which2) -> errorDialog.dismiss())
                                        .show();
                            }
                        }, startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH), startDate.get(Calendar.DAY_OF_MONTH));
                        endDatePicker.show();
                    });

                    endDateAlertDialog.show();

                }, startDate.get(Calendar.YEAR), startDate.get(Calendar.MONTH), startDate.get(Calendar.DAY_OF_MONTH));

                startDatePicker.show();
            });

            startDateAlertDialog.show();
        });



        return binding.getRoot();
    }
}