package ftn.booking_app_team_2.bookie.clients;

import java.util.Collection;
import java.util.List;

import ftn.booking_app_team_2.bookie.model.NumberOfCancelledReservations;
import ftn.booking_app_team_2.bookie.model.ReservationGuest;
import ftn.booking_app_team_2.bookie.model.ReservationOwner;
import ftn.booking_app_team_2.bookie.model.ReservationStatus;
import ftn.booking_app_team_2.bookie.model.ReservationStatusDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ReservationService {
    String reservationControllerPath = ClientUtils.SERVICE_API_PATH + "reservations";

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
            "Authorization: Bearer " + ClientUtils.JWT
    })
    @GET(reservationControllerPath + "/reservee")
    Call<Collection<ReservationGuest>> searchAndFilterGuest(
            @Query("name") String name,
            @Query("start_timestamp") Long startTimestamp,
            @Query("end_timestamp") Long endTimestamp,
            @Query("status") List<ReservationStatus> statuses
    );

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
            "Authorization: Bearer " + ClientUtils.JWT
    })
    @GET(reservationControllerPath + "/accommodation/owner")
    Call<Collection<ReservationOwner>> searchAndFilterOwner(
            @Query("name") String name,
            @Query("start_timestamp") Long startTimestamp,
            @Query("end_timestamp") Long endTimestamp,
            @Query("status") List<ReservationStatus> statuses
    );

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
            "Authorization: Bearer " + ClientUtils.JWT
    })
    @GET(reservationControllerPath + "/status/cancelled")
    Call<NumberOfCancelledReservations> getNumberOfCancelledReservationsForReservee(
            @Query("reservee_id") Long reserveeId
    );

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
            "Authorization: Bearer " + ClientUtils.JWT
    })
    @PUT(reservationControllerPath + "/{id}/status/accepted")
    Call<ReservationStatusDTO> acceptReservation(@Path("id") Long id);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
            "Authorization: Bearer " + ClientUtils.JWT
    })
    @PUT(reservationControllerPath + "/{id}/status/declined")
    Call<ReservationStatusDTO> declineReservation(@Path("id") Long id);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
            "Authorization: Bearer " + ClientUtils.JWT
    })
    @PUT(reservationControllerPath + "/{id}/status/cancelled")
    Call<ReservationStatusDTO> cancelReservation(@Path("id") Long id);
}
