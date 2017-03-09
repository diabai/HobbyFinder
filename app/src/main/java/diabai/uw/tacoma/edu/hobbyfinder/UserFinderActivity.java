package diabai.uw.tacoma.edu.hobbyfinder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import diabai.uw.tacoma.edu.hobbyfinder.user.User;

/**
 * UserFinderActivity class.
 * This class launches the list fragment that is displayed when users do a search
 *
 * @author Ibrahim Diabate, Edgard Solorzano
 */
public class UserFinderActivity extends AppCompatActivity implements
        HobbyFragment.UserHobbiesListener, UserFragment.OnListUserListener {

    String s = null;

    /**
     * Lifecycle method first called once this Activity is launched.
     * @param savedInstanceState data that might have been passed from other activities/fragments
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_finder);

        if (savedInstanceState == null || getSupportFragmentManager().findFragmentById(R.id.list) == null) {
            UserFragment userFragment = new UserFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_user_finder, userFragment)
                    .commit();
        }
    }

    /**
     * Passes the hobbies from a fragment to here
     * @param theUserHobbies the user hobbies passed
     */
    @Override
    public void passHobbies(String theUserHobbies) {
        s = theUserHobbies;

    }

    /**
     * Gets the user hobbies
     * @return
     */
    public String getHobbiesPassForSearch() {
        return s;
    }

    /**
     * When user is viewing a profile after search
     * they can email them to meet and have fun.
     *
     * @param view
     */
    public void emailUser(View view) {
    }

    /**
     * When a user clicks on a list of users after
     * searching by hobby.
     *
     * @param user the user selected
     */
    @Override
    public void onListFragmentInteraction(User user) {
        UserProfileFragment userProfileFragment = new UserProfileFragment();
        Bundle args = new Bundle();
        args.putSerializable(UserProfileFragment.USER_ITEM_SELECTED, user);
        userProfileFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_user_finder, userProfileFragment)
                .addToBackStack(null)
                .commit();
    }

    /**
     * Sends an email, lets the user choose what provider they want
     * to use.
     * @param email
     * @param message
     */
    @Override
    public void sendEmail(String email, String message) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL,new String[] {email});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Found a buddy for hobbies!");
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send e-mail."));
            finish();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "There is no email client installed.",
                    Toast.LENGTH_LONG).show();
        }
    }
}
