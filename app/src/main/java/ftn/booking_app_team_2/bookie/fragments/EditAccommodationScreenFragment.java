package ftn.booking_app_team_2.bookie.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.NumberPicker;

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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import ftn.booking_app_team_2.bookie.R;
import ftn.booking_app_team_2.bookie.adapters.ImageAdapter;
import ftn.booking_app_team_2.bookie.clients.ClientUtils;
import ftn.booking_app_team_2.bookie.databinding.FragmentEditAccommodationScreenBinding;
import ftn.booking_app_team_2.bookie.model.AccommodationAutoAccept;
import ftn.booking_app_team_2.bookie.model.AccommodationBasicInfo;
import ftn.booking_app_team_2.bookie.model.AccommodationDTO;
import ftn.booking_app_team_2.bookie.model.AccommodationType;
import ftn.booking_app_team_2.bookie.model.Amenities;
import ftn.booking_app_team_2.bookie.model.AvailabilityPeriodDTO;
import ftn.booking_app_team_2.bookie.model.Location;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditAccommodationScreenFragment extends Fragment {
    private FragmentEditAccommodationScreenBinding binding;

    private AccommodationDTO accommodation = null;

    private final ArrayList<byte[]> images = new ArrayList<>();
    private final ArrayList<Long> imageIds = new ArrayList<>();

    private RecyclerView carousel;
    private NumberPicker imageNumber;
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

    private ActivityResultLauncher<Intent> activityResultLauncher;

    public EditAccommodationScreenFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getLong(ARG_ID);
        }
    }

    private void addImageToView(byte[] image, Long id) {
        images.add(image);
        imageIds.add(id);

        ImageAdapter imageAdapter = new ImageAdapter(requireContext(), images);
        carousel.setAdapter(imageAdapter);

        imageNumber.setMinValue(0);

        int imageCount = Objects.requireNonNull(carousel.getAdapter()).getItemCount() - 1;
        if (imageCount == 0) {
            binding.carousel.setVisibility(View.VISIBLE);
            binding.imageRemovalHolder.setVisibility(View.VISIBLE);
        } else
            imageNumber.setMaxValue(imageCount);
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
                            byte[] rawImage = response.body().bytes();

                            addImageToView(rawImage, image.getId());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
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

    private void addMarkerToMap(GeoPoint geoPoint) {
        Marker locationMarker = new Marker(location);
        locationMarker.setPosition(geoPoint);
        locationMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);
        locationMarker.setTitle(accommodation.getName());
        location.getOverlays().add(locationMarker);
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

                    if (accommodation.getImages().isEmpty()) {
                        binding.carousel.setVisibility(View.GONE);
                        binding.imageRemovalHolder.setVisibility(View.GONE);
                    }
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

    private void updateAccommodation() {
        List<Float> numberOfGuestsValues = numberOfGuests.getValues();

        Call<AccommodationBasicInfo> call = ClientUtils
                .accommodationService
                .putAccommodationBasicInfo(
                        id,
                        new AccommodationBasicInfo(
                                id,
                                Objects.requireNonNull(name.getText()).toString(),
                                Objects.requireNonNull(description.getText()).toString(),
                                numberOfGuestsValues.get(0).intValue(),
                                numberOfGuestsValues.get(1).intValue(),
                                getCurrentLocation(),
                                getCurrentAmenities(),
                                accommodation.getImages(),
                                AccommodationType.valueOf(accommodationType.getText().toString()),
                                accommodation
                                        .getAvailabilityPeriods()
                                        .stream()
                                        .map(AvailabilityPeriodDTO::new)
                                        .collect(Collectors.toSet())
                        )
                );

        call.enqueue(new Callback<AccommodationBasicInfo>() {
            @Override
            public void onResponse(@NonNull Call<AccommodationBasicInfo> call,
                                   @NonNull Response<AccommodationBasicInfo> response) {
                if (response.code() == 200) {
                    Snackbar
                            .make(
                                    requireView(),
                                    "Accommodation successfully updated.",
                                    Snackbar.LENGTH_LONG
                            )
                            .show();
                } else {
                    assert response.errorBody() != null;
                    try {
                        Snackbar
                                .make(
                                        requireView(),
                                        "Accommodation has reservations in the period you " +
                                                "are trying to update.",
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
            public void onFailure(@NonNull Call<AccommodationBasicInfo> call,
                                  @NonNull Throwable t) {
                Snackbar.make(
                        requireView(),
                        "Error reaching the server.",
                        Snackbar.LENGTH_SHORT
                ).show();
            }
        });
    }

    private void updateIsReservationAutoAccepted() {
        Call<AccommodationAutoAccept> call =
                ClientUtils.accommodationService.putIsReservationAutoAccepted(
                        accommodation.getId(),
                        new AccommodationAutoAccept(isReservationAutoAccepted.isChecked())
                );

        call.enqueue(new Callback<AccommodationAutoAccept>() {
            @Override
            public void onResponse(@NonNull Call<AccommodationAutoAccept> call,
                                   @NonNull Response<AccommodationAutoAccept> response) {
                if (response.code() == 200) {
                    updateAccommodation();
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
            public void onFailure(@NonNull Call<AccommodationAutoAccept> call,
                                  @NonNull Throwable t) {
                Snackbar.make(
                        requireView(),
                        "Error reaching the server.",
                        Snackbar.LENGTH_SHORT
                ).show();
            }
        });
    }

    private void registerResult() {
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                activityResult -> {
                    try {
                        assert activityResult.getData() != null;
                        addImage(Objects.requireNonNull(activityResult.getData().getData()).toString());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }

    private void getImageFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activityResultLauncher.launch(galleryIntent);
    }

    private void addImage(String imageUri) {
        File file = new File(imageUri);
        RequestBody requestBody = RequestBody.create(MultipartBody.FORM, file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("image", file.getName(), requestBody);

        Call<ResponseBody> call = ClientUtils.imageService.postImage(body, accommodation.getId());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call,
                                   @NonNull Response<ResponseBody> response) {
                if (response.code() == 200) {
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
    }

    private void removeImageFromView() {
        images.remove(images.get(imageNumber.getValue()));
        imageIds.remove(imageIds.get(imageNumber.getValue()));

        ImageAdapter imageAdapter = new ImageAdapter(requireContext(), images);
        carousel.setAdapter(imageAdapter);

        imageNumber.setMinValue(0);

        int imageCount = Objects.requireNonNull(carousel.getAdapter()).getItemCount() - 1;
        if (imageCount == -1) {
            binding.carousel.setVisibility(View.GONE);
            binding.imageRemovalHolder.setVisibility(View.GONE);
        }
        else
            imageNumber.setMaxValue(imageCount);
    }

    private void removeImage() {
        Call<ResponseBody> call =
                ClientUtils.imageService.deleteImage(imageIds.get(imageNumber.getValue()));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call,
                                   @NonNull Response<ResponseBody> response) {
                if (response.code() == 200) {
                    removeImageFromView();
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
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditAccommodationScreenBinding.inflate(
                inflater, container, false
        );

        carousel = binding.carousel;
        imageNumber = binding.imageNumberPicker;
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

        registerResult();
        binding.addImageBtn.setOnClickListener(view -> getImageFromGallery());

        binding.removeImageBtn.setOnClickListener(view -> removeImage());

        binding.updateAccommodationBtn.setOnClickListener(view ->
                updateIsReservationAutoAccepted());

        return binding.getRoot();
    }
}