package ftn.booking_app_team_2.bookie.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AccommodationApproval implements Parcelable, Serializable {
    @SerializedName("approved")
    @Expose
    private final boolean isApproved;

    public AccommodationApproval(boolean isApproved) {
        this.isApproved = isApproved;
    }

    protected AccommodationApproval(Parcel in) {
        isApproved = in.readByte() != 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isApproved ? 1 : 0));
    }

    public static final Creator<AccommodationApproval> CREATOR =
            new Creator<AccommodationApproval>() {
        @Override
        public AccommodationApproval createFromParcel(Parcel in) {
            return new AccommodationApproval(in);
        }

        @Override
        public AccommodationApproval[] newArray(int size) {
            return new AccommodationApproval[size];
        }
    };
}
