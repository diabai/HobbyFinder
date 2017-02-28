package diabai.uw.tacoma.edu.hobbyfinder;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import diabai.uw.tacoma.edu.hobbyfinder.user.User;

/**
 * CreateAccountFragment class.
 * <p>
 * This is class automatically launches when a user launches the app.
 *
 * @Author: Ibrahim Diabate, Edgard Solorzano
 * @version: 2.0
 */
public class CreateAccountFragment extends Fragment  {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    /**
     * Constant to check a user is logged in
     */
    public final static String USER_SELECTED = "user_selected";

    /**
     * A listener for this fragment
     */
    private CreateAccountFragmentInteractionListener mListener;

    /**
     * URL to add a user to the Web server
     */
    private final static String USER_ADD_URL
            = "http://cssgate.insttech.washington.edu/~_450bteam1/addUser.php?";

    /**
     * URL to add check if user exists
     */
    private final static String CHECK_IF_USER_EXISTS
            = "http://cssgate.insttech.washington.edu/~_450bteam1/checkIfUserExists.php?";

    /**
     * The user's name
     */
    private TextView mUserName;
    /**
     * The user's email
     */
    private TextView mUserEmail;
    /**
     * The user's gender
     */
    private TextView mUserGender;
    /**
     * The user's hometown
     */
    private TextView mUserHomeTown;
    /**
     * The user's ID
     */
    private String mUserId;


    /**
     * Hobbies selected by the user
     */
    String selectedHobbies;

    /**
     * User hobbies
     */
    String userHobbies;

    /**
     * Default contructor
     */
    public CreateAccountFragment() {
        // Required empty public constructor
    }

    /**
     * This method is generated by default when creating a fragment
     *
     * @param param1
     * @param param2
     * @return CreateAccountFragment
     */
    public static CreateAccountFragment newInstance(String param1, String param2) {
        CreateAccountFragment fragment = new CreateAccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Method generated by default once a fragment is created
     *
     * @param savedInstanceState Bundle object containing information that might have been saved from previous fragments/activity
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }

