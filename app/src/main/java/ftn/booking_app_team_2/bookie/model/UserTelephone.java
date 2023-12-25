package ftn.booking_app_team_2.bookie.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserTelephone implements Parcelable, Serializable {
    @SerializedName("telephone")
    @Expose
    private final String telephone;

    public UserTelephone(String telephone) {
        this.telephone = telephone;
    }

    protected UserTelephone(Parcel in) {
        telephone = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(telephone);
    }

    public static final Creator<UserTelephone> CREATOR = new Creator<UserTelephone>() {
        @Override
        public UserTelephone createFromParcel(Parcel in) {
            return new UserTelephone(in);
        }

        @Override
        public UserTelephone[] newArray(int size) {
            return new UserTelephone[size];
        }
    };
}
