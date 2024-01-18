package ftn.booking_app_team_2.bookie.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PeriodDTO implements Parcelable, Serializable {
    @SerializedName("startTimestamp")
    @Expose
    private final long startTimestamp;

    @SerializedName("endTimestamp")
    @Expose
    private final long endTimestamp;

    protected PeriodDTO(Parcel in) {
        startTimestamp = in.readLong();
        endTimestamp = in.readLong();
    }

    public long getStartTimestamp() {
        return startTimestamp;
    }

    public long getEndTimestamp() {
        return endTimestamp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(startTimestamp);
        dest.writeLong(endTimestamp);
    }

    public static final Creator<PeriodDTO> CREATOR = new Creator<PeriodDTO>() {
        @Override
        public PeriodDTO createFromParcel(Parcel in) {
            return new PeriodDTO(in);
        }

        @Override
        public PeriodDTO[] newArray(int size) {
            return new PeriodDTO[size];
        }
    };
}
