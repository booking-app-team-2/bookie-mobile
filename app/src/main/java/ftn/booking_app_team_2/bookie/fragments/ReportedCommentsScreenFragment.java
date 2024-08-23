package ftn.booking_app_team_2.bookie.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.util.Collection;
import java.util.Objects;

import ftn.booking_app_team_2.bookie.R;
import ftn.booking_app_team_2.bookie.clients.ClientUtils;
import ftn.booking_app_team_2.bookie.databinding.FragmentReportedCommentsScreenBinding;
import ftn.booking_app_team_2.bookie.databinding.FragmentReservationsScreenBinding;
import ftn.booking_app_team_2.bookie.model.AccommodationReviewDTO;
import ftn.booking_app_team_2.bookie.model.OwnerReviewDTO;
import ftn.booking_app_team_2.bookie.model.ReservationOwner;
import ftn.booking_app_team_2.bookie.tools.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReportedCommentsScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportedCommentsScreenFragment extends Fragment {

    private FragmentReportedCommentsScreenBinding binding;

    private Collection<AccommodationReviewDTO> unapprovedAccommodation = null;
    private Collection<AccommodationReviewDTO> reportedAccommodation = null;
    private Collection<OwnerReviewDTO> unapprovedOwner = null;
    private Collection<OwnerReviewDTO> reportedOwner = null;

    public ReportedCommentsScreenFragment() {

    }

    public static ReportedCommentsScreenFragment newInstance(){
        return new ReportedCommentsScreenFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        SessionManager sessionManager = new SessionManager(requireContext());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void removeAllReviewViews() {
        if (!isAdded())
            return;

        getChildFragmentManager().getFragments().forEach(childFragment -> {
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.remove(childFragment);
            fragmentTransaction.commit();
        });
    }

    private void addUnapprovedAccommodationReviews(){
        if(!isAdded()){
            return;
        }
        unapprovedAccommodation.forEach(unapprovedAcc ->{
            ReviewFragment reviewFragment = ReviewFragment.newInstance(
                    unapprovedAcc.getId(),unapprovedAcc.getGrade(),unapprovedAcc.getComment(),unapprovedAcc.getTimestampOfCreation(),unapprovedAcc.getReviewerId(),unapprovedAcc.getAccommodationId(),unapprovedAcc.getReviewerName(),true,true
            );
            FragmentTransaction fragmentTransaction =
                    getChildFragmentManager().beginTransaction();
            fragmentTransaction.add(binding.unapprovedAccommodationCommentsContainer.getId(), reviewFragment);
            fragmentTransaction.commit();
        });
    }

    private void addReportedAccommodationReviews(){
        if(!isAdded()){
            return;
        }
        reportedAccommodation.forEach(reportedAcc ->{
            ReviewFragment reviewFragment = ReviewFragment.newInstance(
                    reportedAcc.getId(),reportedAcc.getGrade(),reportedAcc.getComment(),reportedAcc.getTimestampOfCreation(),reportedAcc.getReviewerId(),reportedAcc.getAccommodationId(),reportedAcc.getReviewerName(),false,true
            );
            FragmentTransaction fragmentTransaction =
                    getChildFragmentManager().beginTransaction();
            fragmentTransaction.add(binding.reportedAccommodationCommentsContainer.getId(), reviewFragment);
            fragmentTransaction.commit();
        });
    }

    private void addUnapprovedOwnerReviews(){
        if(!isAdded()){
            return;
        }
        unapprovedOwner.forEach(unapprovedOwn ->{
            ReviewFragment reviewFragment = ReviewFragment.newInstance(
                    unapprovedOwn.getId(),unapprovedOwn.getGrade(),unapprovedOwn.getComment(),unapprovedOwn.getTimestampOfCreation(),unapprovedOwn.getReviewerId(),unapprovedOwn.getRevieweeId(),unapprovedOwn.getReviewerName(),true,false
            );
            FragmentTransaction fragmentTransaction =
                    getChildFragmentManager().beginTransaction();
            fragmentTransaction.add(binding.unapprovedOwnerCommentsContainer.getId(), reviewFragment);
            fragmentTransaction.commit();
        });
    }

    private void addReportedOwnerReviews(){
        if(!isAdded()){
            return;
        }
        reportedOwner.forEach(reportedOwn ->{
            ReviewFragment reviewFragment = ReviewFragment.newInstance(
                    reportedOwn.getId(),reportedOwn.getGrade(),reportedOwn.getComment(),reportedOwn.getTimestampOfCreation(),reportedOwn.getReviewerId(),reportedOwn.getRevieweeId(),reportedOwn.getReviewerName(),false,false
            );
            FragmentTransaction fragmentTransaction =
                    getChildFragmentManager().beginTransaction();
            fragmentTransaction.add(binding.reportedOwnerCommentsContainer.getId(), reviewFragment);
            fragmentTransaction.commit();
        });
    }


    protected void getUnapprovedAccommodationReviews() {
        Call<Collection<AccommodationReviewDTO>> call =
                ClientUtils.reviewService.getUnapprovedAccomodationReviews(
                );

        call.enqueue(new Callback<Collection<AccommodationReviewDTO>>() {
            @Override
            public void onResponse(@NonNull Call<Collection<AccommodationReviewDTO>> call,
                                   @NonNull Response<Collection<AccommodationReviewDTO>> response) {
                if (response.code() == 200) {
                    assert response.body() != null;
                    unapprovedAccommodation = response.body();
                    addUnapprovedAccommodationReviews();
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
            public void onFailure(@NonNull Call<Collection<AccommodationReviewDTO>> call,
                                  @NonNull Throwable t) {
                Snackbar.make(
                        requireView(),
                        "Error reaching the server.",
                        Snackbar.LENGTH_SHORT
                ).show();
            }
        });
    }

    protected void getReportedAccommodationReviews() {
        Call<Collection<AccommodationReviewDTO>> call =
                ClientUtils.reviewService.getReportedAccomodationReviews();

        call.enqueue(new Callback<Collection<AccommodationReviewDTO>>() {
            @Override
            public void onResponse(@NonNull Call<Collection<AccommodationReviewDTO>> call,
                                   @NonNull Response<Collection<AccommodationReviewDTO>> response) {
                if (response.code() == 200) {
                    assert response.body() != null;
                    reportedAccommodation = response.body();
                    addReportedAccommodationReviews();
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
            public void onFailure(@NonNull Call<Collection<AccommodationReviewDTO>> call,
                                  @NonNull Throwable t) {
                Snackbar.make(
                        requireView(),
                        "Error reaching the server.",
                        Snackbar.LENGTH_SHORT
                ).show();
            }
        });
    }

    protected void getUnapprovedOwnerReviews() {
        Call<Collection<OwnerReviewDTO>> call =
                ClientUtils.reviewService.getUnapprovedOwnerReviews(
                );

        call.enqueue(new Callback<Collection<OwnerReviewDTO>>() {
            @Override
            public void onResponse(@NonNull Call<Collection<OwnerReviewDTO>> call,
                                   @NonNull Response<Collection<OwnerReviewDTO>> response) {
                if (response.code() == 200) {
                    assert response.body() != null;
                    unapprovedOwner = response.body();
                    addUnapprovedOwnerReviews();
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
            public void onFailure(@NonNull Call<Collection<OwnerReviewDTO>> call,
                                  @NonNull Throwable t) {
                Snackbar.make(
                        requireView(),
                        "Error reaching the server.",
                        Snackbar.LENGTH_SHORT
                ).show();
            }
        });
    }

    protected void getReportedOwnerReviews() {
        Call<Collection<OwnerReviewDTO>> call =
                ClientUtils.reviewService.getReportedOwnerReviews();

        call.enqueue(new Callback<Collection<OwnerReviewDTO>>() {
            @Override
            public void onResponse(@NonNull Call<Collection<OwnerReviewDTO>> call,
                                   @NonNull Response<Collection<OwnerReviewDTO>> response) {
                if (response.code() == 200) {
                    assert response.body() != null;
                    reportedOwner = response.body();
                    addReportedOwnerReviews();
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
            public void onFailure(@NonNull Call<Collection<OwnerReviewDTO>> call,
                                  @NonNull Throwable t) {
                Snackbar.make(
                        requireView(),
                        "Error reaching the server.",
                        Snackbar.LENGTH_SHORT
                ).show();
            }
        });
    }

    protected void getAllReviews(){
        getUnapprovedAccommodationReviews();
        getReportedAccommodationReviews();
        getUnapprovedOwnerReviews();
        getReportedOwnerReviews();
    }

    @Override
    public void onResume() {
        super.onResume();
        removeAllReviewViews();
        getAllReviews();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReportedCommentsScreenBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}