        selectedHobbies = null;
    }

    /**
     * This method contains the widgets to be displayed once the fragment is launched
     *
     * @param inflater           The layout inflater
     * @param container          The current container
     * @param savedInstanceState Data that might have been saved from a previous fragment or activity
     * @return view The graphical interface of this fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_account, container, false);

        String urlCheckIfExists = buildCheckIfExistsUrl(view);

        mListener.checkIfExists(urlCheckIfExists);

        mUserName = (TextView) view.findViewById(R.id.create_name);
        mUserEmail = (TextView) view.findViewById(R.id.create_email);
        mUserGender = (TextView) view.findViewById(R.id.create_gender);
        mUserHomeTown = (TextView) view.findViewById(R.id.create_hometown);

        Button addAccountButton = (Button) view.findViewById(R.id.add_account_frag_button);
        addAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkIfValid()) {
                    String url = buildUserAddURL(v);
                    mListener.createAccount(url);
                } else {
                    Toast.makeText(v.getContext(), "Please fill out all fields.", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        Button addHobbiesButton = (Button) view.findViewById(R.id.add_hobbies_frag_button);
        addHobbiesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Launches the hobby dialog so user can select his/her hobbies
                mListener.launchHobbyDialog();
            }
        });
        return view;
    }

    /**
     * Fragment lifecycle method containing data from the LogInFragment that we want to use
     * in this fragment once it starts (i.e: the Bundle object)
     */
    @Override
    public void onStart() {
        super.onStart();

        // During startup, check if there are arguments passed to the fragment.
        // onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method
        // below that sets the users text.
        Bundle args = getArguments();
        if (args != null) {
            // Set the textviews to the user passed in
            updateUserView((User) args.getSerializable(USER_SELECTED));
        }

        if ( ((MainActivity)getActivity()).getHobbiesFromFragment()!= null){
          //  Log.i("CreateccountFragmen", ((MainActivity) getActivity()).getHobbiesFromFragment()); //list of hobbies is passed here in the cast
           selectedHobbies = ((MainActivity) getActivity()).getHobbiesFromFragment();
           Log.i("IN MY HOBBIES", ((MainActivity) getActivity()).getHobbiesFromFragment());
        }
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        Bundle args = getArguments();
        if (args != null) {
            // Set the textviews to the user passed in
            updateUserView((User) args.getSerializable(USER_SELECTED));
        }

        if ( ((MainActivity)getActivity()).getHobbiesFromFragment()!= null){
            //  Log.i("CreateccountFragmen", ((MainActivity) getActivity()).getHobbiesFromFragment()); //list of hobbies is passed here in the cast
            selectedHobbies = ((MainActivity) getActivity()).getHobbiesFromFragment();
            Log.i("IN MY HOBBIES", ((MainActivity) getActivity()).getHobbiesFromFragment());
        }
    }


    /**
     * Updates the create account text views with the user's information that is
     * passed in
     *
     * @param user user object with information
     */
    public void updateUserView(User user) {
        mUserId = user.getmId();

        TextView userNameTextView = (TextView) getActivity().findViewById(R.id.create_name);
        userNameTextView.setText(user.getmName());

        TextView userEmailTextView = (TextView) getActivity().findViewById(R.id.create_email);
        userEmailTextView.setText(user.getmEmail());

        TextView userGenderTextView = (TextView) getActivity().findViewById(R.id.create_gender);
        userGenderTextView.setText(user.getmGender());

        TextView userHomeTownTextView = (TextView) getActivity().findViewById(R.id.create_hometown);
        userHomeTownTextView.setText(user.getmHomeTown());
    }



    /**
     * Builds the url for adding a user.
     *
     * @param v the view
     * @return returns the url
     */
    private String buildUserAddURL(View v) {

        StringBuilder sb = new StringBuilder(USER_ADD_URL);

        try {

            String userId = mUserId;
            sb.append("id=");
            sb.append(userId);

            String userName = mUserName.getText().toString();
            sb.append("&name=");
            sb.append(URLEncoder.encode(userName, "UTF-8"));

            String userEmail = mUserEmail.getText().toString();
            sb.append("&email=");
            sb.append(URLEncoder.encode(userEmail, "UTF-8"));

            String userGender = mUserGender.getText().toString();
            sb.append("&gender=");
            sb.append(URLEncoder.encode(userGender, "UTF-8"));

            String userHometown = mUserHomeTown.getText().toString();
            sb.append("&hometown=");
            sb.append(URLEncoder.encode(userHometown, "UTF-8"));

            sb.append("&hobbies=");
            selectedHobbies = ((MainActivity) getActivity()).getHobbiesFromFragment();
            sb.append(URLEncoder.encode(selectedHobbies, "UTF-8"));


        } catch (Exception e) {
            Toast.makeText(v.getContext(), "Something wrong with the url here" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
        return sb.toString();
    }

    /**
     * Builds the url for adding a user.
     *
     * @param v the view
     * @return returns the url
     */
    private String buildCheckIfExistsUrl(View v) {

        StringBuilder sb = new StringBuilder(CHECK_IF_USER_EXISTS);

        try {
            String userId = Profile.getCurrentProfile().getId();
            sb.append("id=");
            sb.append(userId);
            Log.i("UserAddFragment", sb.toString());
        } catch (Exception e) {
            Toast.makeText(v.getContext(), "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
        return sb.toString();
    }

    /**
     * Checks if all textviews are empty or not
     *
     * @return true if none of the textviews are empty, else false
     */
    boolean checkIfValid() {
        return !(mUserId != null && mUserId.isEmpty() ||
                mUserName != null && mUserName.getText().toString().isEmpty() ||
                mUserEmail != null && mUserEmail.getText().toString().isEmpty() ||
                mUserGender != null && mUserGender.getText().toString().isEmpty() ||
                mUserHomeTown != null && mUserHomeTown.getText().toString().isEmpty());
    }

    /**
     * Method for when the fragment is attached to its parent activity
     *
     * @param context the context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CreateAccountFragmentInteractionListener) {
            mListener = (CreateAccountFragmentInteractionListener) context;
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
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface CreateAccountFragmentInteractionListener {
        // Once a user hits the submit account button.
        void createAccount(String url);
        void launchHobbyDialog();
        void checkIfExists(String url);
    }
}
