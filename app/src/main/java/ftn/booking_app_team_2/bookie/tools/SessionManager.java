package ftn.booking_app_team_2.bookie.tools;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.auth0.android.jwt.JWT;

import java.util.Objects;

import ftn.booking_app_team_2.bookie.activities.LoginRegisterActivity;

public class SessionManager {

    private final Context context;

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;

    private static final String KEY_JWT = "jwt";
    private static final String PREF_FILE_NAME = "user_data";

    private static final String KEY_USER_ID = "id";
    private static final String KEY_USER_TYPE = "type";

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void createLoginSession(String jwt) {
        JWT parsedJwt = new JWT(jwt);

        editor.putString(KEY_JWT, jwt);

        editor.putLong(
                KEY_USER_ID,
                Objects.requireNonNull(parsedJwt.getClaim("id").asLong())
        );
        editor.putString(
                KEY_USER_TYPE,
                parsedJwt.getClaim("role").asString()
        );

        editor.commit();
    }
    public String getJwtToken() { return sharedPreferences.getString(KEY_JWT, null); }

    public Long getUserId() {
        return sharedPreferences.getLong(KEY_USER_ID, 0);
    }

    public String getUserType() {
        return sharedPreferences.getString(KEY_USER_TYPE, null);
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();

        Intent intent = new Intent(context, LoginRegisterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(intent);
    }
}
