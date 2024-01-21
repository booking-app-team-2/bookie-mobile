package ftn.booking_app_team_2.bookie.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Period implements Parcelable, Serializable {
    @SerializedName("startDate")
    @Expose
    private final String startDate;

    @SerializedName("endDate")
    @Expose
    private final String endDate;

    protected Period(Parcel in) {
        startDate = in.readString();
        endDate = in.readString();
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(startDate);
        dest.writeString(endDate);
    }

    public static final Creator<Period> CREATOR = new Creator<Period>() {
        @Override
        public Period createFromParcel(Parcel in) {
            return new Period(in);
        }

        @Override
        public Period[] newArray(int size) {
            return new Period[size];
        }
    };
}
