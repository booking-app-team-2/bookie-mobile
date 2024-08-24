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

import ftn.booking_app_team_2.bookie.R;
import ftn.booking_app_team_2.bookie.clients.ClientUtils;
import ftn.booking_app_team_2.bookie.clients.ReportService;
import ftn.booking_app_team_2.bookie.databinding.FragmentReportedCommentsScreenBinding;
import ftn.booking_app_team_2.bookie.databinding.FragmentReportedUsersScreenBinding;
import ftn.booking_app_team_2.bookie.model.AccommodationReviewDTO;
import ftn.booking_app_team_2.bookie.model.ReportDTO;
import ftn.booking_app_team_2.bookie.tools.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReportedUsersScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportedUsersScreenFragment extends Fragment {

    private FragmentReportedUsersScreenBinding binding;

    private Collection<ReportDTO> reports = null;

    public ReportedUsersScreenFragment() {
    }


    public static ReportedUsersScreenFragment newInstance() {
        ReportedUsersScreenFragment fragment = new ReportedUsersScreenFragment();
        return fragment;
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

    private void removeAllReports() {
        if (!isAdded())
            return;

        getChildFragmentManager().getFragments().forEach(childFragment -> {
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.remove(childFragment);
            fragmentTransaction.commit();
        });
    }

    private void addAllReports(){
        if(!isAdded()){
            return;
        }
        reports.forEach(report ->{
            ReportFragment reportFragment = ReportFragment.newInstance(
                    report.getId(), report.getBody(), report.getReporterName(), report.getReporteeName()
            );
            FragmentTransaction fragmentTransaction =
                    getChildFragmentManager().beginTransaction();
            fragmentTransaction.add(binding.reportsContainer.getId(), reportFragment);
            fragmentTransaction.commit();
        });
    }

    protected void getReports() {
        ReportService service = ClientUtils.getReportService(getContext());
        Call<Collection<ReportDTO>> call =
                service.getAllReports();

        call.enqueue(new Callback<Collection<ReportDTO>>() {
            @Override
            public void onResponse(@NonNull Call<Collection<ReportDTO>> call,
                                   @NonNull Response<Collection<ReportDTO>> response) {
                if (response.code() == 200) {
                    assert response.body() != null;
                    reports = response.body();
                    addAllReports();
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
            public void onFailure(@NonNull Call<Collection<ReportDTO>> call,
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
        removeAllReports();
        getReports();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReportedUsersScreenBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}