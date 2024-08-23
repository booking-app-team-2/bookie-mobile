package ftn.booking_app_team_2.bookie.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import ftn.booking_app_team_2.bookie.R;
import ftn.booking_app_team_2.bookie.clients.ClientUtils;
import ftn.booking_app_team_2.bookie.clients.ImageService;
import ftn.booking_app_team_2.bookie.databinding.FragmentAccommodationCardBinding;
import ftn.booking_app_team_2.bookie.model.AvailabilityPeriod;
import ftn.booking_app_team_2.bookie.model.Image;
import ftn.booking_app_team_2.bookie.tools.AvailabilityPeriodComparator;
import ftn.booking_app_team_2.bookie.tools.SessionManager;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccommodationCardFragment extends Fragment {
    private FragmentAccommodationCardBinding binding;

    private String userRole;

    private static final String ARG_ACCOMMODATION_NAME = "accommodation_name";
    private static final String ARG_ACCOMMODATION_DESCRIPTION = "accommodation_description";
    private static final String ARG_ACCOMMODATION_MIN_GUESTS = "Minimum guests";
    private static final String ARG_ACCOMMODATION_MAX_GUESTS = "Maximum guests";
    private static final String ARG_ACCOMMODATION_ID="Accommodation ID";
    private static final String ARG_IMAGES = "images";
    private static final String ARG_AVAILABILITY_PERIODS = "availability_periods";

    private String accommodationName;
    private String accommodationDescription;
    private String minGuests;
    private String maxGuests;
    private Long accommodationId;
    private Set<Image> images;
    private Set<AvailabilityPeriod> availabilityPeriods;

    public AccommodationCardFragment() { }

    public static AccommodationCardFragment newInstance(
            String accommodationName, String accommodationDescription, String minGuests,
            String maxGuests, Long accommodationId, Set<Image> images,
            Set<AvailabilityPeriod> availabilityPeriods
    ) {
        AccommodationCardFragment fragment = new AccommodationCardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ACCOMMODATION_NAME, accommodationName);
        args.putString(ARG_ACCOMMODATION_DESCRIPTION, accommodationDescription);
        args.putString(ARG_ACCOMMODATION_MIN_GUESTS, minGuests);
        args.putString(ARG_ACCOMMODATION_MAX_GUESTS, maxGuests);
        args.putLong(ARG_ACCOMMODATION_ID,accommodationId);
        args.putParcelableArrayList(ARG_IMAGES, new ArrayList<>(images));
        args.putParcelableArrayList(ARG_AVAILABILITY_PERIODS, new ArrayList<>(availabilityPeriods));
        fragment.setArguments(args);
        return fragment;
    }

    public static AccommodationCardFragment newInstance(
            String accommodationName, String accommodationDescription, String minGuests,
            String maxGuests, Long accommodationId, Set<Image> images
    ) {
        AccommodationCardFragment fragment = new AccommodationCardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ACCOMMODATION_NAME, accommodationName);
        args.putString(ARG_ACCOMMODATION_DESCRIPTION, accommodationDescription);
        args.putString(ARG_ACCOMMODATION_MIN_GUESTS, minGuests);
        args.putString(ARG_ACCOMMODATION_MAX_GUESTS, maxGuests);
        args.putLong(ARG_ACCOMMODATION_ID,accommodationId);
        args.putParcelableArrayList(ARG_IMAGES, new ArrayList<>(images));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        SessionManager sessionManager = new SessionManager(requireContext());
        userRole = sessionManager.getUserType();
    }

    @Override
    public void onResume() {
        super.onResume();

        getThumbnail();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            accommodationName = getArguments().getString(ARG_ACCOMMODATION_NAME);
            accommodationDescription = getArguments().getString(ARG_ACCOMMODATION_DESCRIPTION);
            minGuests=getArguments().getString(ARG_ACCOMMODATION_MIN_GUESTS);
            maxGuests=getArguments().getString(ARG_ACCOMMODATION_MAX_GUESTS);
            accommodationId=getArguments().getLong(ARG_ACCOMMODATION_ID);
            images = new HashSet<>(
                    Objects.requireNonNull(requireArguments().getParcelableArrayList(ARG_IMAGES))
            );
            if (userRole.equals("Guest"))
                availabilityPeriods = new HashSet<>(
                        Objects.requireNonNull(
                                requireArguments().getParcelableArrayList(ARG_AVAILABILITY_PERIODS)
                        )
                );
        }
    }

    private void displayThumbnail(ResponseBody responseBody) {
        Bitmap bitmap = BitmapFactory.decodeStream(responseBody.byteStream());
        binding.imageId.setImageBitmap(
                Bitmap.createScaledBitmap(
                        bitmap,
                        binding.imageId.getWidth(),
                        binding.imageId.getHeight(),
                        false
                )
        );
    }

    private void getThumbnail() {
        ImageService service = ClientUtils.getImageService(getContext());
        images.stream().findFirst().ifPresent(image -> {
            Call<ResponseBody> call = service.getImage(image.getId());

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call,
                                       @NonNull Response<ResponseBody> response) {
                    if (response.code() == 200) {
                        assert response.body() != null;
                        displayThumbnail(response.body());
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
                public void onFailure(@NonNull Call<ResponseBody> call,
                                      @NonNull Throwable t) {
                    Snackbar.make(
                            requireView(),
                            "Error reaching the server.",
                            Snackbar.LENGTH_SHORT
                    ).show();
                }
            });
        });
    }

    private AvailabilityPeriod getBestDealAvailabilityPeriod() {
        return availabilityPeriods
                .stream()
                .min(new AvailabilityPeriodComparator())
                .orElse(null);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAccommodationCardBinding
                .inflate(inflater, container, false);

        if (userRole.equals("Guest")) {
            AvailabilityPeriod bestDealAvailabilityPeriod = getBestDealAvailabilityPeriod();
            binding.bestPrice.setText(
                    String.format(
                            "Best deal: %s",
                            bestDealAvailabilityPeriod != null ?
                                    bestDealAvailabilityPeriod.getPrice() :
                                    "None"
                    )
            );
        } else
            binding.bestPrice.setVisibility(View.GONE);
        binding.accommodationName.setText(accommodationName);
        binding.accommodationDescription.setText(accommodationDescription);
        binding.accommodationGuestCount.setText(
                String.format("%s to %s guests", minGuests, maxGuests)
        );
        if (userRole.equals("Guest")) {
            binding.ownerInteractionContainer.setVisibility(View.GONE);

            binding.detailsBtn.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putLong("accommodationId", accommodationId);
                Navigation.findNavController(v).navigate(
                        R.id.navigateToAccommodationDetailsScreen, bundle
                );
            });
        } else if (userRole.equals("Owner")) {
            binding.guestInteractionContainer.setVisibility(View.GONE);

            binding.updateAccommodationBtn.setOnClickListener(view -> {
                Bundle bundle = new Bundle();
                bundle.putLong("id", accommodationId);
                Navigation.findNavController(view).navigate(
                        R.id.navigateToEditAccommodationScreen,
                        bundle
                );
            });
        }

        return binding.getRoot();
    }
}
