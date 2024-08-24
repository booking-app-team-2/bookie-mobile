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


    @GET(accommodationReviewControllerPath + "/unapproved")
    Call<Collection<AccommodationReviewDTO>> getUnapprovedAccomodationReviews(
    );


    @GET(ownerReviewControllerPath + "/unapproved")
    Call<Collection<OwnerReviewDTO>> getUnapprovedOwnerReviews(
    );


    @GET(accommodationReviewControllerPath + "/reported")
    Call<Collection<AccommodationReviewDTO>> getReportedAccomodationReviews(
    );


    @GET(ownerReviewControllerPath + "/reported")
    Call<Collection<OwnerReviewDTO>> getReportedOwnerReviews(
    );


    @PUT(accommodationReviewControllerPath + "/unapproved/{id}")
    Call<Void> approveAccommodationReview(
            @Path("id") Long id
    );


    @DELETE(accommodationReviewControllerPath + "/unapproved/{id}")
    Call<Void> denyAccommodationReview(
            @Path("id") Long id
    );


    @PUT(ownerReviewControllerPath + "/unapproved/{id}")
    Call<Void> approveOwnerReview(
            @Path("id") Long id
    );


    @DELETE(ownerReviewControllerPath + "/unapproved/{id}")
    Call<Void> denyOwnerReview(
            @Path("id") Long id
    );


    @DELETE(ownerReviewControllerPath + "/reported/{id}")
    Call<Void> deleteReportedOwnerReview(
            @Path("id") Long id
    );


    @DELETE(accommodationReviewControllerPath + "/reported/{id}")
    Call<Void> deleteReportedAccommodationReview(
            @Path("id") Long id
    );

    @GET(accommodationReviewControllerPath + "/{id}")
    Call<Collection<AccommodationReviewDTO>> getReviews(@Path("id") Long accommodationId);
}
