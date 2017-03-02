package diabai.uw.tacoma.edu.hobbyfinder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;

public class Dashboard extends AppCompatActivity {
    private String hobbies;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // The line below will get all the fields available in the JSON object
        ProfilePictureView mProfilePictureView = (ProfilePictureView) findViewById(R.id.profile_image);
        mProfilePictureView.setProfileId(Profile.getCurrentProfile().getId());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_user, menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // below line to be commented to prevent crash on nougat.
        // http://blog.sqisland.com/2016/09/transactiontoolargeexception-crashes-nougat.html
        //
        //super.onSaveInstanceState(outState);
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


    public void setDashboardHobbies(String s) {
        hobbies = s;
    }

    public String getDashboardHobbies() {
        return hobbies;
    }


    /**
     * This method gets launched one a users clicks the search button
     */
    public void launch(View v) {

        Log.i("Bye", "**");

        DialogFragment fragment = null;

        if (v.getId() == R.id.btn_search) {

            fragment = new HobbyFragment();

        }
        if (fragment != null)
            //trying to get data from that fragment so that i can  search retrieve those users from the db with similiar hobbies
         //   fragment.setTargetFragment((Fragment)this, 1);
            fragment.show(getSupportFragmentManager(), "search");
    }

    public void editUserProfile(View v) {
        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivity(intent);
    }
}
