package ftn.booking_app_team_2.bookie.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AccommodationNameDTO implements Parcelable, Serializable {
    @SerializedName("name")
    @Expose
    private final String name;

    protected AccommodationNameDTO(Parcel in) {
        name = in.readString();
    }

    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }

    public static final Creator<AccommodationNameDTO> CREATOR =
            new Creator<AccommodationNameDTO>() {
                @Override
                public AccommodationNameDTO createFromParcel(Parcel in) {
                    return new AccommodationNameDTO(in);
                }

                @Override
                public AccommodationNameDTO[] newArray(int size) {
                    return new AccommodationNameDTO[size];
                }
    };
}
