package ftn.booking_app_team_2.bookie.tools;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import ftn.booking_app_team_2.bookie.activities.LoginRegisterActivity;

public class SessionManager {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;
    private static final String PREF_FILE_NAME = "user_data";
    private static final String KEY_USER_TYPE = "type";

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    // TODO: Potentially change to enum, depending on the user model class.
    public void createLoginSession(String userType) {
        editor.putString(KEY_USER_TYPE, userType);
        editor.commit();
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
