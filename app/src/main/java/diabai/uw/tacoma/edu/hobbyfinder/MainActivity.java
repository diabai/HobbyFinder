package diabai.uw.tacoma.edu.hobbyfinder;

import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import diabai.uw.tacoma.edu.hobbyfinder.user.User;

/**
 * Main activity class for the application.
 *
 * @author Edgard Solorzano
 * @author Ibrahim Diabate
 * @version 2.0
 */
public class MainActivity extends AppCompatActivity implements
        LogInFragment.OnListFragmentInteractionListener,
        CreateAccountFragment.CreateAccountFragmentInteractionListener {

    /**
     * Method generated by default once a fragment is created
     *
     * @param savedInstanceState Bundle object containing information that might have
     *                           been saved from previous fragments/activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Adds LogInFragment to this activy's layout when app first starts
        if (findViewById(R.id.fragment_container) != null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new LogInFragment())
                    .commit();
        }
    }


    /**
     * The create account method used from
     * the Create account fragment listener. Handles the
     * submission of an account.
     * @param url the url to the php file with proper query params
     */
    @Override
    public void createAccount(String url) {
        HobbyFinderTask task = new HobbyFinderTask();
        task.execute(new String[]{url.toString()});

        // Takes you back to the previous fragment by popping the current fragment out.
        getSupportFragmentManager().popBackStackImmediate();
    }

    /**
     * When passing information from facebook to
     * the create account screen.
     *
     * @param user the facebook user creating an account
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


    /**
     * This methods gets called when the users clicks on Add Hobbies button inside the CreateAccountFragment
     */
    @Override
    public void launchHobbyDialog() {
        DialogFragment fragment = null;
        fragment = new HobbyFragment();
        if (fragment != null)
            fragment.show(getSupportFragmentManager(), "launch");
    }

    /**
     * The inner class to handle AsynTasks
     */
    private class HobbyFinderTask extends AsyncTask<String, Void, String> {

        /**
         * Invoked on the UI thread before the task is executed
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * This step is used to perform background computation
         * @param urls urls passed in that are related to the php files
         * @return string of the result
         */
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();

                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }

                } catch (Exception e) {
                    response = "Unable to go connect, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }


        /**
         * It checks to see if there was a problem with the URL(Network) which is when an
         * exception is caught. It tries to call the parse Method and checks to see if it was successful.
         * If not, it displays the exception.
         *
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            try {
                JSONObject jsonObject = new JSONObject(result);
                String status = (String) jsonObject.get("result");
                if (status.equals("success")) {
                    Toast.makeText(getApplicationContext(), "Success!"
                            , Toast.LENGTH_LONG)
                            .show();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed: "
                                    + jsonObject.get("error")
                            , Toast.LENGTH_LONG)
                            .show();
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Something wrong with the data" +
                        e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}