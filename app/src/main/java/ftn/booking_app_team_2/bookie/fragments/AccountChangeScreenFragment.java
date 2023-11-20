package ftn.booking_app_team_2.bookie.fragments;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import ftn.booking_app_team_2.bookie.R;
import ftn.booking_app_team_2.bookie.databinding.FragmentAccountChangeScreenBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountChangeScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountChangeScreenFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentAccountChangeScreenBinding binding;

    public AccountChangeScreenFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountChangeScreenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountChangeScreenFragment newInstance(String param1, String param2) {
        AccountChangeScreenFragment fragment = new AccountChangeScreenFragment();
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
        binding = FragmentAccountChangeScreenBinding
                .inflate(inflater, container, false);

        binding.profileImageBtn.setOnClickListener(view -> {
            Snackbar.make(view, R.string.picture_press_stop, Snackbar.LENGTH_SHORT).show();
        });

        binding.cancelBtn.setOnClickListener(view -> {
            Navigation.findNavController(view).navigate(R.id.navigateToAccountScreen);
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@Nullable View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();

        Snackbar.make(requireView(), R.string.change_picture_note, Snackbar.LENGTH_LONG).show();
    }
}