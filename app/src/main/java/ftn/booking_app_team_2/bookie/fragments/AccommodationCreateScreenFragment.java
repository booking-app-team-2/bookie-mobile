package ftn.booking_app_team_2.bookie.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.slider.RangeSlider;
import com.google.android.material.snackbar.Snackbar;

import org.osmdroid.api.IMapController;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import ftn.booking_app_team_2.bookie.R;
import ftn.booking_app_team_2.bookie.clients.AccommodationService;
import ftn.booking_app_team_2.bookie.clients.ClientUtils;
import ftn.booking_app_team_2.bookie.databinding.FragmentCreateAccommodationScreenBinding;
import ftn.booking_app_team_2.bookie.model.AccommodationDTO;
import ftn.booking_app_team_2.bookie.model.AccommodationType;
import ftn.booking_app_team_2.bookie.model.Amenities;
import ftn.booking_app_team_2.bookie.model.Location;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccommodationCreateScreenFragment extends Fragment {

    private RangeSlider numberOfGuests;
    private MapView location;
    private FragmentCreateAccommodationScreenBinding binding;
    private CheckBox wiFi;
    private CheckBox kitchen;
    private CheckBox aC;
    private CheckBox parking;
    private AutoCompleteTextView accommodationType;

    private AccommodationDTO accommodation = null;

    private final ArrayList<byte[]> images = new ArrayList<>();
    private final ArrayList<Long> imageIds = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
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

    private Set<Amenities> getCurrentAmenities() {
        Set<Amenities> amenities = new HashSet<>();

        if (wiFi.isChecked())
            amenities.add(Amenities.valueOf(wiFi.getText().toString()));
        if (kitchen.isChecked())
            amenities.add(Amenities.valueOf(kitchen.getText().toString()));
        if (aC.isChecked())
            amenities.add(Amenities.valueOf(aC.getText().toString()));
        if (parking.isChecked())
            amenities.add(Amenities.valueOf(parking.getText().toString()));

        return amenities;
    }

    private void removeAllMarkersFromMap() {
        location.getOverlays().forEach(overlay -> {
            if (overlay instanceof Marker)
                location.getOverlays().remove(overlay);
        });
    }

    private void addMarkerToMap(GeoPoint geoPoint) {
        Marker locationMarker = new Marker(location);
        locationMarker.setPosition(geoPoint);
        locationMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
        locationMarker.setTitle("Location");
        location.getOverlays().add(locationMarker);
    }

    public void displayLocation() {
        IMapController mapController = location.getController();
        mapController.setZoom(15F);
        GeoPoint locationPoint = new GeoPoint(
                45.24611466641891,
                19.851694566067337
        );
        mapController.setCenter(locationPoint);
        addMarkerToMap(locationPoint);
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

    private void createAccommodation() {
        AccommodationService service = ClientUtils.getAccommodationService(getContext());
        List<Float> numberOfGuestsValues = numberOfGuests.getValues();

        Long id = null;
        String name = binding.nameTextInput.getText().toString();
        String description = binding.descriptionTextInput.getText().toString();
        int minimumGuests = numberOfGuestsValues.get(0).intValue();
        int maximumGuests = numberOfGuestsValues.get(1).intValue();
        boolean autoAccept = binding.isReservationAutoAcceptedSwitch.isChecked();
        Call<AccommodationDTO> call = service
                .createAccommodation(
                        new AccommodationDTO(
                                id,
                                name,
                                description,
                                minimumGuests,
                                maximumGuests,
                                getCurrentLocation(),
                                getCurrentAmenities(),
                                AccommodationType.valueOf(binding.accommodationTypeAutoCompleteTextView.getText().toString()),
                                autoAccept
                        )
                );

        call.enqueue(new Callback<AccommodationDTO>() {
            @Override
            public void onResponse(@NonNull Call<AccommodationDTO> call,
                                   @NonNull Response<AccommodationDTO> response) {
                if (response.code() == 201) {
                    Snackbar
                            .make(
                                    requireView(),
                                    "Accommodation successfully created.",
                                    Snackbar.LENGTH_LONG
                            )
                            .show();
                } else {
                    assert response.errorBody() != null;
                    try {
                        Snackbar
                                .make(
                                        requireView(),
                                        "Error!",
                                        Snackbar.LENGTH_SHORT
                                )
                                .show();
                    } catch (Exception ex) {
                        Log.d(
                                "Bookie",
                                ex.getMessage() != null ? ex.getMessage() : "Unknown error"
                        );
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<AccommodationDTO> call,
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreateAccommodationScreenBinding.inflate(
                inflater, container, false
        );
        location = binding.locationMapView;
        setupMap();
        displayLocation();
        numberOfGuests = binding.numberOfGuestsSlider;
        numberOfGuests.setValues(
                (float) 1,
                (float) 2
        );
        numberOfGuests = binding.numberOfGuestsSlider;
        numberOfGuests.setLabelFormatter(value ->
                String.format(value == 1 ? "%s person" : "%s people", (int) value));
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
        binding.createAccommodationBtn.setOnClickListener(view -> {
                    createAccommodation();
                });

        return binding.getRoot();
    }
}

