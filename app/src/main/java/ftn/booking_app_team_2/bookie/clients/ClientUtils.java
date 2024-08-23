package ftn.booking_app_team_2.bookie.clients;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientUtils {
    public static final String SERVICE_API_PATH = "http://localhost:8081/api/v1/";
    public static final String AUTH_PATH = "http://localhost:8081/authentication/";

    private static OkHttpClient getOkHttpClient(Context context) {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return AuthInterceptor.getHttpClientBuilder(context)
                .addInterceptor(httpLoggingInterceptor)
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build();
    }

    public static Retrofit getRetrofitInstance(Context context) {
        return new Retrofit.Builder()
                .baseUrl(SERVICE_API_PATH)
                .client(getOkHttpClient(context))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static UserService getUserService(Context context) {
        return getRetrofitInstance(context).create(UserService.class);
    }

    public static AccommodationService getAccommodationService(Context context) {
        return getRetrofitInstance(context).create(AccommodationService.class);
    }

    public static ReservationService getReservationService(Context context) {
        return getRetrofitInstance(context).create(ReservationService.class);
    }

    public static ImageService getImageService(Context context) {
        return getRetrofitInstance(context).create(ImageService.class);
    }

    public static AuthService getAuthService(Context context) {
        return getRetrofitInstance(context).create(AuthService.class);
    }
}
