package diabai.uw.tacoma.edu.hobbyfinder;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URLEncoder;

import diabai.uw.tacoma.edu.hobbyfinder.user.User;

/**
 * This is the Create Account fragment. Once a user logs in thru
 * facebook this fragment will show.
 */
public class CreateAccountFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public final static String USER_SELECTED = "user_selected";

    private CreateAccountFragmentInteractionListener mListener;

    private final static String USER_ADD_URL
            = "http://cssgate.insttech.washington.edu/~_450bteam1/addUser.php?";
    private TextView mUserName;
    private TextView mUserEmail;
    private TextView mUserGender;
    private TextView mUserHomeTown;
    private String mUserId;

    public CreateAccountFragment() {
        // Required empty public constructor
    }

    public static CreateAccountFragment newInstance(String param1, String param2) {
        CreateAccountFragment fragment = new CreateAccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_account, container, false);

        // This is the array used for the radius fro zip code drop down
        Spinner dynamicSpinner = (Spinner) view.findViewById(R.id.dynamic_spinner);
        String[] miles = new String[]{"5", "10", "15", "20", "50", "100"};

        // Apply the array of miles into the drop down.
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getBaseContext(),
                android.R.layout.simple_spinner_item, miles);
        dynamicSpinner.setAdapter(adapter);
        mUserName = (TextView) view.findViewById(R.id.create_name);
        mUserEmail = (TextView) view.findViewById(R.id.create_email);
        mUserGender = (TextView) view.findViewById(R.id.create_gender);
        mUserHomeTown = (TextView) view.findViewById(R.id.create_hometown);

        // Set listener on drop down
        dynamicSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.v("item", (String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Call the build URl
        // when using fragments onClick in frags use this
        Button addCourseButton = (Button) view.findViewById(R.id.add_account_frag_button);
        addCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = buildUserAddURL(v);
                mListener.createAccount(url);
            }
        });
        return view;
    }

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
    }

    /*
        Updates the create account text views with the user passed in.
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

            Log.i("UserAddFragment", sb.toString());
        } catch (Exception e) {
            Toast.makeText(v.getContext(), "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
        return sb.toString();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

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
    }
}
