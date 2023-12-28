package ftn.booking_app_team_2.bookie.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserBasicInfo implements Parcelable, Serializable {
    @SerializedName("name")
    @Expose
    private final String name;

    @SerializedName("surname")
    @Expose
    private final String surname;

    public UserBasicInfo(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    protected UserBasicInfo(Parcel in) {
        name = in.readString();
        surname = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(surname);
    }

    public static final Creator<UserBasicInfo> CREATOR = new Creator<UserBasicInfo>() {
        @Override
        public UserBasicInfo createFromParcel(Parcel in) {
            return new UserBasicInfo(in);
        }

        @Override
        public UserBasicInfo[] newArray(int size) {
            return new UserBasicInfo[size];
        }
    };
}
