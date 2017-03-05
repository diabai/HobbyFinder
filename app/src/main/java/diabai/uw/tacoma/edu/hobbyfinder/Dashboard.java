package diabai.uw.tacoma.edu.hobbyfinder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;
import com.facebook.share.ShareApi;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;

public class Dashboard extends AppCompatActivity {

    private static String[] hobbies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // The line below will get all the fields available in the JSON object
        ProfilePictureView mProfilePictureView = (ProfilePictureView) findViewById(R.id.profile_image);
        mProfilePictureView.setProfileId(Profile.getCurrentProfile().getId());

        // Handling the Facebook share button
        Button shareButton = (Button) findViewById(R.id.btn_share);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharePhotoToFacebook();
            }
        });
    }

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
        hobbies = s;
    }

    public static String[] getDashboardHobbies() {
        return hobbies;
    }

    /**
     * Used to share a photo to facebook. Currently we are
     * in the process of getting Facebook to approve sharing for our
     * application for our test account.
     */
    private void sharePhotoToFacebook() {
        Bitmap image = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(image)
                .setCaption("Check out how much fun I am having from using HobbyFinder!!")
                .build();
        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();
        ShareApi.share(content, null);
    }

    /**
     * This method gets launched one a users clicks the search button
     */
    //TODO: Rename this to a better name, maybe submitSearch
    public void launch(View v) {
        DialogFragment fragment = null;

        if (v.getId() == R.id.btn_search) {
            fragment = new HobbyFragment();
        }
        if (fragment != null)
            //trying to get data from that fragment so that i can  search retrieve those users from the db with similiar hobbies
            //   fragment.setTargetFragment((Fragment)this, 1);
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
