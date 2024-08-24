package ftn.booking_app_team_2.bookie.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NewUserDTO implements Parcelable, Serializable {
    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("surname")
    @Expose
    private String surname;

    @SerializedName("addressOfResidence")
    @Expose
    private String address;

    @SerializedName("telephone")
    @Expose
    private String telephone;

    @SerializedName("role")
    @Expose
    private String role;

    public NewUserDTO() {

    }

    protected NewUserDTO(Parcel source) {
        username = source.readString();
        password = source.readString();
        name = source.readString();
        surname = source.readString();
        address = source.readString();
        telephone = source.readString();
        role = source.readString();
    }

    public NewUserDTO(String usernameArg, String passwordArg, String nameArg, String lastNameArg,
                      String addressArg, String phoneArg, String roleArg) {
        username = usernameArg;
        password = passwordArg;
        name = nameArg;
        surname = lastNameArg;
        address = addressArg;
        telephone = phoneArg;
        role = roleArg;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(name);
        dest.writeString(surname);
        dest.writeString(address);
        dest.writeString(telephone);
        dest.writeString(role);
    }

    public static final Creator<ftn.booking_app_team_2.bookie.model.NewUserDTO> CREATOR = new Creator<NewUserDTO>() {
        @Override
        public ftn.booking_app_team_2.bookie.model.NewUserDTO createFromParcel(Parcel source) {
            return new ftn.booking_app_team_2.bookie.model.NewUserDTO(source);
        }

        @Override
        public ftn.booking_app_team_2.bookie.model.NewUserDTO[] newArray(int size) {
            return new ftn.booking_app_team_2.bookie.model.NewUserDTO[size];
        }
    };
}