package ftn.booking_app_team_2.bookie.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import ftn.booking_app_team_2.bookie.R;
import ftn.booking_app_team_2.bookie.clients.ClientUtils;
import ftn.booking_app_team_2.bookie.databinding.FragmentAccountScreenBinding;
import ftn.booking_app_team_2.bookie.model.User;
import ftn.booking_app_team_2.bookie.tools.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountScreenFragment extends Fragment {
    private User user = new User();

    private TextView email;
    private TextView name;
    private TextView surname;
    private TextView addressOfResidence;
    private TextView telephone;

    public AccountScreenFragment() {
    }

    public static AccountScreenFragment newInstance() {
        AccountScreenFragment fragment = new AccountScreenFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (user.getUsername() != null)
            return;

        Call<User> call = ClientUtils.userService.getUser(1L);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.code() == 200) {
                    user = response.body();
                    assert user != null;

                    email.setText(user.getUsername());
                    name.setText(user.getName());
                    surname.setText(user.getSurname());
                    addressOfResidence.setText(user.getAddressOfResidence());
                    telephone.setText(user.getTelephone());
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
            public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
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
        FragmentAccountScreenBinding binding =
                FragmentAccountScreenBinding.inflate(inflater, container, false);

        email = binding.email;
        name = binding.name;
        surname = binding.surname;
        addressOfResidence = binding.addressOfResidence;
        telephone = binding.telephone;

        binding.changeDataBtn.setOnClickListener(view ->
            Navigation.findNavController(view).navigate(R.id.navigateToAccountChangeScreen)
        );

        binding.deleteBtn.setOnClickListener(view -> {

        });

        binding.logOutBtn.setOnClickListener(view -> {
            SessionManager sessionManager = new SessionManager(requireContext());
            sessionManager.logoutUser();
        });

        return binding.getRoot();
    }
}