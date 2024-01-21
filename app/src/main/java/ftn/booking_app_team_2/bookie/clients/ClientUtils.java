package ftn.booking_app_team_2.bookie.clients;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientUtils {
    public static final String SERVICE_API_PATH = "http://localhost:8081/api/v1/";

    public static final String JWT = "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJib29raWUtYmFja2VuZCIsInN1YiI6Im93bmVyQGdtYWlsLmNvbSIsImF1ZCI6IndlYiIsImlhdCI6MTcwNTY0MDU2NCwiaWQiOjQsIm5hbWUiOiJPd25lciIsInJvbGUiOiJPd25lciIsImV4cCI6MTcwNTY0MjM2NH0.7Z4VhmM2nL3XM3LsRZOfxbcjnaP4kKckfAJrc7I2q_sNTzhSZtTMqbFZ5Gaq6V64uCuxDeu82JXUOgq4Y59-5w";

    public static OkHttpClient test() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(httpLoggingInterceptor).build();
    }

    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(SERVICE_API_PATH)
            .addConverterFactory(GsonConverterFactory.create())
            .client(test())
            .build();

    public static UserService userService = retrofit.create(UserService.class);
    public static AccommodationService accommodationService=retrofit.create(AccommodationService.class);
    public static ReservationService reservationService = retrofit.create(ReservationService.class);
    public static ImageService imageService = retrofit.create(ImageService.class);
}
