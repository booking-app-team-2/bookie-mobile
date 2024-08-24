package ftn.booking_app_team_2.bookie.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;

import ftn.booking_app_team_2.bookie.R;
import ftn.booking_app_team_2.bookie.activities.MainActivity;
import ftn.booking_app_team_2.bookie.clients.AuthService;
import ftn.booking_app_team_2.bookie.clients.ClientUtils;
import ftn.booking_app_team_2.bookie.databinding.FragmentLoginBinding;
import ftn.booking_app_team_2.bookie.databinding.FragmentRegisterBinding;
import ftn.booking_app_team_2.bookie.model.NewUserDTO;
import ftn.booking_app_team_2.bookie.model.Token;
import ftn.booking_app_team_2.bookie.tools.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentRegisterBinding binding;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        binding.loginBtn.setOnClickListener(view->{
            Navigation.findNavController(view).navigate(R.id.navigateToLogin);
        });

        binding.registerBtn.setOnClickListener(view->{
            String username = binding.usernameField.getText().toString();
            String password = binding.passwordField.getText().toString();
            String name = binding.firstnameField.getText().toString();
            String last_name = binding.lastnameField.getText().toString();
            String address = binding.addressField.getText().toString();
            String phone = binding.phoneField.getText().toString();

            int radioId = binding.role.getCheckedRadioButtonId();
            RadioButton radioButton = (RadioButton) binding.getRoot().findViewById(radioId);

            String role = null;
            if(radioButton.getText().toString().equals("Guest Account")){
                role = "Guest";
            } else {
                role = "Owner";
            }

            if (name.isEmpty()) {
                binding.firstnameField.requestFocus();
                binding.firstnameField.setError("Required");
                showSnackbar(view, "All fields must be filled.");
                return;
            } else if (last_name.isEmpty()) {
                binding.lastnameField.requestFocus();
                binding.lastnameField.setError("Required");
                showSnackbar(view, "All fields must be filled.");
                return;
            } else if (username.isEmpty()) {
                binding.usernameField.requestFocus();
                binding.usernameField.setError("Required");
                showSnackbar(view, "All fields must be filled.");
                return;
            } else if (password.isEmpty()) {
                binding.passwordField.requestFocus();
                binding.passwordField.setError("Required");
                showSnackbar(view, "All fields must be filled.");
                return;
            } else if (address.isEmpty()) {
                binding.addressField.requestFocus();
                binding.addressField.setError("Required");
                showSnackbar(view, "All fields must be filled.");
                return;
            } else if (phone.isEmpty()) {
                binding.phoneField.requestFocus();
                binding.phoneField.setError("Required");
                showSnackbar(view, "All fields must be filled.");
                return;
            }

            NewUserDTO user = new NewUserDTO(username,password,name,last_name,address,phone,role);
            SessionManager sessionManager = new SessionManager(requireContext());

            AuthService service = ClientUtils.getAuthService(getContext());
            Call<NewUserDTO> call = service.register(user);

            call.enqueue(new Callback<NewUserDTO>() {
                @Override
                public void onResponse(Call<NewUserDTO> call, Response<NewUserDTO> response) {
                    if (response.isSuccessful()) {
                        // Extract the token from the response
                        NewUserDTO userResponse = response.body();
                        if (userResponse != null) {
                            Snackbar.make(
                                    requireView(),
                                    "Successful register!",
                                    Snackbar.LENGTH_SHORT
                            ).show();
                            Navigation.findNavController(view).navigate(R.id.navigateToLogin);
                        } else {
                            Snackbar.make(
                                    requireView(),
                                    "Unknown error!",
                                    Snackbar.LENGTH_SHORT
                            ).show();
                        }
                    } else {
                        if(response.code()==401) {
                            Snackbar.make(
                                    requireView(),
                                    "Wrong credentials!",
                                    Snackbar.LENGTH_SHORT
                            ).show();
                        }
                        if(response.code()==500) {
                            Snackbar.make(
                                    requireView(),
                                    "Account already present with that email.",
                                    Snackbar.LENGTH_SHORT
                            ).show();
                        }
                        try {
                            Log.e("API Error", "Error Body: " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<NewUserDTO> call, Throwable t) {
                    Snackbar.make(
                            requireView(),
                            "Error reaching the server.",
                            Snackbar.LENGTH_SHORT
                    ).show();
                }
            });
        });
        return binding.getRoot();
    }

    private void showSnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }
}