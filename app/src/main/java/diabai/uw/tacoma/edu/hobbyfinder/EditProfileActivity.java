package diabai.uw.tacoma.edu.hobbyfinder;

import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

import static diabai.uw.tacoma.edu.hobbyfinder.CreateAccountFragment.*;

/**
 * EditProfile Activity
 * This activity contains the features available to a user wishing to edit his/her profile information
 */
public class EditProfileActivity extends AppCompatActivity implements
        CreateAccountFragmentInteractionListener {
    private final static String USER_EDIT_URL
            = "http://cssgate.insttech.washington.edu/~_450bteam1/editUser.php?";
    private final static String USER_INFO =
            "http://cssgate.insttech.washington.edu/~_450bteam1/getUser.php?";
    /**
     * User ID textView
     */
    private String userId;

    private TextView mUserIdTextView;
    /**
     * User name textView
     */
    private TextView mUserNameTextView;
    /**
     * User email textView
     */
    private TextView mUserEmailTextView;
    /**
     * User hometown textView
     */
    private TextView mUserHometownTextView;
    /**
     * User hobbies textView
     */
    private TextView mUserHobbiesTextView;

    /**
     * User hobbies
     */
    private String hobbiesFromFragment;


    /**
     * Lifecycle method first called once the activity starts.
     * @param savedInstanceState data that might have been passed from another afctivity/fragment
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        //mUserIdTextView = (TextView) findViewById(R.id.edit_user_id);
        mUserNameTextView = (TextView) findViewById(R.id.edit_user_name);
        mUserEmailTextView = (TextView) findViewById(R.id.edit_email);
        mUserHometownTextView = (TextView) findViewById(R.id.edit_hometown);
        mUserHobbiesTextView = (TextView) findViewById(R.id.edit_hobbies);

        Toast.makeText(getApplicationContext(),
                "Edit profile", Toast.LENGTH_SHORT)
                .show();

        /*
            Gets users information to
            put in edit text views
         */
        EditUserTask task = new EditUserTask();
        try {
            task.execute(new String[]{USER_INFO + "id=" + Profile.getCurrentProfile().getId()}).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        Button addHobbiesButton = (Button) findViewById(R.id.add_hobbies_frag_button);
        addHobbiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Launches the hobby dialog so user can select his/her hobbies
                launchHobbyDialog();
            }
        });
    }

    /**
     * Populating the text views with user information
     *
     * @param result string from the json
     * @throws JSONException Exception thrown when something goes wrong while updating a user profile
     */
    public void populateUserInfo(String result) throws JSONException {
        JSONArray jsonArray = new JSONArray(result);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            userId = obj.getString("id");
            mUserNameTextView.setText(obj.getString("name"));
            mUserEmailTextView.setText(obj.getString("email"));
            mUserHometownTextView.setText(obj.getString("hometown"));
            mUserHobbiesTextView.setText(obj.getString("hobbies").replaceAll(", $", ""));
        }
    }

    /**
     * Editing user information by submitting
     * to the EditUserTask
     *
     * @param v view object
     */
    public void editUser(View v) {
        EditUserTask task = new EditUserTask();
        if (!checkData()) {
            task.execute(buildEditUserURL());
        } else {
            Toast.makeText(getApplicationContext(),
                    "Make sure all fields are filled out (at least 1 hobby is selected)", Toast.LENGTH_LONG)
                    .show();
        }
        // Takes you back to the previous fragment by popping the current fragment out.
        getSupportFragmentManager().popBackStackImmediate();
    }

    /**
     * Setter for user hobbies
     * @param s the new hobbies
     */
    public void setHobbies(String s) {
        hobbiesFromFragment = s;
    }

    /**
     * Getter for user hobbies
     * @return the user's hobbies
     */
    public String getHobbiesFromFragment() {
        return hobbiesFromFragment;
    }

    /**
     * Checks validity of data passed
     * @return true if valid otherwise false
     */
    public boolean checkData() {
        return
         mUserNameTextView.getText().toString().isEmpty() || mUserNameTextView.getText() == null ||
         mUserEmailTextView.getText().toString().isEmpty() || mUserEmailTextView.getText() == null ||
         mUserHometownTextView.getText().toString().isEmpty() || mUserHometownTextView.getText() == null ||
         getHobbiesFromFragment() == null;
    }

    /**
     * Builds the url for submiting editing of profile
     *
     * @return the url string
     */
    private String buildEditUserURL() {
        StringBuilder sb = new StringBuilder(USER_EDIT_URL);
        try {
            sb.append("id=");
            sb.append(userId);

            String userName = mUserNameTextView.getText().toString();
            sb.append("&name=");
            sb.append(URLEncoder.encode(userName, "UTF-8"));

            String userEmail = mUserEmailTextView.getText().toString();
            sb.append("&email=");
            sb.append(URLEncoder.encode(userEmail, "UTF-8"));

            String userHomeTown = mUserHometownTextView.getText().toString();
            sb.append("&hometown=");
            sb.append(URLEncoder.encode(userHomeTown, "UTF-8"));

            sb.append("&hobbies=");
            sb.append(URLEncoder.encode(getHobbiesFromFragment(), "UTF-8"));

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Something wrong with the url inside EditProfile"
                    + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
        return sb.toString();
    }

    /**
     * Used to prevent crash on nougat devices
     *
     * @param outState a bundle object
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // below line to be commented to prevent crash on nougat.
        // http://blog.sqisland.com/2016/09/transactiontoolargeexception-crashes-nougat.html
        //super.onSaveInstanceState(outState);
    }

    /**
     * Not really used in here but needed to implement
     *
     * @param url url to create account
     */
    @Override
    public void createAccount(String url) {
    }

    @Override
    public void launchHobbyDialog() {
        DialogFragment fragment = new HobbyFragment();
        fragment.show(getSupportFragmentManager(), "launch");
    }

    /**
     * Async task that gets user information to display on edit texts
     * and also can handle the submission of editing.
     */
    private class EditUserTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

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
                    response = "Unable to edit, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            try {
                populateUserInfo(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return response;
        }


        /**
         * It checks to see if there was a problem with the URL(Network) which is when an
         * exception is caught. It tries to call the parse Method and checks to see if it was successful.
         * If not, it displays the exception.
         *
         * @param result string
         */
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                String status = (String) jsonObject.get("result");
                Log.w("EditProfileActivity", "Grabbed user info successfully");
                if ("User edited success".equals(status)) {

                    finish();
                    startActivity(getIntent());
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Log.w("EditProfileActivity", "Failed to grab user info");
            }
        }
    }
}
