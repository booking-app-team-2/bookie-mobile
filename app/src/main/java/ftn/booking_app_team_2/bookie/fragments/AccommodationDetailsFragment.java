package ftn.booking_app_team_2.bookie.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

import ftn.booking_app_team_2.bookie.R;
import ftn.booking_app_team_2.bookie.clients.AccommodationService;
import ftn.booking_app_team_2.bookie.clients.ClientUtils;
import ftn.booking_app_team_2.bookie.clients.GuestService;
import ftn.booking_app_team_2.bookie.clients.ReviewService;
import ftn.booking_app_team_2.bookie.databinding.FragmentAccommodationDetailsBinding;
import ftn.booking_app_team_2.bookie.dialogs.CreateReservationDialog;
import ftn.booking_app_team_2.bookie.model.AccommodationApproval;
import ftn.booking_app_team_2.bookie.model.AccommodationDTO;
import ftn.booking_app_team_2.bookie.model.AccommodationReviewDTO;
import ftn.booking_app_team_2.bookie.model.AvailabilityPeriod;
import ftn.booking_app_team_2.bookie.tools.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccommodationDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccommodationDetailsFragment extends Fragment {
    private Collection<AccommodationReviewDTO> reviews = null;

    public AccommodationDetailsFragment() {

    }
    private Pair<Long, Long> selectedDateRange;
    private FragmentAccommodationDetailsBinding binding;
    public static GuestMainScreenFragment newInstance() {
        GuestMainScreenFragment fragment = new GuestMainScreenFragment();
        return fragment;
    }
    private Long accommodationId;
    private Long userId;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        SessionManager sessionManager = new SessionManager(requireContext());
        userId = sessionManager.getUserId();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void updateIsApproved(boolean isApproved) {
        AccommodationService service = ClientUtils.getAccommodationService(getContext());
        assert getArguments() != null;
        Call<AccommodationApproval> call = service.putIsApproved(
                getArguments().getLong("accommodationId"),
                new AccommodationApproval(isApproved)
        );
        call.enqueue(new Callback<AccommodationApproval>() {
            @Override
            public void onResponse(@NonNull Call<AccommodationApproval> call,
                                   @NonNull Response<AccommodationApproval> response) {
                if (response.code() == 200) {
                    requireActivity().getSupportFragmentManager().popBackStack();
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
            public void onFailure(@NonNull Call<AccommodationApproval> call,
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentAccommodationDetailsBinding.inflate(inflater, container, false);
        View root=binding.getRoot();

        SessionManager sessionManager = new SessionManager(requireContext());
        String userRole = sessionManager.getUserType();

        if (userRole.equals("Guest")) {
            binding.approvalLayout.setVisibility(View.GONE);
            root.findViewById(R.id.reserveButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog();
                }
            });
        }
        else if (userRole.equals("Admin")) {
            binding.reservationLayout.setVisibility(View.GONE);

            binding.approveBtn.setOnClickListener(view ->
                    new MaterialAlertDialogBuilder(view.getContext())
                            .setTitle("Are you sure you want to approve this accommodation?")
                            .setPositiveButton("Confirm", ((dialog, which) ->
                                    updateIsApproved(true)))
                            .setNegativeButton("Cancel", ((dialog, which) -> { }))
                            .show()

            );
            binding.sendBackBtn.setOnClickListener(view ->
                    new MaterialAlertDialogBuilder(view.getContext())
                            .setTitle("Are you sure you want to send this accommodation back for" +
                                    " revision")
                            .setPositiveButton("Confirm", ((dialog, which) ->
                                    updateIsApproved(false)))
                            .setNegativeButton("Cancel", ((dialog, which) -> { }))
                            .show()
            );
        }
        else if(userRole.equals("Owner")){

        }
        Bundle args = getArguments();
        if (args != null) {
            accommodationId = args.getLong("accommodationId");
            //TODO: Add getReqest and connect to other fields
            loadAccommodation(accommodationId,root);
        }
        binding.favouriteButton.setOnClickListener(view ->
                new MaterialAlertDialogBuilder(view.getContext())
                        .setTitle("Are you sure you want to make this your favourite accommodation?")
                        .setPositiveButton("Yes", ((dialog, which) ->
                                setFavouriteAccommodation()))
                        .setNegativeButton("No", ((dialog, which) -> { }))
                        .show()
        );
        loadReviews(accommodationId);
        return root;
    }

    private void loadReviews(Long accommodationId) {
        ReviewService service = ClientUtils.getReviewService(getContext());
        Call<Collection<AccommodationReviewDTO>> call = service.getReviews(accommodationId);
        call.enqueue(new Callback<Collection<AccommodationReviewDTO>>() {
            @Override
            public void onResponse(Call<Collection<AccommodationReviewDTO>> call, Response<Collection<AccommodationReviewDTO>> response) {
                reviews = response.body();
                if(reviews != null && !reviews.isEmpty()) {
                    binding.textView2.setVisibility(View.GONE);
                    reviews.forEach(review -> {
                        ReviewFragment reviewFragment = ReviewFragment
                                .newInstance(
                                        review.getId(),
                                        review.getGrade(),
                                        review.getComment(),
                                        review.getTimestampOfCreation(),
                                        review.getReviewerId(),
                                        (long) 0,
                                        review.getReviewerName(),
                                        false,
                                        true
                                );

                        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                        transaction.add(binding.reviewsContainer.getId(), reviewFragment);
                        transaction.commit();
                    });
                }
            }

            @Override
            public void onFailure(Call<Collection<AccommodationReviewDTO>> call, Throwable t) {

            }
        });
    }

    private void showDialog() {
        CreateReservationDialog dialog=new CreateReservationDialog(requireContext());
        dialog.show();
    }

    private void loadAccommodation(Long id,View view){
        AccommodationService service = ClientUtils.getAccommodationService(getContext());

        Call<AccommodationDTO> call = service.getAccommodation(id);
        call.enqueue(new Callback<AccommodationDTO>() {
            @Override
            public void onResponse(Call<AccommodationDTO> call, Response<AccommodationDTO> response) {
                if (response.isSuccessful()) {
                    AccommodationDTO accommodation = response.body();
                    assert accommodation != null;
                    MaterialTextView nameText=view.findViewById(R.id.nameTextView);
                    nameText.setText(accommodation.getName());
                    TextInputEditText descText=view.findViewById(R.id.desciptionField);
                    descText.setText(accommodation.getDescription());
                    TextInputEditText amenitiesText=view.findViewById(R.id.amenityField);
                    amenitiesText.setText("Accommodation amenities: "+accommodation.getAmenities().toString());
                    TextInputEditText guestText=view.findViewById(R.id.guestCountfield);
                    guestText.setText(accommodation.getMinimumGuests()+" to "+accommodation.getMaximumGuests()+" people");
                    TextInputEditText typeText=view.findViewById(R.id.typeField);
                    typeText.setText(accommodation.getType().toString());
                    for(AvailabilityPeriod availabilityPeriod:accommodation.getAvailabilityPeriods()){

                        AvailabilityPeriodFragment fragment=AvailabilityPeriodFragment.newInstance(
                                availabilityPeriod.getPeriod().getStartDate(),
                                availabilityPeriod.getPeriod().getEndDate(),
                                availabilityPeriod.getPrice().doubleValue()
                        );

                        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                        transaction.add(R.id.periodLayout, fragment);
                        transaction.commit();
                    }

                } else {
                    Log.e("TAG","NE RADI");
                }
            }

            @Override
            public void onFailure(Call<AccommodationDTO> call, Throwable t) {
                Log.e("TAG",t.toString());
            }
        });
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


        }
    }

    private void setFavouriteAccommodation(){
        GuestService service= ClientUtils.getGuestService(getContext());
        Call<Void> call = service.addFavouriteAccommodations(userId,accommodationId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call,
                                   @NonNull Response<Void> response) {
                if (response.code() == 200) {
                    Snackbar.make(
                            requireView(),
                            "Successfully added to favourite accommodations",
                            Snackbar.LENGTH_SHORT
                    ).show();

                    //TODO Add call for refreshing
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
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Snackbar.make(
                        requireView(),
                        "Error reaching the server.",
                        Snackbar.LENGTH_SHORT
                ).show();
            }
        });
    }
}