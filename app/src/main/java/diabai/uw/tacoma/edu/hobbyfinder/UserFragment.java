package diabai.uw.tacoma.edu.hobbyfinder;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import diabai.uw.tacoma.edu.hobbyfinder.UserFragment.OnListUserListener;

import diabai.uw.tacoma.edu.hobbyfinder.user.User;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * User Fragment class
 * This class contains the functionalities required to query user information from the server
 *
 * @author: Ibrahim Diabate, Edgard Solorzano
 */
public class UserFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";

    // TODO: Customize parameters
    private int mColumnCount = 1;

    /**
     * Listener object
     */
    private OnListUserListener mListener;
    /**
     * URL to query user information
     */
    private static final String USERS_URL
            = "http://cssgate.insttech.washington.edu/~_450bteam1/getUsersByHobby.php?hobbies=";
    /**
     * Recycler view object.
     */
    private RecyclerView mRecyclerView;

    /**
     * List of users
     */
    private List<User> mUserList;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public UserFragment() {
    }

    /**
     * Unused auto-generated method
     * @param columnCount
     * @return
     */
    public static UserFragment newInstance(int columnCount) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Lifecycle method first called once fragment is launched
     * @param savedInstanceState data passed from other fragments/activities
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    /**
     * Displayes this fragment's data
     * @param inflater Inflates the fragment
     * @param container the fragment holder
     * @param savedInstanceState data passed to this fragment
     * @return view this fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            // Downloading the users information
            DownloadUsersTask task = new DownloadUsersTask();
            //We search for the first hobby selected
            task.execute(new String[]{USERS_URL + Dashboard.getDashboardHobbies()[0]});
        }
        return view;
    }


    /**
     * Lifecycle method called once the fragment appears to the screen
     * @param context the context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListUserListener) {
            mListener = (OnListUserListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListUserListener {
        void onListFragmentInteraction(User user);
        void sendEmail(String email, String message);
    }

    /**
     * Inner class to download user information from the web service
     */
    private class DownloadUsersTask extends AsyncTask<String, Void, String> {
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
                    response = "Unable to download the list of users, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {

            Log.i("Empty", "Intentional empty");
            // Something wrong with the network or the URL.
            if (result.startsWith("Unable to")) {
                Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }

            mUserList = new ArrayList<User>();
            result = User.parseUserJSON(result, mUserList);
            // Something wrong with the JSON returned.
            if (!result.equals("Success")) {
                Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }

            // Everything is good, show the list of users.
            if (!mUserList.isEmpty()) {
                mRecyclerView.setAdapter(new MyUserRecyclerViewAdapter(mUserList, mListener));
            }
        }
    }
}

