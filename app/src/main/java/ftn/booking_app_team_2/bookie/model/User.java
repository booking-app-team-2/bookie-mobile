package ftn.booking_app_team_2.bookie.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Parcelable, Serializable {
    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("surname")
    @Expose
    private String surname;

    @SerializedName("addressOfResidence")
    @Expose
    private String addressOfResidence;

    @SerializedName("telephone")
    @Expose
    private String telephone;

    public String getUsername() {
        return username;
    }

    public User() {

    }

    protected User(Parcel source) {
        username = source.readString();
        name = source.readString();
        surname = source.readString();
        addressOfResidence = source.readString();
        telephone = source.readString();
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getAddressOfResidence() {
        return addressOfResidence;
    }

    public String getTelephone() {
        return telephone;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(name);
        dest.writeString(surname);
        dest.writeString(addressOfResidence);
        dest.writeString(telephone);
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
