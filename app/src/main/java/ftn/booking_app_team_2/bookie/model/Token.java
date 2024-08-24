package ftn.booking_app_team_2.bookie.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Token implements Parcelable, Serializable {
    @SerializedName("validityPeriod")
    @Expose
    private String validity;

    @SerializedName("jwt")
    @Expose
    private String jWT;

    public Token() {

    }

    protected Token(Parcel source) {
        validity = source.readString();
        jWT = source.readString();
    }

    public static final Creator<Token> CREATOR = new Creator<Token>() {
        @Override
        public Token createFromParcel(Parcel in) {
            return new Token(in);
        }

        @Override
        public Token[] newArray(int size) {
            return new Token[size];
        }
    };

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        return;
    }

    public String getValidity() {
        return validity;
    }

    public String getjWT() {
        return jWT;
    }
}
