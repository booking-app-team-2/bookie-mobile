package ftn.booking_app_team_2.bookie.clients;

import java.util.Collection;
import java.util.List;

import ftn.booking_app_team_2.bookie.model.ReportDTO;
import ftn.booking_app_team_2.bookie.model.ReservationGuest;
import ftn.booking_app_team_2.bookie.model.ReservationStatus;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ReportService {
    String reportControllerPath = ClientUtils.SERVICE_API_PATH + "reports";


    @GET(reportControllerPath)
    Call<Collection<ReportDTO>> getAllReports();


    @PUT(reportControllerPath + "/{id}")
    Call<Void> blockUser(@Path("id") Long id);

    @POST(reportControllerPath)
    Call<ReportDTO> createReport(@Body ReportDTO report);
}
