package ftn.booking_app_team_2.bookie.clients;
import java.util.Collection;

import ftn.booking_app_team_2.bookie.model.AccommodationDTO;
import retrofit2.Call;
import retrofit2.http.GET;
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

    @GET("accommodations/{id}")
    Call<AccommodationDTO> getAccommodation(@Path("id") Long id);
}
