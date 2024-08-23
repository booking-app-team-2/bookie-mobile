package ftn.booking_app_team_2.bookie.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;

import java.util.Objects;

import ftn.booking_app_team_2.bookie.R;
import ftn.booking_app_team_2.bookie.clients.ClientUtils;
import ftn.booking_app_team_2.bookie.clients.UserService;
import ftn.booking_app_team_2.bookie.databinding.FragmentAccountChangeScreenBinding;
import ftn.booking_app_team_2.bookie.model.User;
import ftn.booking_app_team_2.bookie.model.UserAddress;
import ftn.booking_app_team_2.bookie.model.UserBasicInfo;
import ftn.booking_app_team_2.bookie.model.UserPassword;
import ftn.booking_app_team_2.bookie.model.UserTelephone;
import ftn.booking_app_team_2.bookie.tools.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountChangeScreenFragment extends Fragment {
    private User user = new User();

    private Long userId;

    private TextInputEditText name;
    private TextInputEditText surname;
    private TextInputEditText telephone;
    private TextInputEditText addressOfResidence;
    private TextInputEditText currentPassword;
    private TextInputEditText newPassword;

    public AccountChangeScreenFragment() {
    }

    public static AccountChangeScreenFragment newInstance() {
        AccountChangeScreenFragment fragment = new AccountChangeScreenFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        SessionManager sessionManager = new SessionManager(requireContext());
        userId = sessionManager.getUserId();
    }

    public void populateView() {
        name.setText(user.getName());
        surname.setText(user.getSurname());
        telephone.setText(user.getTelephone());
        addressOfResidence.setText(user.getAddressOfResidence());
    }

    private void getUser() {
        UserService service = ClientUtils.getUserService(getContext());
        Call<User> call = service.getUser(userId);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(@NonNull Call<User> call, @NonNull Response<User> response) {
                if (response.code() == 200) {
                    user = response.body();
                    assert user != null;

                    populateView();
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
    public void onResume() {
        super.onResume();

        if (user.getUsername() != null) {
            populateView();
            return;
        }

        getUser();
    }

    private void updateUserBasicInfo() {
        UserService service = ClientUtils.getUserService(getContext());
        Call<UserBasicInfo> call = service.putUserBasicInfo(
                userId,
                new UserBasicInfo(
                        Objects.requireNonNull(name.getText()).toString(),
                        Objects.requireNonNull(surname.getText()).toString()
                )
        );
        call.enqueue(new Callback<UserBasicInfo>() {
            @Override
            public void onResponse(@NonNull Call<UserBasicInfo> call,
                                   @NonNull Response<UserBasicInfo> response) {
                if (response.code() == 200) {
                    Snackbar.make(
                            requireView(),
                            "Basic info successfully updated.",
                            Snackbar.LENGTH_SHORT
                    ).show();
                    getUser();
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
            public void onFailure(@NonNull Call<UserBasicInfo> call, @NonNull Throwable t) {
                Snackbar.make(
                        requireView(),
                        "Error reaching the server.",
                        Snackbar.LENGTH_SHORT
                ).show();
            }
        });
    }

    private void updateUserTelephone() {
        UserService service = ClientUtils.getUserService(getContext());
        Call<UserTelephone> call = service.putUserTelephone(
                userId,
                new UserTelephone(
                        Objects.requireNonNull(telephone.getText()).toString()
                )
        );
        call.enqueue(new Callback<UserTelephone>() {
            @Override
            public void onResponse(@NonNull Call<UserTelephone> call,
                                   @NonNull Response<UserTelephone> response) {
                if (response.code() == 200) {
                    Snackbar.make(
                            requireView(),
                            "Telephone successfully updated.",
                            Snackbar.LENGTH_SHORT
                    ).show();
                    getUser();
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
            public void onFailure(@NonNull Call<UserTelephone> call, @NonNull Throwable t) {
                Snackbar.make(
                        requireView(),
                        "Error reaching the server.",
                        Snackbar.LENGTH_SHORT
                ).show();
            }
        });
    }

    private void updateUserAddress() {
        UserService service = ClientUtils.getUserService(getContext());
        Call<UserAddress> call = service.putUserAddress(
                userId,
                new UserAddress(
                        Objects.requireNonNull(addressOfResidence.getText()).toString()
                )
        );
        call.enqueue(new Callback<UserAddress>() {
            @Override
            public void onResponse(@NonNull Call<UserAddress> call,
                                   @NonNull Response<UserAddress> response) {
                if (response.code() == 200) {
                    Snackbar.make(
                            requireView(),
                            "Address of residence successfully updated.",
                            Snackbar.LENGTH_SHORT
                    ).show();
                    getUser();
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
            public void onFailure(@NonNull Call<UserAddress> call, @NonNull Throwable t) {
                Snackbar.make(
                        requireView(),
                        "Error reaching the server.",
                        Snackbar.LENGTH_SHORT
                ).show();
            }
        });
    }

    private void updateUserPassword() {
        UserService service = ClientUtils.getUserService(getContext());
        Call<UserPassword> call = service.putUserPassword(
                userId,
                new UserPassword(
                        Objects.requireNonNull(currentPassword.getText()).toString(),
                        Objects.requireNonNull(newPassword.getText()).toString()
                )
        );
        call.enqueue(new Callback<UserPassword>() {
            @Override
            public void onResponse(@NonNull Call<UserPassword> call,
                                   @NonNull Response<UserPassword> response) {
                if (response.code() == 200) {
                    Snackbar.make(
                            requireView(),
                            "Password successfully updated.",
                            Snackbar.LENGTH_SHORT
                    ).show();
                    getUser();
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
            public void onFailure(@NonNull Call<UserPassword> call, @NonNull Throwable t) {
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
        FragmentAccountChangeScreenBinding binding = FragmentAccountChangeScreenBinding
                .inflate(inflater, container, false);

        name = binding.name;
        surname = binding.surname;
        telephone = binding.telephone;
        addressOfResidence = binding.addressOfResidence;
        currentPassword = binding.currentPassword;
        newPassword = binding.newPassword;

        binding.userUpdateBasicInfo.setOnClickListener(view ->
                new MaterialAlertDialogBuilder(view.getContext())
                        .setTitle("Are you sure you want to update your basic info?")
                        .setPositiveButton("Confirm",
                                ((dialog, which) -> updateUserBasicInfo()))
                        .setNegativeButton("Cancel", ((dialog, which) -> { }))
                        .show()
        );
        binding.updateUserTelephone.setOnClickListener(view ->
                new MaterialAlertDialogBuilder(view.getContext())
                        .setTitle("Are you sure you want to update your telephone?")
                        .setPositiveButton("Confirm",
                                ((dialog, which) -> updateUserTelephone()))
                        .setNegativeButton("Cancel", ((dialog, which) -> { }))
                        .show()
        );
        binding.updateUserAddress.setOnClickListener(view ->
                new MaterialAlertDialogBuilder(view.getContext())
                        .setTitle("Are you sure you want to update your address of residence?")
                        .setPositiveButton("Confirm",
                                ((dialog, which) -> updateUserAddress()))
                        .setNegativeButton("Cancel", ((dialog, which) -> { }))
                        .show()
        );
        binding.updateUserPassword.setOnClickListener(view ->
                new MaterialAlertDialogBuilder(view.getContext())
                        .setTitle("Are you sure you want to update your password?")
                        .setPositiveButton("Confirm",
                                ((dialog, which) -> updateUserPassword()))
                        .setNegativeButton("Cancel", ((dialog, which) -> { }))
                        .show()
        );

        binding.cancelBtn.setOnClickListener(view ->
            Navigation.findNavController(view).navigate(R.id.navigateToAccountScreen)
        );

        return binding.getRoot();
    }
}