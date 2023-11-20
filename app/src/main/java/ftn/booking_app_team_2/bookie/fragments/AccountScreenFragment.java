package ftn.booking_app_team_2.bookie.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ftn.booking_app_team_2.bookie.R;
import ftn.booking_app_team_2.bookie.databinding.FragmentAccountScreenBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountScreenFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentAccountScreenBinding binding;

    public AccountScreenFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountScreenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountScreenFragment newInstance(String param1, String param2) {
        AccountScreenFragment fragment = new AccountScreenFragment();
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAccountScreenBinding.inflate(inflater, container, false);

        binding.changeDataBtn.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.navigateToAccountChangeScreen);
        });

        binding.logOutBtn.setOnClickListener(view -> {
            // TODO: Add logic for logging the user out.
        });

        return binding.getRoot();
    }
}