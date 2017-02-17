package diabai.uw.tacoma.edu.hobbyfinder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoggingBehavior;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import diabai.uw.tacoma.edu.hobbyfinder.user.User;


public class LogInFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public final static String USER_SELECTED = "user_selected";

    private String mParam1;
    private String mParam2;

    private ProfilePictureView mProfilePictureView;
    private OnListFragmentInteractionListener mListener;
    private CallbackManager mCallbackManager;
    private TextView mTxtView;
    private Boolean isLoggedIn;
    private Button mHomePageButton;


    public LogInFragment() {
        // Required empty public constructor
    }

    public static LogInFragment newInstance(String param1, String param2) {
        LogInFragment fragment = new LogInFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCallbackManager = CallbackManager.Factory.create();
        isLoggedIn = isLoggedIn();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflating the layout when this fragment is launched
        View v = inflater.inflate(R.layout.fragment_log_in, container, false);

        //If user is logged in display the home page button and add a listener to it
        if (true) { //change back to isUserLoggedIn once it is implemented to check against db
            mHomePageButton = (Button) v.findViewById(R.id.home_page_button);
            mHomePageButton.setVisibility(View.INVISIBLE); //invisible until homepage is created
            mHomePageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // change to another fragment or activity such as user homepage
                    CreateAccountFragment createAccountFragment = new CreateAccountFragment();
                    getFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, createAccountFragment)
                            .addToBackStack(null)
                            .commit();
                }
            });
        }
        return v;
    }


    /**
     * Contains all the facebook button interactions and responses.
     * @param v the view
     * @param savedInstanceState the saved instance
     */
    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        // Initializing
        LoginButton mLoginButton = (LoginButton) v.findViewById(R.id.login_button);
        mTxtView = (TextView) v.findViewById(R.id.log_in_txtView);
        mLoginButton.setFragment(this);

        // Requesting permissions to access the following info from the user's facebook account
        mLoginButton.setReadPermissions(Arrays.asList(
                "public_profile", "user_birthday", "user_friends"));

        // Login button Callback to handle Login related events
        mLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {

            /**
             * On success gets called whe a user logs in for the
             * first time.
             * @param loginResult the result from the login
             */
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();

                // GraphRequest to get the data needed from the facebook Graph
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                FacebookSdk.addLoggingBehavior(LoggingBehavior.REQUESTS);

                                //To see   permissions are enabled
                                Log.v("onCompleted ****", AccessToken.getCurrentAccessToken().getPermissions().toString());

                                JSONObject json = response.getJSONObject();
                                JSONObject hometown = null;
                                try {
                                    hometown = json.getJSONObject("hometown");
                                    final String town = hometown.getString("name");
                                    Log.v("++++Hometown: ++++ ", town);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                // Getting json objects into strings
                                try {

                                    String id = object.getString("id");

                                  /*  The line below will get all the fields available in the JSON object*/
                                   /* mTxtView.setText("Object.names: " + object.names().toString());*/
                                    mProfilePictureView = (ProfilePictureView) getActivity().findViewById(R.id.pro_image);
                                    mProfilePictureView.setProfileId(id);

                                    // Once success is done we just send the user information
                                    // over to create account
                                    mListener.setUser(new User(object.getString("id"),
                                            object.getString("name"),
                                            object.getString("email"),
                                            object.getString("gender"),
                                            "hometown"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                //Saving the stuff in case we need to use it in another fragment
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, name, email, gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                mTxtView.setText("Login cancelled");
                //NEED TO HIDE THAT HOMEPAGE BUTTON WHEN USER LOGS OUT
                mHomePageButton.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onError(FacebookException error) {
                // Log in case we get an exception while logging in
                Log.v("LoginActivity onError", error.getCause().toString());
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Method for when the fragment is attached to its parent activity
     *
     * @param context the context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement CreateAccountFragmentInteractionListener");
        }
    }

    /**
     * Method for when the fragment is detached from its parent activity
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Checks if a user is logged into facebook. We will
     * have to check with DB if user has account as well.
     *
     * @return true if user is logged in
     */
    public boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void setUser(User user);

    }
}
