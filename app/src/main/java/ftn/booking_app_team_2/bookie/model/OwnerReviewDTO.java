package ftn.booking_app_team_2.bookie.model;

import android.os.Parcelable;

import java.io.Serializable;
import android.os.Parcel;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class OwnerReviewDTO implements Parcelable, Serializable{
    @SerializedName("id")
    @Expose
    private Long id = null;

    @SerializedName("grade")
    @Expose
    private final Float grade;

    @SerializedName("comment")
    @Expose
    private final String comment;

    @SerializedName("timestampOfCreation")
    @Expose
    private final Long timestampOfCreation;

    @SerializedName("reviewerId")
    @Expose
    private final Long reviewerId;

    @SerializedName("reviewerName")
    @Expose
    private String reviewerName = "";

    @SerializedName("revieweeId")
    @Expose
    private Long revieweeId;

    protected OwnerReviewDTO(Parcel in) {
        id = in.readLong();
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        if (in.readByte() == 0) {
            grade = null;
        } else {
            grade = in.readFloat();
        }
        comment = in.readString();
        if (in.readByte() == 0) {
            timestampOfCreation = null;
        } else {
            timestampOfCreation = in.readLong();
        }
        if (in.readByte() == 0) {
            reviewerId = null;
        } else {
            reviewerId = in.readLong();
        }
        reviewerName = in.readString();
        revieweeId = in.readLong();
    }

    public static final Creator<AccommodationReviewDTO> CREATOR = new Creator<AccommodationReviewDTO>() {
        @Override
        public AccommodationReviewDTO createFromParcel(Parcel in) {
            return new AccommodationReviewDTO(in);
        }

        @Override
        public AccommodationReviewDTO[] newArray(int size) {
            return new AccommodationReviewDTO[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        if (grade == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(grade);
        }
        dest.writeString(comment);
        if (timestampOfCreation == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(timestampOfCreation);
        }
        if (reviewerId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(reviewerId);
        }
        dest.writeString(reviewerName);
        dest.writeLong(revieweeId);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getGrade() {
        return grade;
    }

    public String getComment() {
        return comment;
    }

    public Long getTimestampOfCreation() {
        return timestampOfCreation;
    }

    public Long getReviewerId() {
        return reviewerId;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public Long getRevieweeId() {
        return revieweeId;
    }

    public void setRevieweeId(Long revieweeId) {
        this.revieweeId = revieweeId;
    }
}
