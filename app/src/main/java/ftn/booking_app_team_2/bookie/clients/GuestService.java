package ftn.booking_app_team_2.bookie.clients;

import java.util.Collection;

import ftn.booking_app_team_2.bookie.model.AccommodationDTO;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GuestService {

    String guestControllerPath = ClientUtils.SERVICE_API_PATH + "guests";

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
            "Authorization: Bearer " + ClientUtils.JWT
    })
    @GET(guestControllerPath + "/{id}/favourite-accommodations")
    Call<Collection<AccommodationDTO>> getFavouriteAccommodations(@Path("id") Long id);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
            "Authorization: Bearer " + ClientUtils.JWT
    })
    @POST(guestControllerPath + "/{id}/favourite-accommodations")
    Call<Void> addFavouriteAccommodations(@Path("id") Long id, @Query("accommodation_id") Long accommodationId);
}
