package ftn.booking_app_team_2.bookie.clients;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface ImageService {
    String imageControllerPath = ClientUtils.SERVICE_API_PATH + "images";

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: image/jpeg",
            "Authorization: Bearer " + ClientUtils.JWT
    })
    @GET(imageControllerPath + "/{id}")
    Call<ResponseBody> getImage(@Path("id") Long id);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: image/jpeg",
            "Authorization: Bearer " + ClientUtils.JWT
    })
    @DELETE(imageControllerPath + "/{id}")
    Call<ResponseBody> deleteImage(@Path("id") Long id);
}
