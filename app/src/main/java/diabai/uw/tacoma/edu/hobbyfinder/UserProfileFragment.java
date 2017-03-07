package diabai.uw.tacoma.edu.hobbyfinder;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Profile;
import com.facebook.login.widget.ProfilePictureView;

import diabai.uw.tacoma.edu.hobbyfinder.user.User;

import static diabai.uw.tacoma.edu.hobbyfinder.UserFragment.*;


/**
 * This fragment is the profile with a users info
 * after the user using the application searches for
 * other users with a hobby.
 *
 * @author Edgard S, Ibrahim D
 */
public class UserProfileFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public final static String USER_ITEM_SELECTED = "user_selected";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private User mUser;
    private OnListUserListener mListener;


    public UserProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserProfileFragment newInstance(String param1, String param2) {
        UserProfileFragment fragment = new UserProfileFragment();
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle args = getArguments();
        if (args != null) {
            // Set article based on argument passed in
            mUser = (User) args.getSerializable(USER_ITEM_SELECTED);
            updateUserView((User) args.getSerializable(USER_ITEM_SELECTED));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        // when using fragments onClick in frags use this
        Button sendEmailButton = (Button) view.findViewById(R.id.email_user);
        sendEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView emailBodyTextView = (TextView) getActivity().findViewById(R.id.email_body_message);
                if (!emailBodyTextView.getText().toString().isEmpty()) {
                    mListener.sendEmail(mUser.getmEmail(), emailBodyTextView.getText().toString());
                } else {
                    Toast.makeText(v.getContext(), "Please fill out a message to send.",
                            Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListUserListener) {
            mListener = (OnListUserListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Updates the text views with the user's information that is
     * passed in
     *
     * @param user user object with information
     */
    public void updateUserView(User user) {
        ProfilePictureView mProfilePictureView = (ProfilePictureView) getActivity().findViewById(R.id.profile_image_profile_frag);
        mProfilePictureView.setProfileId(user.getmId());

        TextView userNameTextView = (TextView) getActivity().findViewById(R.id.profile_user_name);
        userNameTextView.setText(user.getmName());
        mUser.setmName(user.getmName());

        mUser.setmEmail(user.getmEmail());

        TextView userHometownTextView = (TextView) getActivity().findViewById(R.id.profile_hometown);
        userHometownTextView.setText(user.getmHomeTown());

        TextView userGenderTextView = (TextView) getActivity().findViewById(R.id.profile_gender);
        userGenderTextView.setText(user.getmGender());
        mUser.setmGender(user.getmGender());
    }

    /**
     * The user profile listener
     */
    public interface UserProfileListener {
        void userProfileListener(Uri uri);
    }
}
