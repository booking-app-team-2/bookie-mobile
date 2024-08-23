package ftn.booking_app_team_2.bookie.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;

import ftn.booking_app_team_2.bookie.R;
import ftn.booking_app_team_2.bookie.activities.MainActivity;
import ftn.booking_app_team_2.bookie.clients.AuthService;
import ftn.booking_app_team_2.bookie.clients.ClientUtils;
import ftn.booking_app_team_2.bookie.databinding.FragmentLoginBinding;
import ftn.booking_app_team_2.bookie.model.LoginCredentials;
import ftn.booking_app_team_2.bookie.model.Token;
import ftn.booking_app_team_2.bookie.tools.SessionManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentLoginBinding binding;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
        binding = FragmentLoginBinding.inflate(inflater, container, false);

        binding.registerBtn.setOnClickListener(view->{
            Navigation.findNavController(view).navigate(R.id.navigateToRegister);
        });

        binding.loginBtn.setOnClickListener(view -> {

            String username = binding.usernameField.getText().toString();
            String password = binding.passwordField.getText().toString();

            LoginCredentials credentials = new LoginCredentials(username,password);

            SessionManager sessionManager = new SessionManager(requireContext());
            AuthService service = ClientUtils.getAuthService(getContext());
            Call<Token> call = service.getToken(credentials);

            call.enqueue(new Callback<Token>() {
                @Override
                public void onResponse(Call<Token> call, Response<Token> response) {
                    if (response.isSuccessful()) {
                        // Extract the token from the response
                        Token tokenResponse = response.body();
                        if (tokenResponse != null) {
                            String jWT = tokenResponse.getjWT();

                            // Save the token using SessionManager
                            sessionManager.createLoginSession(jWT);

                            // Start the MainActivity
                            Intent intent = new Intent(requireActivity(), MainActivity.class);
                            startActivity(intent);
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
                        try {
                            Log.e("API Error", "Error Body: " + response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Token> call, Throwable t) {
                    Snackbar.make(
                            requireView(),
                            "Error reaching the server.",
                            Snackbar.LENGTH_SHORT
                    ).show();
                }
            });
//          sessionManager.createLoginSession(ClientUtils.JWT);

//          Intent intent = new Intent(requireActivity(), MainActivity.class);
//          startActivity(intent);
        });

        return binding.getRoot();
    }
}