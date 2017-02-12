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
import android.widget.Spinner;
import android.widget.TextView;

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
        String[] miles = new String[] { "5", "10", "15", "20", "50", "100" };

        // Apply the array of miles into the drop down.
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getBaseContext(),
                android.R.layout.simple_spinner_item, miles);
        dynamicSpinner.setAdapter(adapter);
        TextView mUserName = (TextView) view.findViewById(R.id.create_name);
        TextView mUserEmail = (TextView) view.findViewById(R.id.create_email);
        TextView mUserGender = (TextView) view.findViewById(R.id.create_gender);
        TextView mUserHomeTown = (TextView) view.findViewById(R.id.create_hometown);

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
        TextView userNameTextView = (TextView) getActivity().findViewById(R.id.create_name);
        userNameTextView.setText(user.getmName());

        TextView userEmailTextView = (TextView) getActivity().findViewById(R.id.create_email);
        userEmailTextView.setText(user.getmEmail());

        TextView userGenderTextView = (TextView) getActivity().findViewById(R.id.create_gender);
        userGenderTextView.setText(user.getmGender());

        TextView userHomeTownTextView = (TextView) getActivity().findViewById(R.id.create_hometown);
        userHomeTownTextView.setText(user.getmHomeTown());
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
        public void createAccount(String url);
    }
}
