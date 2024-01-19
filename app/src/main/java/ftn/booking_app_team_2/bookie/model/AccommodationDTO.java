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

public class AccommodationDTO implements Parcelable, Serializable {
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

    @SerializedName("availabilityPeriods")
    @Expose
    private Set<AvailabilityPeriod> availabilityPeriods;

    @SerializedName("images")
    @Expose
    private Set<Image> images;

    @SerializedName("reservationCancellationDeadline")
    @Expose
    private final int reservationCancellationDeadline;

    @SerializedName("type")
    @Expose
    private final AccommodationType type;

    @SerializedName("reservationAutoAccepted")
    @Expose
    private final boolean isReservationAutoAccepted;

    protected AccommodationDTO(Parcel in) {
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
            availabilityPeriods = new HashSet<>(
                    Objects.requireNonNull(
                            in.readArrayList(
                                    AvailabilityPeriod.class.getClassLoader(),
                                    AvailabilityPeriod.class
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
        reservationCancellationDeadline = in.readInt();
        type = AccommodationType.valueOf(in.readString());
        isReservationAutoAccepted = in.readByte() != 0;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getMinimumGuests() {
        return minimumGuests;
    }

    public int getMaximumGuests() {
        return maximumGuests;
    }

    public Location getLocation() {
        return location;
    }

    public Set<Amenities> getAmenities() {
        return amenities;
    }

    public Set<AvailabilityPeriod> getAvailabilityPeriods() {
        return availabilityPeriods;
    }

    public Set<Image> getImages() {
        return images;
    }

    public int getReservationCancellationDeadline() {
        return reservationCancellationDeadline;
    }

    public AccommodationType getType() {
        return type;
    }

    public boolean isReservationAutoAccepted() {
        return isReservationAutoAccepted;
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
        dest.writeArray(availabilityPeriods.toArray());
        dest.writeArray(images.toArray());
        dest.writeInt(reservationCancellationDeadline);
        dest.writeSerializable(type);
        dest.writeByte((byte) (isReservationAutoAccepted ? 1 : 0));
    }

    public static final Creator<AccommodationDTO> CREATOR = new Creator<AccommodationDTO>() {
        @Override
        public AccommodationDTO createFromParcel(Parcel in) {
            return new AccommodationDTO(in);
        }

        @Override
        public AccommodationDTO[] newArray(int size) {
            return new AccommodationDTO[size];
        }
    };


    @NonNull
    @Override
    public String toString() {
        return "AccommodationDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", minimumGuests=" + minimumGuests +
                ", maximumGuests=" + maximumGuests +
                ", location=" + location +
                ", amenities=" + amenities +
                ", availabilityPeriods=" + availabilityPeriods +
                ", images=" + images +
                ", reservationCancellationDeadline=" + reservationCancellationDeadline +
                ", type=" + type +
                ", isReservationAutoAccepted=" + isReservationAutoAccepted +
                '}';
    }
}
