package ftn.booking_app_team_2.bookie.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LoginCredentials implements Parcelable, Serializable {
    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("password")
    @Expose
    private String password;

    public LoginCredentials() {}

    public LoginCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    protected LoginCredentials(Parcel in) {
        username = in.readString();
        password = in.readString();
    }

    public static final Creator<LoginCredentials> CREATOR = new Creator<LoginCredentials>() {
        @Override
        public LoginCredentials createFromParcel(Parcel in) {
            return new LoginCredentials(in);
        }

        @Override
        public LoginCredentials[] newArray(int size) {
            return new LoginCredentials[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(password);
    }
}
