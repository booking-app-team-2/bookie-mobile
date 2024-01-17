package ftn.booking_app_team_2.bookie.model;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;

public class ReservationOwner implements Parcelable, Serializable {
    @SerializedName("id")
    @Expose
    private final Long id;


    @SerializedName("numberOfGuests")
    @Expose
    private final int numberOfGuests;

    @SerializedName("status")
    @Expose
    private final ReservationStatus status;

    @SerializedName("accommodationNameDTO")
    @Expose
    private final AccommodationNameDTO accommodationNameDTO;

    @SerializedName("reserveeBasicInfoDTO")
    @Expose
    private final ReserveeBasicInfoDTO reserveeBasicInfoDTO;

    @SerializedName("periodDTO")
    @Expose
    private final PeriodDTO periodDTO;

    @SerializedName("price")
    @Expose
    private BigDecimal price;

    protected ReservationOwner(Parcel in) {
        id = in.readLong();
        numberOfGuests = in.readInt();
        status = ReservationStatus.valueOf(in.readString());
        accommodationNameDTO = in.readParcelable(AccommodationNameDTO.class.getClassLoader());
        reserveeBasicInfoDTO = in.readParcelable(ReserveeBasicInfoDTO.class.getClassLoader());
        periodDTO = in.readParcelable(PeriodDTO.class.getClassLoader());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            price = in.readSerializable(BigDecimal.class.getClassLoader(), BigDecimal.class);
        }
    }

    public Long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public AccommodationNameDTO getAccommodationNameDTO() {
        return accommodationNameDTO;
    }

    public ReserveeBasicInfoDTO getReserveeBasicInfoDTO() {
        return reserveeBasicInfoDTO;
    }

    public PeriodDTO getPeriodDTO() {
        return periodDTO;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeInt(numberOfGuests);
        dest.writeSerializable(status);
        dest.writeParcelable(accommodationNameDTO, flags);
        dest.writeParcelable(reserveeBasicInfoDTO, flags);
        dest.writeParcelable(periodDTO, flags);
        dest.writeSerializable(price);
    }

    public static final Creator<ReservationOwner> CREATOR = new Creator<ReservationOwner>() {
        @Override
        public ReservationOwner createFromParcel(Parcel in) {
            return new ReservationOwner(in);
        }

        @Override
        public ReservationOwner[] newArray(int size) {
            return new ReservationOwner[size];
        }
    };
}
