package ftn.booking_app_team_2.bookie.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;

import com.google.android.material.materialswitch.MaterialSwitch;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import ftn.booking_app_team_2.bookie.R;
import ftn.booking_app_team_2.bookie.adapters.ImageAdapter;
import ftn.booking_app_team_2.bookie.clients.ClientUtils;
import ftn.booking_app_team_2.bookie.databinding.FragmentEditAccommodationScreenBinding;
import ftn.booking_app_team_2.bookie.model.AccommodationDTO;
import ftn.booking_app_team_2.bookie.model.AccommodationType;
import ftn.booking_app_team_2.bookie.model.Location;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditAccommodationScreenFragment extends Fragment {
    private FragmentEditAccommodationScreenBinding binding;

    private AccommodationDTO accommodation = null;
    private final ArrayList<byte[]> images = new ArrayList<>();

    private TextInputEditText name;
    private TextInputEditText description;
    private RangeSlider numberOfGuests;
    private MapView location;
    private MaterialSwitch isReservationAutoAccepted;
    private CheckBox wiFi;
    private CheckBox kitchen;
    private CheckBox aC;
    private CheckBox parking;
    private AutoCompleteTextView accommodationType;

    private static final String ARG_ID = "id";

    private Long id;

    public EditAccommodationScreenFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getLong(ARG_ID);
        }
    }

    private void displayImages() {
        ImageAdapter imageAdapter = new ImageAdapter(requireContext(), images);
        binding.carousel.setAdapter(imageAdapter);
    }

    private void getImages() {
        accommodation.getImages().forEach(image -> {
            Call<ResponseBody> call = ClientUtils.imageService.getImage(image.getId());

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call,
                                       @NonNull Response<ResponseBody> response) {
                    if (response.code() == 200) {
                        assert response.body() != null;
                        try {
                            images.add(response.body().bytes());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        displayImages();
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
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    Snackbar.make(
                            requireView(),
                            "Error reaching the server.",
                            Snackbar.LENGTH_SHORT
                    ).show();
                }
            });
        });
    }

    private void displayAmenities() {
        Set<String> amenities = accommodation
                .getAmenities()
                .stream()
                .map(Enum::toString)
                .collect(Collectors.toSet());

        if (amenities.contains(wiFi.getText().toString()))
            wiFi.setChecked(true);
        if (amenities.contains(kitchen.getText().toString()))
            kitchen.setChecked(true);
        if (amenities.contains(aC.getText().toString()))
            aC.setChecked(true);
        if (amenities.contains(parking.getText().toString()))
            parking.setChecked(true);
    }

    private void addMarkerToMap(GeoPoint geoPoint) {
        Marker locationMarker = new Marker(location);
        locationMarker.setPosition(geoPoint);
        locationMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
        locationMarker.setTitle(accommodation.getName());
        location.getOverlays().add(locationMarker);
        System.out.println(getCurrentLocation().getLatitude() + " " + getCurrentLocation().getLongitude());
    }

    public void displayLocation() {
        IMapController mapController = location.getController();
        mapController.setZoom(15F);
        GeoPoint locationPoint = new GeoPoint(
                accommodation.getLocation().getLatitude(),
                accommodation.getLocation().getLongitude()
        );
        mapController.setCenter(locationPoint);
        addMarkerToMap(locationPoint);
    }

    private void displayBasicInfo() {
        name.setText(accommodation.getName());
        description.setText(accommodation.getDescription());
        numberOfGuests.setValues(
                (float) accommodation.getMinimumGuests(),
                (float) accommodation.getMaximumGuests()
        );
        displayLocation();
        displayAmenities();
        accommodationType.setText(accommodation.getType().toString(), false);
        isReservationAutoAccepted.setChecked(accommodation.isReservationAutoAccepted());
    }

    private void getAccommodation() {
        Call<AccommodationDTO> call = ClientUtils.accommodationService.getAccommodation(id);

        call.enqueue(new Callback<AccommodationDTO>() {
            @Override
            public void onResponse(@NonNull Call<AccommodationDTO> call,
                                   @NonNull Response<AccommodationDTO> response) {
                if (response.code() == 200) {
                    assert response.body() != null;
                    accommodation = response.body();

                    if (accommodation.getImages().isEmpty())
                        binding.carousel.setVisibility(View.GONE);
                    else
                        getImages();

                    displayBasicInfo();
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
            public void onFailure(@NonNull Call<AccommodationDTO> call, @NonNull Throwable t) {
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

        if (accommodation != null)
            return;

        getAccommodation();
        location.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

        location.onPause();
    }

    private void removeAllMarkersFromMap() {
        location.getOverlays().forEach(overlay -> {
            if (overlay instanceof Marker)
                location.getOverlays().remove(overlay);
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupMap() {
        location.setMultiTouchControls(true);
        location.setTileSource(TileSourceFactory.OpenTopo);

        location.setOnTouchListener((View.OnTouchListener) (v, event) -> {
            NestedScrollView predecessorScrollView = binding.predecessorScrollView;
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    predecessorScrollView.requestDisallowInterceptTouchEvent(true);
                    return false;
                case MotionEvent.ACTION_UP:
                    predecessorScrollView.requestDisallowInterceptTouchEvent(false);
                    return false;
                default:
                    return true;
            }
        });

        MapEventsReceiver mapEventsReceiver = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint geoPoint) {
                removeAllMarkersFromMap();
                addMarkerToMap(geoPoint);
                location.invalidate();
                return false;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        };

        location.getOverlays().add(new MapEventsOverlay(mapEventsReceiver));
    }

    private Location getCurrentLocation() {
        GeoPoint currentLocationMarkerGeoPoint = ((Marker) Objects
                .requireNonNull(location
                        .getOverlays()
                        .stream()
                        .filter(overlay -> overlay instanceof Marker)
                        .findFirst()
                        .orElse(null)))
                .getPosition();

        return new Location(
                currentLocationMarkerGeoPoint.getLatitude(),
                currentLocationMarkerGeoPoint.getLongitude()
        );
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditAccommodationScreenBinding.inflate(
                inflater, container, false
        );

        name = binding.nameTextInput;
        description = binding.descriptionTextInput;
        numberOfGuests = binding.numberOfGuestsSlider;
        numberOfGuests.setLabelFormatter(value ->
                String.format(value == 1 ? "%s person" : "%s people", (int) value));
        location = binding.locationMapView;
        setupMap();
        wiFi = binding.wiFiCheckBox;
        kitchen = binding.kitchenCheckBox;
        aC = binding.acCheckBox;
        parking = binding.parkingCheckBox;
        accommodationType = binding.accommodationTypeAutoCompleteTextView;
        accommodationType.setAdapter(
                new ArrayAdapter<>(
                        requireContext(),
                        R.layout.drop_down_item,
                        AccommodationType.values())
        );
        isReservationAutoAccepted = binding.isReservationAutoAcceptedSwitch;
        isReservationAutoAccepted.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked)
                isReservationAutoAccepted.setThumbIconResource(R.drawable.round_check);
            else
                isReservationAutoAccepted.setThumbIconResource(R.drawable.round_close);
        });

        return binding.getRoot();
    }
}