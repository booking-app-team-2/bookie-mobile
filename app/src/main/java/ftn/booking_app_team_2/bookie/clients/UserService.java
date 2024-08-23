package ftn.booking_app_team_2.bookie.clients;

import ftn.booking_app_team_2.bookie.model.User;
import ftn.booking_app_team_2.bookie.model.UserAddress;
import ftn.booking_app_team_2.bookie.model.UserBasicInfo;
import ftn.booking_app_team_2.bookie.model.UserPassword;
import ftn.booking_app_team_2.bookie.model.UserTelephone;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserService {
    String userControllerPath = ClientUtils.SERVICE_API_PATH + "users";

    @GET(userControllerPath + "/{id}")
    Call<User> getUser(@Path("id") Long id);

    @PUT(userControllerPath + "/{id}/basic-info")
    Call<UserBasicInfo> putUserBasicInfo(@Path("id") Long id, @Body UserBasicInfo userBasicInfo);

    @PUT(userControllerPath + "/{id}/telephone")
    Call<UserTelephone> putUserTelephone(@Path("id") Long id, @Body UserTelephone userTelephone);

    @PUT(userControllerPath + "/{id}/address-of-residence")
    Call<UserAddress> putUserAddress(@Path("id") Long id, @Body UserAddress userAddress);

    @PUT(userControllerPath + "/{id}/password")
    Call<UserPassword> putUserPassword(@Path("id") Long id, @Body UserPassword userPassword);

    @DELETE(userControllerPath + "/{id}")
    Call<ResponseBody> delete(@Path("id") Long id);
}
