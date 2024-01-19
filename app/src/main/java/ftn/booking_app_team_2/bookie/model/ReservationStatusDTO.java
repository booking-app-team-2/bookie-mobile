package ftn.booking_app_team_2.bookie.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ReservationStatusDTO implements Parcelable, Serializable {
    @SerializedName("status")
    @Expose
    private final ReservationStatus status;

    protected ReservationStatusDTO(Parcel in) {
        status = ReservationStatus.valueOf(in.readString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(status);
    }

    public static final Creator<ReservationStatusDTO> CREATOR =
            new Creator<ReservationStatusDTO>() {
                @Override
                public ReservationStatusDTO createFromParcel(Parcel in) {
                    return new ReservationStatusDTO(in);
                }

                @Override
                public ReservationStatusDTO[] newArray(int size) {
                    return new ReservationStatusDTO[size];
                }
    };
}
