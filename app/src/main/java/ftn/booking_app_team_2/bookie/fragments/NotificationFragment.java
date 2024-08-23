package ftn.booking_app_team_2.bookie.fragments;

import android.app.Notification;
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

import ftn.booking_app_team_2.bookie.clients.ClientUtils;
import ftn.booking_app_team_2.bookie.clients.NotificationService;
import ftn.booking_app_team_2.bookie.databinding.FragmentNotificationBinding;
import ftn.booking_app_team_2.bookie.tools.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationFragment extends Fragment {

    NotificationsScreenFragment parentFragment;
    private FragmentNotificationBinding binding;

    private static final String ARG_ID = "id";
    private static final String ARG_BODY = "body";
    private static final String ARG_TYPE = "type";
    private static final String ARG_RECEIVER_ID = "receiverId";

    private Long id;
    private String body;
    private String type;
    private String receiverId;

    public NotificationFragment() {

    }


    public static NotificationFragment newInstance(Long id,String body,String type,String receiverId) {
        NotificationFragment fragment = new NotificationFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ID, id);
        args.putString(ARG_BODY,body);
        args.putString(ARG_TYPE,type);
        args.putString(ARG_RECEIVER_ID,receiverId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        parentFragment = (NotificationsScreenFragment) getParentFragment();
        SessionManager sessionManager = new SessionManager(requireContext());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id=getArguments().getLong(ARG_ID);
            body=getArguments().getString(ARG_BODY);
            type =getArguments().getString(ARG_TYPE);
            receiverId =getArguments().getString(ARG_RECEIVER_ID);
        }
    }

    private void deleteNotification(){
        NotificationService service=ClientUtils.getNotificationService(getContext());
        Call<Void> call = service.deleteNotification(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call,
                                   @NonNull Response<Void> response) {
                if (response.code() == 200) {
                    Snackbar.make(
                            requireView(),
                            "Notification successfully deleted",
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
        binding = FragmentNotificationBinding.inflate(inflater, container, false);
        binding.body.setText(body);
        binding.deleteNotificationBtn.setOnClickListener(view ->
                new MaterialAlertDialogBuilder(view.getContext())
                        .setTitle("Are you sure you want to delete this notification?")
                        .setPositiveButton(
                                "Confirm",
                                (dialog, which) -> deleteNotification()
                        )
                        .setNegativeButton("Cancel", ((dialog, which) -> { }))
                        .show()
        );
        return binding.getRoot();
    }
}