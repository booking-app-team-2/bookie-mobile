package ftn.booking_app_team_2.bookie.clients;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientUtils {
    public static final String SERVICE_API_PATH = "http://localhost:8081/api/v1/";

    public static final String JWT = "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJib29raWUtYmFja2VuZCIsInN1YiI6ImRhcmtvQGdtYWlsLmNvbSIsImF1ZCI6IndlYiIsImlhdCI6MTcwNTY5MzM5NSwiaWQiOjMsIm5hbWUiOiJEYXJrbyIsInJvbGUiOiJHdWVzdCIsImV4cCI6MTcwNTY5NTE5NX0.N0JF1B55w7R_6SJvsGVOS6Bjq92rSXqtZa4eikDWvWcIRineMUe350822l6NCNNp2I0hz8Ei9hwI6Pk1FRfksw";

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
}
