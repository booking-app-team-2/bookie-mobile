package ftn.booking_app_team_2.bookie.model;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class AccommodationBasicInfo implements Parcelable, Serializable {
    @SerializedName("id")
    @Expose
    private final Long id;

    @SerializedName("name")
    @Expose
    private final String name;

    @SerializedName("description")
    @Expose
    private final String description;

    @SerializedName("minimumGuests")
    @Expose
    private final int minimumGuests;

    @SerializedName("maximumGuests")
    @Expose
    private final int maximumGuests;

    @SerializedName("location")
    @Expose
    private final Location location;

    @SerializedName("amenities")
    @Expose
    private Set<Amenities> amenities;

    @SerializedName("images")
    @Expose
    private Set<Image> images;

    @SerializedName("type")
    @Expose
    private final AccommodationType type;

    @SerializedName("availabilityPeriods")
    @Expose
    private Set<AvailabilityPeriodDTO> availabilityPeriodDTOs;

    public AccommodationBasicInfo(Long id, String name, String description, int minimumGuests,
                                  int maximumGuests, Location location, Set<Amenities> amenities,
                                  Set<Image> images, AccommodationType type,
                                  Set<AvailabilityPeriodDTO> availabilityPeriodDTOs) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.minimumGuests = minimumGuests;
        this.maximumGuests = maximumGuests;
        this.location = location;
        this.amenities = amenities;
        this.images = images;
        this.type = type;
        this.availabilityPeriodDTOs = availabilityPeriodDTOs;
    }

    protected AccommodationBasicInfo(Parcel in) {
        id = in.readLong();
        name = in.readString();
        description = in.readString();
        minimumGuests = in.readInt();
        maximumGuests = in.readInt();
        location = in.readParcelable(Location.class.getClassLoader());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            amenities = new HashSet<>(
                    Objects.requireNonNull(
                            in.readArrayList(Amenities.class.getClassLoader(), Amenities.class)
                    )
            );
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            availabilityPeriodDTOs = new HashSet<>(
                    Objects.requireNonNull(
                            in.readArrayList(
                                    AvailabilityPeriodDTO.class.getClassLoader(),
                                    AvailabilityPeriodDTO.class
                            )
                    )
            );
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            images = new HashSet<>(
                    Objects.requireNonNull(
                            in.readArrayList(Image.class.getClassLoader(), Image.class)
                    )
            );
        }
        type = AccommodationType.valueOf(in.readString());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public AccommodationType getType() {
        return type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeInt(minimumGuests);
        dest.writeInt(maximumGuests);
        dest.writeParcelable(location, flags);
        dest.writeArray(amenities.toArray());
        dest.writeArray(availabilityPeriodDTOs.toArray());
        dest.writeArray(images.toArray());
        dest.writeSerializable(type);
    }

    public static final Creator<AccommodationBasicInfo> CREATOR =
            new Creator<AccommodationBasicInfo>() {
                @Override
                public AccommodationBasicInfo createFromParcel(Parcel in) {
                    return new AccommodationBasicInfo(in);
                }

                @Override
                public AccommodationBasicInfo[] newArray(int size) {
                    return new AccommodationBasicInfo[size];
                }
    };
}
