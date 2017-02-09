package diabai.uw.tacoma.edu.hobbyfinder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import diabai.uw.tacoma.edu.hobbyfinder.user.UserContent;

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
    public void onListFragmentInteraction(UserContent.UserItem item) {
     /*   LogInFragment logInFrag = new LogInFragment();
        Bundle args = new Bundle();
        logInFrag.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, logInFrag)
                .addToBackStack(null)
                .commit();*/
    }

    @Override
    public void createAccount(String url) {

    }
}

