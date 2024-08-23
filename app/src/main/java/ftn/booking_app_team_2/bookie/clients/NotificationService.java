package ftn.booking_app_team_2.bookie.clients;

import java.util.Collection;

import ftn.booking_app_team_2.bookie.model.NotificationDTO;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface NotificationService {

    String notificationControllerPath = ClientUtils.SERVICE_API_PATH + "notifications";

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
            "Authorization: Bearer " + ClientUtils.JWT
    })
    @GET(notificationControllerPath + "/{id}")
    Call<Collection<NotificationDTO>> getUserNotifications(
            @Path("id") Long id
    );

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type: application/json",
            "Authorization: Bearer " + ClientUtils.JWT
    })
    @DELETE(notificationControllerPath + "/{id}")
    Call<Void> deleteNotification(
            @Path("id") Long id
    );
}
