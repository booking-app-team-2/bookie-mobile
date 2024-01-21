package ftn.booking_app_team_2.bookie.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AccommodationAutoAccept implements Parcelable, Serializable {
    @SerializedName("reservationAutoAccepted")
    @Expose
    private final boolean reservationAutoAccepted;

    public AccommodationAutoAccept(boolean reservationAutoAccepted) {
        this.reservationAutoAccepted = reservationAutoAccepted;
    }

    protected AccommodationAutoAccept(Parcel in) {
        reservationAutoAccepted = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (reservationAutoAccepted ? 1 : 0));
    }

    public static final Creator<AccommodationAutoAccept> CREATOR =
            new Creator<AccommodationAutoAccept>() {
                @Override
                public AccommodationAutoAccept createFromParcel(Parcel in) {
                    return new AccommodationAutoAccept(in);
                }

                @Override
                public AccommodationAutoAccept[] newArray(int size) {
                    return new AccommodationAutoAccept[size];
                }
    };
}
