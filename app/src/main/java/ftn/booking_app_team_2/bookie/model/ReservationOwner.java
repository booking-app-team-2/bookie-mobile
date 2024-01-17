package ftn.booking_app_team_2.bookie.model;

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
    private Long id;


    @SerializedName("numberOfGuests")
    @Expose
    private int numberOfGuests;

    @SerializedName("status")
    @Expose
    private ReservationStatus status;

    @SerializedName("accommodationNameDTO")
    @Expose
    private AccommodationNameDTO accommodationNameDTO;

    @SerializedName("reserveeBasicInfoDTO")
    @Expose
    private ReserveeBasicInfoDTO reserveeBasicInfoDTO;

    @SerializedName("periodDTO")
    @Expose
    private PeriodDTO periodDTO;

    @SerializedName("price")
    @Expose
    private BigDecimal price;

    public ReservationOwner(Long id, int numberOfGuests, ReservationStatus status,
                            AccommodationNameDTO accommodationNameDTO,
                            ReserveeBasicInfoDTO reserveeBasicInfoDTO, PeriodDTO periodDTO,
                            BigDecimal price) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.status = status;
        this.accommodationNameDTO = accommodationNameDTO;
        this.reserveeBasicInfoDTO = reserveeBasicInfoDTO;
        this.periodDTO = periodDTO;
        this.price = price;
    }

    protected ReservationOwner(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        numberOfGuests = in.readInt();
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
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeInt(numberOfGuests);
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
