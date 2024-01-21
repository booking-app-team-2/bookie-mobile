package ftn.booking_app_team_2.bookie.model;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;

public class AvailabilityPeriodDTO implements Parcelable, Serializable {
    @SerializedName("id")
    @Expose
    private final Long id;

    @SerializedName("price")
    @Expose
    private BigDecimal price;

    @SerializedName("period")
    @Expose
    private final PeriodDTO periodDTO;

    public AvailabilityPeriodDTO(AvailabilityPeriod availabilityPeriod) {
        id = availabilityPeriod.getId();
        price = availabilityPeriod.getPrice();
        periodDTO = new PeriodDTO(availabilityPeriod.getPeriod());
    }

    protected AvailabilityPeriodDTO(Parcel in) {
        id = in.readLong();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            price = in.readSerializable(BigDecimal.class.getClassLoader(), BigDecimal.class);
        }
        periodDTO = in.readParcelable(PeriodDTO.class.getClassLoader());
    }

    public Long getId() {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeSerializable(price);
        dest.writeParcelable(periodDTO, flags);
    }

    public static final Creator<AvailabilityPeriodDTO> CREATOR =
            new Creator<AvailabilityPeriodDTO>() {
                @Override
                public AvailabilityPeriodDTO createFromParcel(Parcel in) {
                    return new AvailabilityPeriodDTO(in);
                }

                @Override
                public AvailabilityPeriodDTO[] newArray(int size) {
                    return new AvailabilityPeriodDTO[size];
                }
    };
}
