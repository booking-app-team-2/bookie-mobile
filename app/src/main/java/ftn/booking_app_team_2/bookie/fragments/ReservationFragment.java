package ftn.booking_app_team_2.bookie.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import ftn.booking_app_team_2.bookie.clients.ClientUtils;
import ftn.booking_app_team_2.bookie.databinding.FragmentReservationBinding;
import ftn.booking_app_team_2.bookie.model.AccommodationNameDTO;
import ftn.booking_app_team_2.bookie.model.NumberOfCancelledReservations;
import ftn.booking_app_team_2.bookie.model.PeriodDTO;
import ftn.booking_app_team_2.bookie.model.ReservationStatus;
import ftn.booking_app_team_2.bookie.model.ReserveeBasicInfoDTO;
import ftn.booking_app_team_2.bookie.tools.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservationFragment extends Fragment {
    private FragmentReservationBinding binding;

    private String userRole;

    private static final String ARG_ID = "id";
    private static final String ARG_ACCOMMODATION_NAME_DTO = "accommodationNameDTO";
    private static final String ARG_NUMBER_OF_GUESTS = "numberOfGuests";
    private static final String ARG_PERIOD_DTO = "periodDTO";
    private static final String ARG_PRICE = "price";
    private static final String ARG_RESERVEE_BASIC_INFO_DTO = "reserveeBasicInfoDTO";
    private static final String ARG_STATUS = "status";

    private Long id;
    private AccommodationNameDTO accommodationNameDTO;
    private int numberOfGuests;
    private PeriodDTO periodDTO;
    private BigDecimal price;
    private ReserveeBasicInfoDTO reserveeBasicInfoDTO;
    private ReservationStatus status;


    public ReservationFragment() { }

    public static ReservationFragment newInstance(Long id,
                                                  AccommodationNameDTO accommodationNameDTO,
                                                  int numberOfGuests, PeriodDTO periodDTO,
                                                  BigDecimal price,
                                                  ReserveeBasicInfoDTO reserveeBasicInfoDTO,
                                                  ReservationStatus status) {
        ReservationFragment fragment = new ReservationFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ID, id);
        args.putParcelable(ARG_ACCOMMODATION_NAME_DTO, accommodationNameDTO);
        args.putInt(ARG_NUMBER_OF_GUESTS, numberOfGuests);
        args.putParcelable(ARG_PERIOD_DTO, periodDTO);
        args.putSerializable(ARG_PRICE, price);
        args.putParcelable(ARG_RESERVEE_BASIC_INFO_DTO, reserveeBasicInfoDTO);
        args.putSerializable(ARG_STATUS, status);
        fragment.setArguments(args);
        return fragment;
    }

    public static ReservationFragment newInstance(Long id,
                                                  AccommodationNameDTO accommodationNameDTO,
                                                  int numberOfGuests, PeriodDTO periodDTO,
                                                  BigDecimal price,
                                                  ReservationStatus status) {
        ReservationFragment fragment = new ReservationFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ID, id);
        args.putParcelable(ARG_ACCOMMODATION_NAME_DTO, accommodationNameDTO);
        args.putInt(ARG_NUMBER_OF_GUESTS, numberOfGuests);
        args.putParcelable(ARG_PERIOD_DTO, periodDTO);
        args.putSerializable(ARG_PRICE, price);
        args.putSerializable(ARG_STATUS, status);
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
            id = getArguments().getLong(ARG_ID);
            accommodationNameDTO = getArguments().getParcelable(ARG_ACCOMMODATION_NAME_DTO);
            numberOfGuests = getArguments().getInt(ARG_NUMBER_OF_GUESTS);
            periodDTO = getArguments().getParcelable(ARG_PERIOD_DTO);
            price = (BigDecimal) getArguments().getSerializable(ARG_PRICE);
            if (userRole.equals("Owner"))
                reserveeBasicInfoDTO = getArguments().getParcelable(ARG_RESERVEE_BASIC_INFO_DTO);
            status = (ReservationStatus) getArguments().getSerializable(ARG_STATUS);
        }
    }

    private String getPeriodInDisplayFormat() {
        LocalDate startDate = Instant
                .ofEpochMilli(periodDTO.getStartTimestamp())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        LocalDate endDate = Instant
                .ofEpochMilli(periodDTO.getEndTimestamp())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        return String.format(
                "%s - %s",
                startDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                endDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        );
    }

    private void displayNumberOfCancelledReservations(
            NumberOfCancelledReservations numberOfCancelledReservations
    ) {
        binding.numberOfCancelledReservations.setText(
                String.valueOf(numberOfCancelledReservations.getNumberOfCancelledReservations())
        );
    }

    private void getNumberOfCancelledReservations() {
        Call<NumberOfCancelledReservations> call = ClientUtils
                .reservationService
                .getNumberOfCancelledReservationsForReservee(reserveeBasicInfoDTO.getId());

        call.enqueue(new Callback<NumberOfCancelledReservations>() {
            @Override
            public void onResponse(@NonNull Call<NumberOfCancelledReservations> call,
                                   @NonNull Response<NumberOfCancelledReservations> response) {
                if (response.code() == 200) {
                    assert response.body() != null;
                    displayNumberOfCancelledReservations(response.body());
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
            public void onFailure(@NonNull Call<NumberOfCancelledReservations> call,
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReservationBinding.inflate(inflater, container, false);

        binding.accommodationName.setText(accommodationNameDTO.getName());
        binding.numberOfGuests.setText(
                String.format(numberOfGuests < 2 ? "%s person" : "%s people", numberOfGuests)
        );
        binding.reservationPeriod.setText(getPeriodInDisplayFormat());
        binding.price.setText(String.format("$%s", price));
        if (userRole.equals("Owner")) {
            binding.reservee.setText(String.format(
                    "%s %s",
                    reserveeBasicInfoDTO.getName(),
                    reserveeBasicInfoDTO.getSurname())
            );
            getNumberOfCancelledReservations();
        } else if (userRole.equals("Guest")) {
            binding.reservationOwnerDetails.setVisibility(View.GONE);
        }
        binding.status.setText(status.toString());

        return binding.getRoot();
    }
}