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
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AccommodationService {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
            "Authorization: Bearer " + ClientUtils.JWT
    })
    @GET("accommodations/search")
    Call<Collection<AccommodationDTO>> getSearchedAccommodations(
            @Query("location") String location,
            @Query("numberOfGuests") Integer numberOfGuests,
            @Query("startDate") Long startDate,
            @Query("endDate") Long endDate
    );

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
            "Authorization: Bearer " + ClientUtils.JWT
    })
    @GET("accommodations/unapproved")
    Call<Collection<AccommodationDTO>> getUnapproved();

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
            "Authorization: Bearer " + ClientUtils.JWT
    })
    @GET("accommodations/{id}")
    Call<AccommodationDTO> getAccommodation(@Path("id") Long id);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
            "Authorization: Bearer " + ClientUtils.JWT
    })
    @GET("accommodations/owner-accommodations/{ownerId}")
    Call<Collection<AccommodationDTO>> getAccommodationsByOwner(@Path("ownerId") Long ownerId);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
            "Authorization: Bearer " + ClientUtils.JWT
    })
    @PUT("accommodations/{id}/basic-info")
    Call<AccommodationBasicInfo> putAccommodationBasicInfo(
            @Path("id") Long id,
            @Body AccommodationBasicInfo accommodationBasicInfo
    );

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
            "Authorization: Bearer " + ClientUtils.JWT
    })
    @PUT("accommodations/{id}/is-approved")
    Call<AccommodationApproval> putIsApproved(@Path("id") Long id,
                                              @Body AccommodationApproval accommodationApproval);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
            "Authorization: Bearer " + ClientUtils.JWT
    })
    @PUT("accommodations/{id}/is-reservation-auto-accepted")
    Call<AccommodationAutoAccept> putIsReservationAutoAccepted(
            @Path("id") Long id,
            @Body AccommodationAutoAccept accommodationAutoAccept
    );
}
