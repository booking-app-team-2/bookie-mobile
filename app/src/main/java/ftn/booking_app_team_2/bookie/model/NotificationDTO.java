package ftn.booking_app_team_2.bookie.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NotificationDTO implements Parcelable, Serializable {

    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("body")
    @Expose
    private String body;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("receiverId")
    @Expose
    private String receiverId;

    protected NotificationDTO(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        body = in.readString();
        type = in.readString();
        receiverId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(body);
        dest.writeString(type);
        dest.writeString(receiverId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NotificationDTO> CREATOR = new Creator<NotificationDTO>() {
        @Override
        public NotificationDTO createFromParcel(Parcel in) {
            return new NotificationDTO(in);
        }

        @Override
        public NotificationDTO[] newArray(int size) {
            return new NotificationDTO[size];
        }
    };

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }
}
