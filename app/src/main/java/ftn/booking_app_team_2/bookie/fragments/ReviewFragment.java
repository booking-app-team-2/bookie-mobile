package ftn.booking_app_team_2.bookie.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import ftn.booking_app_team_2.bookie.clients.ClientUtils;
import ftn.booking_app_team_2.bookie.clients.ReviewService;
import ftn.booking_app_team_2.bookie.databinding.FragmentReviewBinding;
import ftn.booking_app_team_2.bookie.model.ReservationStatusDTO;
import ftn.booking_app_team_2.bookie.tools.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReviewFragment extends Fragment {

    Fragment parentFragment;
    private FragmentReviewBinding binding;

    private static final String ARG_ID = "id";
    private static final String ARG_GRADE = "grade";
    private static final String ARG_COMMENT = "comment";
    private static final String ARG_TIMESTAMP_OF_CREATION = "timestampOfCreation";
    private static final String ARG_REVIEWER_ID = "reviewerId";
    private static final String ARG_REVIEWER_NAME = "reviewerName";
    private static final String ARG_REVIEWEE_ID = "revieweeId";
    private static final String ARG_IS_UNAPPROVED_REVIEW = "isUnapprovedReview";
    private static final String ARG_IS_ACCOMMODATION_REVIEW = "isAccommodationReview";

    private Long id;
    private Float grade;
    private String comment;
    private Long timestampOfCreation;
    private Long reviewerId;
    private String reviewerName;
    private Long revieweeId;
    private Boolean isUnapprovedReview;
    private Boolean isAccommodationReview;

    public ReviewFragment() { }


    public static ReviewFragment newInstance(Long id,
                                             Float grade,
                                             String comment,
                                             Long timestampOfCreation,
                                             Long reviewerId,
                                             Long revieweeId,
                                             String reviewerName,
                                             Boolean isUnapprovedReview,
                                             Boolean isAccommodationReview
                                             ) {
        ReviewFragment fragment = new ReviewFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ID, id);
        args.putFloat(ARG_GRADE, grade);
        args.putString(ARG_COMMENT, comment);
        args.putLong(ARG_TIMESTAMP_OF_CREATION, timestampOfCreation);
        args.putLong(ARG_REVIEWER_ID, reviewerId);
        args.putString(ARG_REVIEWER_NAME, reviewerName);
        args.putLong(ARG_REVIEWEE_ID, revieweeId);
        args.putBoolean(ARG_IS_UNAPPROVED_REVIEW,isUnapprovedReview);
        args.putBoolean(ARG_IS_ACCOMMODATION_REVIEW,isAccommodationReview);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        Fragment parent = getParentFragment();
        super.onAttach(context);
        if (parent instanceof ReportedCommentsScreenFragment) {
            parentFragment = (ReportedCommentsScreenFragment) getParentFragment();
        } else {
            parentFragment = (AccommodationDetailsFragment) getParentFragment();
        }
        SessionManager sessionManager = new SessionManager(requireContext());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getLong(ARG_ID);
            grade = getArguments().getFloat(ARG_GRADE);
            comment = getArguments().getString(ARG_COMMENT);
            timestampOfCreation = getArguments().getLong(ARG_TIMESTAMP_OF_CREATION);
            reviewerId = getArguments().getLong(ARG_REVIEWER_ID);
            reviewerName = getArguments().getString(ARG_REVIEWER_NAME);
            revieweeId = getArguments().getLong(ARG_REVIEWEE_ID);
            isUnapprovedReview = getArguments().getBoolean(ARG_IS_UNAPPROVED_REVIEW);
            isAccommodationReview = getArguments().getBoolean(ARG_IS_ACCOMMODATION_REVIEW);

        }
    }

    private String getTimestampToDisplayFormat(){
        LocalDate startDate = Instant
                .ofEpochMilli(timestampOfCreation)
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        return String.format(
                "%s",
                startDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        );
    }

    private void approveReview(){
        if(isAccommodationReview){
            ReviewService service=ClientUtils.getReviewService(getContext());
            Call<Void> call = service.approveAccommodationReview(id);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call,
                                       @NonNull Response<Void> response) {
                    if (response.code() == 200) {
                        Snackbar.make(
                                requireView(),
                                "Review successfully approved.",
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
        else{
            ReviewService service=ClientUtils.getReviewService(getContext());
            Call<Void> call = service.approveOwnerReview(id);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call,
                                       @NonNull Response<Void> response) {
                    if (response.code() == 200) {
                        Snackbar.make(
                                requireView(),
                                "Review successfully approved.",
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

    private void denyReview(){
        if(isAccommodationReview){
            ReviewService service=ClientUtils.getReviewService(getContext());
            Call<Void> call = service.denyAccommodationReview(id);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call,
                                       @NonNull Response<Void> response) {
                    if (response.code() == 200) {
                        Snackbar.make(
                                requireView(),
                                "Review successfully denied.",
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
        else{
            ReviewService service=ClientUtils.getReviewService(getContext());
            Call<Void> call = service.denyOwnerReview(id);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call,
                                       @NonNull Response<Void> response) {
                    if (response.code() == 200) {
                        Snackbar.make(
                                requireView(),
                                "Review successfully denied.",
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

    private void deleteReview(){
        if(isAccommodationReview){
            ReviewService service=ClientUtils.getReviewService(getContext());
            Call<Void> call = service.deleteReportedAccommodationReview(id);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call,
                                       @NonNull Response<Void> response) {
                    if (response.code() == 200) {
                        Snackbar.make(
                                requireView(),
                                "Review successfully deleted.",
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
        else{
            ReviewService service=ClientUtils.getReviewService(getContext());
            Call<Void> call = service.deleteReportedOwnerReview(id);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call,
                                       @NonNull Response<Void> response) {
                    if (response.code() == 200) {
                        Snackbar.make(
                                requireView(),
                                "Review successfully deleted.",
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReviewBinding.inflate(inflater, container, false);
        binding.reviewerName.setText(reviewerName);
        binding.rewieweeName.setText(revieweeId.toString());
        binding.comment.setText(comment);
        binding.grade.setText(grade.toString());
        binding.date.setText(getTimestampToDisplayFormat());
        if(isUnapprovedReview){
            binding.deleteReviewBtn.setVisibility(View.GONE);
            binding.approveReviewBtn.setOnClickListener(view ->
                    new MaterialAlertDialogBuilder(view.getContext())
                            .setTitle("Are you sure you want to accept this review?")
                            .setPositiveButton(
                                    "Confirm",
                                    (dialog, which) -> approveReview()
                            )
                            .setNegativeButton("Cancel", ((dialog, which) -> { }))
                            .show()
            );
            binding.denyReviewBtn.setOnClickListener(view ->
                    new MaterialAlertDialogBuilder(view.getContext())
                            .setTitle("Are you sure you want to deny this review?")
                            .setPositiveButton(
                                    "Confirm",
                                    (dialog, which) -> denyReview()
                            )
                            .setNegativeButton("Cancel", ((dialog, which) -> { }))
                            .show()
            );
        }
        else{
            binding.unapprovedButtonHolder.setVisibility(View.GONE);
            binding.deleteReviewBtn.setOnClickListener(view ->
                    new MaterialAlertDialogBuilder(view.getContext())
                            .setTitle("Are you sure you want to delete this review?")
                            .setPositiveButton(
                                    "Confirm",
                                    (dialog, which) -> deleteReview()
                            )
                            .setNegativeButton("Cancel", ((dialog, which) -> { }))
                            .show()
            );
        }
        SessionManager sessionManager = new SessionManager(requireContext());
        String userRole = sessionManager.getUserType();
        if(!userRole.equals("Admin")){
            binding.unapprovedButtonHolder.setVisibility(View.GONE);
            binding.deleteReviewBtn.setVisibility(View.GONE);
        }
        return binding.getRoot();
    }
}