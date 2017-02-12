package diabai.uw.tacoma.edu.hobbyfinder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import diabai.uw.tacoma.edu.hobbyfinder.user.User;

public class MainActivity extends AppCompatActivity implements
        LogInFragment.OnListFragmentInteractionListener,
        CreateAccountFragment.CreateAccountFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.fragment_container) != null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new LogInFragment())
                    .commit();
        }
    }

    @Override
    public void createAccount(String url) {

    }

    /**
     * When passing information from facebook to
     * create account screen.
     *
     * @param user
     */
    @Override
    public void setUser(User user) {
        CreateAccountFragment createAccountFragment = new CreateAccountFragment();
        Bundle args = new Bundle();
        args.putSerializable(LogInFragment.USER_SELECTED, user);
        createAccountFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, createAccountFragment)
                .addToBackStack(null)
                .commit();
    }

}

