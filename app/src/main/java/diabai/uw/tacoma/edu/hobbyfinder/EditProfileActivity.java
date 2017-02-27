package diabai.uw.tacoma.edu.hobbyfinder;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
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

public class EditProfileActivity extends AppCompatActivity {
    private final static String USER_EDIT_URL
            = "http://cssgate.insttech.washington.edu/~_450bteam1/editUser.php?";
    private final static String USER_INFO =
            "http://cssgate.insttech.washington.edu/~_450bteam1/getUser.php?";
    private TextView mUserIdTextView;
    private TextView mUserNameTextView;
    private TextView mUserEmailTextView;
    private TextView mUserHometownTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        mUserIdTextView = (TextView) findViewById(R.id.edit_user_id);
        mUserNameTextView = (TextView) findViewById(R.id.edit_user_name);
        mUserEmailTextView = (TextView) findViewById(R.id.edit_email);
        mUserHometownTextView = (TextView) findViewById(R.id.edit_hometown);

        /*
            Below calling the get for getting users information to
            put in edit text views
         */
        EditUserTask task = new EditUserTask();
        try {
            task.execute(new String[]{USER_INFO + "id=" + Profile.getCurrentProfile().getId()}).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * Populating the text views with user information
     * @param result string from the json
     * @throws JSONException
     */
    public void populateUserInfo(String result) throws JSONException {
        JSONArray jsonArray = new JSONArray(result);

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            mUserIdTextView.setText(obj.getString("id"));
            mUserNameTextView.setText(obj.getString("name"));
            mUserEmailTextView.setText(obj.getString("email"));
            mUserHometownTextView.setText(obj.getString("hometown"));
        }
    }

    /**
     * Editing user information by submitting
     * to the EditUserTask
     * @param v view
     */
    public void editUser(View v) {
        EditUserTask task = new EditUserTask();
        task.execute(buildEditUserURL());

        // Takes you back to the previous fragment by popping the current fragment out.
        getSupportFragmentManager().popBackStackImmediate();
    }

    private String buildEditUserURL() {
        StringBuilder sb = new StringBuilder(USER_EDIT_URL);
        try {
            String courseId = mUserIdTextView.getText().toString();
            sb.append("id=");
            sb.append(courseId);


            String courseShortDesc = mUserNameTextView.getText().toString();
            sb.append("&name=");
            sb.append(URLEncoder.encode(courseShortDesc, "UTF-8"));


            String courseLongDesc = mUserEmailTextView.getText().toString();
            sb.append("&email=");
            sb.append(URLEncoder.encode(courseLongDesc, "UTF-8"));

            String coursePrereqs = mUserHometownTextView.getText().toString();
            sb.append("&hometown=");
            sb.append(URLEncoder.encode(coursePrereqs, "UTF-8"));
            Log.i("EditUser", sb.toString());
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
        return sb.toString();
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
            if (result != null && !result.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Success!"
                        , Toast.LENGTH_LONG)
                        .show();
            } else {
                Toast.makeText(getApplicationContext(), "Failed : "
                        , Toast.LENGTH_LONG)
                        .show();
            }
        }
    }
}
