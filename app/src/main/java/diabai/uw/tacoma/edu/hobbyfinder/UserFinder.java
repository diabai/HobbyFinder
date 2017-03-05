package diabai.uw.tacoma.edu.hobbyfinder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import diabai.uw.tacoma.edu.hobbyfinder.user.User;

import static java.security.AccessController.getContext;

/**
 * UserFinder class.
 *This class launches the list fragment that is displayed when users do a search
 * @author: Ibrahim Diabate
 *
 */
public class UserFinder extends AppCompatActivity implements HobbyFragment.UserHobbiesListener,UserFragment.OnListUserListener {

    String s = null;
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
    @Override
    public void passHobbies(String theUserHobbies) {
        s =  theUserHobbies;

    }
    public String getHobbiesPassForSearch() {
        return s;
    }

    @Override
    public void onListFragmentInteraction(User user) {

    }
}
