package ftn.booking_app_team_2.bookie.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ReserveeBasicInfoDTO implements Parcelable, Serializable {
    @SerializedName("id")
    @Expose
    private final Long id;

    @SerializedName("name")
    @Expose
    private final String name;

    @SerializedName("surname")
    @Expose
    private final String surname;

    protected ReserveeBasicInfoDTO(Parcel in) {
        id = in.readLong();
        name = in.readString();
        surname = in.readString();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(surname);
    }

    public static final Creator<ReserveeBasicInfoDTO> CREATOR =
            new Creator<ReserveeBasicInfoDTO>() {
                @Override
                public ReserveeBasicInfoDTO createFromParcel(Parcel in) {
                    return new ReserveeBasicInfoDTO(in);
                }

                @Override
                public ReserveeBasicInfoDTO[] newArray(int size) {
                    return new ReserveeBasicInfoDTO[size];
                }
    };
}
