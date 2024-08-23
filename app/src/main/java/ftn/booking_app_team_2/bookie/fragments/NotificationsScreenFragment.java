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
import ftn.booking_app_team_2.bookie.clients.NotificationService;
import ftn.booking_app_team_2.bookie.databinding.FragmentNotificationsScreenBinding;
import ftn.booking_app_team_2.bookie.model.NotificationDTO;
import ftn.booking_app_team_2.bookie.tools.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotificationsScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationsScreenFragment extends Fragment {

   private FragmentNotificationsScreenBinding binding;

   private Collection<NotificationDTO> notifications = null;

   private Long userId;

    public NotificationsScreenFragment() {

    }


    public static NotificationsScreenFragment newInstance() {
        return new NotificationsScreenFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        SessionManager sessionManager = new SessionManager(requireContext());
        userId = sessionManager.getUserId();
    }

    private void removeAllNotifications() {
        if (!isAdded())
            return;

        getChildFragmentManager().getFragments().forEach(childFragment -> {
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.remove(childFragment);
            fragmentTransaction.commit();
        });
    }

    private void addAllNotifications(){
        if(!isAdded()){
            return;
        }
        notifications.forEach(notification ->{
            NotificationFragment reportFragment = NotificationFragment.newInstance(
                    notification.getId(), notification.getBody(), notification.getType(), notification.getReceiverId()
            );
            FragmentTransaction fragmentTransaction =
                    getChildFragmentManager().beginTransaction();
            fragmentTransaction.add(binding.notificationsContainer.getId(), reportFragment);
            fragmentTransaction.commit();
        });
    }

    protected void getNotifications() {
        NotificationService service=ClientUtils.getNotificationService(getContext());
        Call<Collection<NotificationDTO>> call =
                service.getUserNotifications(userId);

        call.enqueue(new Callback<Collection<NotificationDTO>>() {
            @Override
            public void onResponse(@NonNull Call<Collection<NotificationDTO>> call,
                                   @NonNull Response<Collection<NotificationDTO>> response) {
                if (response.code() == 200) {
                    assert response.body() != null;
                    notifications = response.body();
                    addAllNotifications();
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
            public void onFailure(@NonNull Call<Collection<NotificationDTO>> call,
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
        removeAllNotifications();
        getNotifications();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNotificationsScreenBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}