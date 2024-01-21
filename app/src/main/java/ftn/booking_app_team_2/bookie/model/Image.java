package ftn.booking_app_team_2.bookie.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Image implements Parcelable, Serializable {
    @SerializedName("id")
    @Expose
    private final Long id;

    @SerializedName("path")
    @Expose
    private final String path;

    @SerializedName("name")
    @Expose
    private final String name;

    @SerializedName("type")
    @Expose
    private final String type;

    @SerializedName("deleted")
    @Expose
    private final boolean isDeleted;

    protected Image(Parcel in) {
        id = in.readLong();
        path = in.readString();
        name = in.readString();
        type = in.readString();
        isDeleted = in.readByte() != 0;
    }

    public Long getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(path);
        dest.writeString(name);
        dest.writeString(type);
        dest.writeByte((byte) (isDeleted ? 1 : 0));
    }

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };
}
