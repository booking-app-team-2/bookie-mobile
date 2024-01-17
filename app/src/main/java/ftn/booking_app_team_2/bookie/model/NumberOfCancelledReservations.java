package ftn.booking_app_team_2.bookie.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NumberOfCancelledReservations implements Parcelable, Serializable {
    @SerializedName("numberOfCancelledReservations")
    @Expose
    private int numberOfCancelledReservations;


    protected NumberOfCancelledReservations(Parcel in) {
        numberOfCancelledReservations = in.readInt();
    }

    public int getNumberOfCancelledReservations() {
        return numberOfCancelledReservations;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(numberOfCancelledReservations);
    }

    public static final Creator<NumberOfCancelledReservations> CREATOR =
            new Creator<NumberOfCancelledReservations>() {
                @Override
                public NumberOfCancelledReservations createFromParcel(Parcel in) {
                    return new NumberOfCancelledReservations(in);
                }

                @Override
                public NumberOfCancelledReservations[] newArray(int size) {
                    return new NumberOfCancelledReservations[size];
                }
    };
}
