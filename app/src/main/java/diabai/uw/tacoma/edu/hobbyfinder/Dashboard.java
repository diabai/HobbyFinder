package diabai.uw.tacoma.edu.hobbyfinder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;

/**
 * DashBoard class.
 * @author: Ibrahim D., Edgard S.
 * This class is contains the main screen that users see when the first log in to the app,
 * if they already registered for an account
 */
public class Dashboard extends AppCompatActivity {

    /**
     * Array of hobbies.
     */
    private static String[] mHobbies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        ProfilePictureView mProfilePictureView = (ProfilePictureView) findViewById(R.id.profile_image);
        mProfilePictureView.setProfileId(Profile.getCurrentProfile().getId());
    }

    /**
     * Menu bar.
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_user, menu);
        return true;
    }

    /**
     * Used to prevent crash on nougat or emulator
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // below line to be commented to prevent crash on nougat.
        // http://blog.sqisland.com/2016/09/transactiontoolargeexception-crashes-nougat.html
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.action_logout) {
            SharedPreferences sharedPreferences =
                    getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
            sharedPreferences.edit().putBoolean(getString(R.string.LOGGEDIN), false)
                    .apply();

            LoginManager.getInstance().logOut();
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
            return true;
        } else {
            return false;
        }
    }

    public void setDashboardHobbies(String[] s) {
        mHobbies = s;
    }

    public static String[] getDashboardHobbies() {
        return mHobbies;
    }

    /**
     * This method gets launched one a users clicks the search button
     */
    public void launch(View v) {
        DialogFragment fragment = null;

        if (v.getId() == R.id.btn_search) {
            fragment = new HobbyFragment();
        }
        if (fragment != null)
            //trying to get data from that fragment so that i can  search retrieve those users from the db with similiar hobbies
            fragment.show(getSupportFragmentManager(), "search");
    }

    /**
     * Called to show the edit profile activity.
     * @param v the view
     */
    public void editUserProfile(View v) {
        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivity(intent);
    }
}
