package ftn.booking_app_team_2.bookie.clients;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientUtils {
    public static final String SERVICE_API_PATH = "http://localhost:8081/api/v1/";

    public static final String JWT = "eyJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJib29raWUtYmFja2VuZCIsInN1YiI6Im93bmVyQGdtYWlsLmNvbSIsImF1ZCI6Im1vYmlsZSIsImlhdCI6MTcyNDQxMzA4NywiaWQiOjQsIm5hbWUiOiJPd25lciIsInJvbGUiOiJPd25lciIsImV4cCI6MTcyNDQ5OTQ4N30.L3OvpA4hsCKiT1Z9_BrwRy0aSVdmeIKP4z8bc9elprsoz_4EriNZ_GuEbe7pAaYnyTrX6KHdf0or6_cjSrw2HQ";
    public static OkHttpClient test() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return new OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(httpLoggingInterceptor)
                .retryOnConnectionFailure(true).build();
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
    public static ReviewService reviewService = retrofit.create(ReviewService.class);
    public static ReportService reportService = retrofit.create(ReportService.class);
    public static NotificationService notificationService = retrofit.create(NotificationService.class);
    public static GuestService guestService = retrofit.create(GuestService.class);
}
