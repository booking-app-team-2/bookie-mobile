package ftn.booking_app_team_2.bookie.fragments;

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

import ftn.booking_app_team_2.bookie.clients.ClientUtils;
import ftn.booking_app_team_2.bookie.databinding.FragmentAccommodationsApprovalScreenBinding;
import ftn.booking_app_team_2.bookie.model.AccommodationDTO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccommodationsApprovalScreenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccommodationsApprovalScreenFragment extends Fragment {
    private FragmentAccommodationsApprovalScreenBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AccommodationsApprovalScreenFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccommodationsApprovalScreenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccommodationsApprovalScreenFragment newInstance(String param1, String param2) {
        AccommodationsApprovalScreenFragment fragment = new AccommodationsApprovalScreenFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();

        binding.accommodationsContainer.removeAllViews();

        Call<Collection<AccommodationDTO>> call = ClientUtils.accommodationService.getUnapproved();
        call.enqueue(new Callback<Collection<AccommodationDTO>>() {
            @Override
            public void onResponse(@NonNull Call<Collection<AccommodationDTO>> call,
                                   @NonNull Response<Collection<AccommodationDTO>> response) {
                if (response.code() == 200) {
                    assert response.body() != null;

                    response.body().forEach(accommodationDTO -> {
                            AccommodationCardFragment accommodationCardFragment =
                                    AccommodationCardFragment.newInstance(
                                            accommodationDTO.getName(),
                                            accommodationDTO.getDescription(),
                                            Integer.toString(accommodationDTO.getMinimumGuests()),
                                            Integer.toString(accommodationDTO.getMaximumGuests()),
                                            accommodationDTO.getId(),
                                            accommodationDTO.getImages()
                                    );

                        FragmentTransaction fragmentTransaction =
                                getChildFragmentManager().beginTransaction();
                        fragmentTransaction.add(binding.accommodationsContainer.getId(),
                                accommodationCardFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    });

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
            public void onFailure(@NonNull Call<Collection<AccommodationDTO>> call,
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
        // Inflate the layout for this fragment
        binding = FragmentAccommodationsApprovalScreenBinding
                .inflate(inflater, container, false);

        return binding.getRoot();
    }
}