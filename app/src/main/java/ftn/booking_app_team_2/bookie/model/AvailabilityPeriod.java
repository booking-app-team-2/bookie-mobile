package ftn.booking_app_team_2.bookie.model;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;

public class AvailabilityPeriod implements Parcelable, Serializable {
    @SerializedName("id")
    @Expose
    private final Long id;

    @SerializedName("price")
    @Expose
    private BigDecimal price;

    @SerializedName("period")
    @Expose
    private final PeriodDTO periodDTO;

    @SerializedName("deleted")
    @Expose
    private final boolean isDeleted;

    protected AvailabilityPeriod(Parcel in) {
        id = in.readLong();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            price = in.readSerializable(BigDecimal.class.getClassLoader(), BigDecimal.class);
        }
        periodDTO = in.readParcelable(PeriodDTO.class.getClassLoader());
        isDeleted = in.readByte() != 0;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public PeriodDTO getPeriod() {
        return periodDTO;
    }

    public boolean isDeleted() {
        return isDeleted;
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
        dest.writeByte((byte) (isDeleted ? 1 : 0));
    }

    public static final Creator<AvailabilityPeriod> CREATOR = new Creator<AvailabilityPeriod>() {
        @Override
        public AvailabilityPeriod createFromParcel(Parcel in) {
            return new AvailabilityPeriod(in);
        }

        @Override
        public AvailabilityPeriod[] newArray(int size) {
            return new AvailabilityPeriod[size];
        }
    };
}
