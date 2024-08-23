package ftn.booking_app_team_2.bookie.clients;

import android.content.Context;

import ftn.booking_app_team_2.bookie.tools.SessionManager;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class AuthInterceptor implements Interceptor {
    private final Context context;

    public AuthInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        SessionManager sessionManager = new SessionManager(context);
        String token = sessionManager.getJwtToken(); // Method to fetch JWT from SessionManager

        Request newRequest = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer " + token)
                .addHeader("User-Agent", "Mobile-Android")
                .addHeader("Content-Type", "application/json")
                .build();

        return chain.proceed(newRequest);
    }

    public static OkHttpClient.Builder getHttpClientBuilder(Context context) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new AuthInterceptor(context));
        return builder;
    }
}
