package ftn.booking_app_team_2.bookie.clients;

import java.util.Collection;

import ftn.booking_app_team_2.bookie.model.AccommodationApproval;
import ftn.booking_app_team_2.bookie.model.AccommodationAutoAccept;
import ftn.booking_app_team_2.bookie.model.AccommodationBasicInfo;
import ftn.booking_app_team_2.bookie.model.AccommodationDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AccommodationService {
    @GET("accommodations/search")
    Call<Collection<AccommodationDTO>> getSearchedAccommodations(
            @Query("location") String location,
            @Query("numberOfGuests") Integer numberOfGuests,
            @Query("startDate") Long startDate,
            @Query("endDate") Long endDate
    );

    @POST("accommodations")
    Call<AccommodationDTO> createAccommodation(@Body AccommodationDTO accommodation);

    @GET("accommodations/unapproved")
    Call<Collection<AccommodationDTO>> getUnapproved();

    @GET("accommodations/{id}")
    Call<AccommodationDTO> getAccommodation(@Path("id") Long id);

    @GET("accommodations/owner-accommodations/{ownerId}")
    Call<Collection<AccommodationDTO>> getAccommodationsByOwner(@Path("ownerId") Long ownerId);

    @PUT("accommodations/{id}/basic-info")
    Call<AccommodationBasicInfo> putAccommodationBasicInfo(
            @Path("id") Long id,
            @Body AccommodationBasicInfo accommodationBasicInfo
    );

    @PUT("accommodations/{id}/is-approved")
    Call<AccommodationApproval> putIsApproved(@Path("id") Long id,
                                              @Body AccommodationApproval accommodationApproval);

    @PUT("accommodations/{id}/is-reservation-auto-accepted")
    Call<AccommodationAutoAccept> putIsReservationAutoAccepted(
            @Path("id") Long id,
            @Body AccommodationAutoAccept accommodationAutoAccept
    );
}
