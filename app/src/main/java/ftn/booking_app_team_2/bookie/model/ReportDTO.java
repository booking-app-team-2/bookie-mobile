package ftn.booking_app_team_2.bookie.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ReportDTO implements Parcelable, Serializable {

    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("body")
    @Expose
    private String body;

    @SerializedName("reporterName")
    @Expose
    private String reporterName;

    @SerializedName("reporteeName")
    @Expose
    private String reporteeName;

    protected ReportDTO(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        body = in.readString();
        reporterName = in.readString();
        reporteeName = in.readString();
    }

    public static final Creator<ReportDTO> CREATOR = new Creator<ReportDTO>() {
        @Override
        public ReportDTO createFromParcel(Parcel in) {
            return new ReportDTO(in);
        }

        @Override
        public ReportDTO[] newArray(int size) {
            return new ReportDTO[size];
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
        dest.writeString(body);
        dest.writeString(reporterName);
        dest.writeString(reporteeName);
    }

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

    public String getReporterName() {
        return reporterName;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    public String getReporteeName() {
        return reporteeName;
    }

    public void setReporteeName(String reporteeName) {
        this.reporteeName = reporteeName;
    }
}
