package ftn.booking_app_team_2.bookie.clients;

import java.util.Collection;
import java.util.List;

import ftn.booking_app_team_2.bookie.model.AccommodationReviewDTO;
import ftn.booking_app_team_2.bookie.model.OwnerReviewDTO;
import ftn.booking_app_team_2.bookie.model.ReservationGuest;
import ftn.booking_app_team_2.bookie.model.ReservationStatus;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ReviewService {

    String ownerReviewControllerPath = ClientUtils.SERVICE_API_PATH + "owner-reviews";
    String accommodationReviewControllerPath = ClientUtils.SERVICE_API_PATH + "accommodation-reviews";

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
            "Authorization: Bearer " + ClientUtils.JWT
    })
    @GET(accommodationReviewControllerPath + "/unapproved")
    Call<Collection<AccommodationReviewDTO>> getUnapprovedAccomodationReviews(
    );

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
            "Authorization: Bearer " + ClientUtils.JWT
    })
    @GET(ownerReviewControllerPath + "/unapproved")
    Call<Collection<OwnerReviewDTO>> getUnapprovedOwnerReviews(
    );

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
            "Authorization: Bearer " + ClientUtils.JWT
    })
    @GET(accommodationReviewControllerPath + "/reported")
    Call<Collection<AccommodationReviewDTO>> getReportedAccomodationReviews(
    );

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
            "Authorization: Bearer " + ClientUtils.JWT
    })
    @GET(ownerReviewControllerPath + "/reported")
    Call<Collection<OwnerReviewDTO>> getReportedOwnerReviews(
    );

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
            "Authorization: Bearer " + ClientUtils.JWT
    })
    @PUT(accommodationReviewControllerPath + "/unapproved/{id}")
    Call<Void> approveAccommodationReview(
            @Path("id") Long id
    );

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
            "Authorization: Bearer " + ClientUtils.JWT
    })
    @DELETE(accommodationReviewControllerPath + "/unapproved/{id}")
    Call<Void> denyAccommodationReview(
            @Path("id") Long id
    );

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
            "Authorization: Bearer " + ClientUtils.JWT
    })
    @PUT(ownerReviewControllerPath + "/unapproved/{id}")
    Call<Void> approveOwnerReview(
            @Path("id") Long id
    );

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
            "Authorization: Bearer " + ClientUtils.JWT
    })
    @DELETE(ownerReviewControllerPath + "/unapproved/{id}")
    Call<Void> denyOwnerReview(
            @Path("id") Long id
    );

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
            "Authorization: Bearer " + ClientUtils.JWT
    })
    @DELETE(ownerReviewControllerPath + "/reported/{id}")
    Call<Void> deleteReportedOwnerReview(
            @Path("id") Long id
    );

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
            "Authorization: Bearer " + ClientUtils.JWT
    })
    @DELETE(accommodationReviewControllerPath + "/reported/{id}")
    Call<Void> deleteReportedAccommodationReview(
            @Path("id") Long id
    );
}
