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

import ftn.booking_app_team_2.bookie.R;
import ftn.booking_app_team_2.bookie.clients.ClientUtils;
import ftn.booking_app_team_2.bookie.clients.ReportService;
import ftn.booking_app_team_2.bookie.databinding.FragmentReportBinding;
import ftn.booking_app_team_2.bookie.tools.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReportFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportFragment extends Fragment {

    ReportedUsersScreenFragment parentFragment;
    private FragmentReportBinding binding;

    private static final String ARG_ID = "id";
    private static final String ARG_BODY = "body";
    private static final String ARG_REPORTER_NAME = "reporterName";
    private static final String ARG_REPORTEE_NAME = "reporteeName";

    private Long id;
    private String body;
    private String reporterName;
    private String reporteeName;

    public ReportFragment() {

    }

    public static ReportFragment newInstance(Long id,String body,String reporterName,String reporteeName) {
        ReportFragment fragment = new ReportFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ID, id);
        args.putString(ARG_BODY,body);
        args.putString(ARG_REPORTER_NAME,reporterName);
        args.putString(ARG_REPORTEE_NAME,reporteeName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        parentFragment = (ReportedUsersScreenFragment) getParentFragment();
        SessionManager sessionManager = new SessionManager(requireContext());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id=getArguments().getLong(ARG_ID);
            body=getArguments().getString(ARG_BODY);
            reporterName =getArguments().getString(ARG_REPORTER_NAME);
            reporteeName =getArguments().getString(ARG_REPORTEE_NAME);
        }
    }
    private void blockUser(){
        ReportService service=ClientUtils.getReportService(getContext());
        Call<Void> call = service.blockUser(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call,
                                   @NonNull Response<Void> response) {
                if (response.code() == 200) {
                    Snackbar.make(
                            requireView(),
                            "User successfully blocked.",
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       binding= FragmentReportBinding.inflate(inflater, container, false);
       binding.reporterName.setText(reporterName);
       binding.reporteeName.setText(reporteeName);
       binding.body.setText(body);
       binding.blockUserBtn.setOnClickListener(view ->
                new MaterialAlertDialogBuilder(view.getContext())
                        .setTitle("Are you sure you want to block the reported user?")
                        .setPositiveButton(
                                "Confirm",
                                (dialog, which) -> blockUser()
                        )
                        .setNegativeButton("Cancel", ((dialog, which) -> { }))
                        .show()
       );
       return binding.getRoot();
    }
}