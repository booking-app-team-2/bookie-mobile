package ftn.booking_app_team_2.bookie.clients;

import ftn.booking_app_team_2.bookie.model.LoginCredentials;
import ftn.booking_app_team_2.bookie.model.NewUserDTO;
import ftn.booking_app_team_2.bookie.model.Token;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {

    @POST(ClientUtils.AUTH_PATH + "login")
    Call<Token> getToken(@Body LoginCredentials loginCredentials);

    @POST(ClientUtils.AUTH_PATH + "register")
    Call<NewUserDTO> register(@Body NewUserDTO user);
}
