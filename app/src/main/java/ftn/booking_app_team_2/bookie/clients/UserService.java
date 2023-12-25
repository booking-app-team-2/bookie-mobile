package ftn.booking_app_team_2.bookie.clients;

import ftn.booking_app_team_2.bookie.model.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface UserService {
    String userControllerPath = ClientUtils.SERVICE_API_PATH + "users";

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
            "Authorization: Bearer " + ClientUtils.JWT
    })
    @GET(userControllerPath + "/{id}")
    Call<User> getUser(@Path("id") Long id);
}
