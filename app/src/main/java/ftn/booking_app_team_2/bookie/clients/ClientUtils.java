package ftn.booking_app_team_2.bookie.clients;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientUtils {
    public static final String SERVICE_API_PATH = "http://localhost:8081/api/v1/";

    public static final String JWT = "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJib29raWUtYmFja2VuZCIsInN1YiI6ImRhcmtvQGdtYWlsLmNvbSIsImF1ZCI6Im1vYmlsZSIsImlhdCI6MTcwNTg1NDQ2MiwiaWQiOjMsIm5hbWUiOiJEYXJrbyIsInJvbGUiOiJHdWVzdCIsImV4cCI6MTcwNTk0MDg2Mn0.vY5h4hd28_amE8uO49ZhmCQVRywW8pUDQSAS8Du3bcChkQb04tC9o037H8t78kXgW9debq58i4yormayrxZpfQ";

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
