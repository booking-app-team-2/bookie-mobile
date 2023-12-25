package ftn.booking_app_team_2.bookie.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserPassword implements Parcelable, Serializable {
    @SerializedName("currentPassword")
    @Expose
    private final String currentPassword;

    @SerializedName("newPassword")
    @Expose
    private final String newPassword;

    public UserPassword(String currentPassword, String newPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }

    protected UserPassword(Parcel in) {
        currentPassword = in.readString();
        newPassword = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(currentPassword);
        dest.writeString(newPassword);
    }

    public static final Creator<UserPassword> CREATOR = new Creator<UserPassword>() {
        @Override
        public UserPassword createFromParcel(Parcel in) {
            return new UserPassword(in);
        }

        @Override
        public UserPassword[] newArray(int size) {
            return new UserPassword[size];
        }
    };
}
