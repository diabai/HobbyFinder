package diabai.uw.tacoma.edu.hobbyfinder;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Hobby Fragment class.
 * This class construct the DialogFragment that is create once a user chooses hobbies to add to his/her profile
 *
 * @author Ibrahim Diabate, Edgard Solorzano
 * @version 2.0
 */
public class HobbyFragment extends DialogFragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    /**
     * A list of hobbies
     */
    private List<String> mHobbyList = new ArrayList<String>();

    /**
     * CharSequence array for the hobbies
     */
    private CharSequence[] mArray;

    /**
     * The URL to query Hobbies from the Web server
     */
    private static final String HOBBIES_URL
            = "http://cssgate.insttech.washington.edu/~_450bteam1/hobbies_list.php?cmd=hobbies";

    /**
     * URL to add a user hobbies
     */
    private final static String USER_ADD_HOBBIES
            = "http://cssgate.insttech.washington.edu/~_450bteam1/user_hobbies.php?";


    /**
     * The fragment listener
     */
    UserHobbiesListener mHobbiesListener;
    /*
     * Fields automatically generated once fragment is created
     */
    private String mParam1;
    private String mParam2;

    /**
     * Default constructor
     */
    public HobbyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HobbyFragment.
     */
    public static HobbyFragment newInstance(String param1, String param2) {
        HobbyFragment fragment = new HobbyFragment();
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        try {
            mHobbiesListener = (UserHobbiesListener) getTargetFragment();
        } catch (Exception e) {
            throw new ClassCastException("Calling Fragment must implement UserHobbiesListener");
        }
    }


    /**
     * This method contains the widgets to be displayed once the dialog is launched
     *
     * @param savedInstanceState Data that might have been saved from a previous fragment or activity
     * @return view The graphical interface of this fragment
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final List mSelectedItems = new ArrayList();  // Where we track the selected items
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //Creating the hobby list by sending request to the server
        DownloadHobbiesTask task = new DownloadHobbiesTask();

        //Creating the hobby list by calling the async task
        try {
            task.execute(new String[]{HOBBIES_URL}).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        mArray = mHobbyList.toArray(new CharSequence[mHobbyList.size()]);
        /*
            If coming from dashboard then let user select only one hobby at a time.
        */
        if (getContext() instanceof Dashboard) {
            builder.setTitle("Select one hobby");  //Searching for one hobby at the time
            builder.setSingleChoiceItems(mArray, 0, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ListView lw = ((AlertDialog) dialog).getListView();
                    Object checkedItem = lw.getAdapter().getItem(lw.getCheckedItemPosition());
                    mSelectedItems.add(checkedItem.toString());
                }
            })
                    // Set the action buttons for this dialog fragment
                    .setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            String[] splitHobbies = (mSelectedItems.isEmpty() ?
                                    mArray[0] : mSelectedItems.get(0))
                                    .toString().split("\\s*,\\s*");

                            //Passing the array of selected hobbies to Dashboard class so that
                            // I can retrieve there later in UserFragment
                            ((Dashboard) getActivity()).setDashboardHobbies(splitHobbies);

                            //Launching the UserFinderActivity activity (parent of UserFragment list)
                            Intent intent = new Intent(getActivity(), UserFinderActivity.class);
                            //Passing the hobbies to UserFinderActivity class
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            //Left empty on purpose. Does not affect this phase
                        }
                    });
        } else {
            builder.setTitle(R.string.pick_hobbies);
            builder.setMultiChoiceItems(mArray, null,
                    new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which,
                                            boolean isChecked) {
                            if (isChecked) {
                                // If the user checked the item, add it to the selected items
                                mSelectedItems.add(which);
                            } else if (mSelectedItems.contains(which)) {
                                // Else, if the item is already in the array, remove it
                                mSelectedItems.remove(Integer.valueOf(which));
                            }
                        }
                    })
                    // Set the action buttons for this dialog fragment
                    .setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK, so save the mSelectedItems results somewhere
                            // or return them to the component that opened the dialog
                            StringBuilder builder = new StringBuilder();
                            String userHobbies = "";
                            for (int i = 0; i < mSelectedItems.size(); i++) {
                                builder.append(mArray[(int) mSelectedItems.get(i)]);
                                builder.append(", ");
                            }
                            if (getContext() instanceof EditProfileActivity) {
                                Toast.makeText(getActivity(), builder.toString(), Toast.LENGTH_LONG)
                                        .show();
                                //EditActivity launched it
                                ((EditProfileActivity) getActivity()).setHobbies(builder.toString());
                            } else {
                                Toast.makeText(getActivity(), builder.toString(), Toast.LENGTH_LONG)
                                        .show();
                                //MainActivity launched it
                                ((MainActivity) getActivity()).setHobbies(builder.toString());
                            }
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            //Left empty on purpose. Does not affect this phase
                        }
                    });
        }
        // Specify the list array, the items to be selected by default (null for none),
        // and the listener through which to receive callbacks when items are selected
        return builder.create();
    }

    /**
     *The fragment's listener interface.
     */
    public interface UserHobbiesListener {
        void passHobbies(String theUserHobbies);
    }

    /**
     * Converts the jsonArray to something useful
     *
     * @param jArray the JSONArray
     */
    private void convertJsonArray(JSONArray jArray) {
        for (int i = 0; i < jArray.length(); i++) {
            JSONObject obj;
            try {
                obj = jArray.getJSONObject(i);
                mHobbyList.add((String) obj.get("hobbyName"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * DownloadHobbiesTask inner class.
     *
     * Gets the hobbies from the database and calls a method within it
     * to parse out the hobbies from json format to a list.
     */
    private class DownloadHobbiesTask extends AsyncTask<String, Void, String> {

        /**
         * @param urls array of URL.
         * @return response the string containing the result from this method call.
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
                    response = "Unable to download the list of hobbies, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            /* Adding all the hobbies being retrieved
                has to be called in this moment to not create
                issues with async calls timing.
             */
            JSONArray jArray = null;
            try {
                jArray = new JSONArray(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            convertJsonArray(jArray); // grabs data and adds to list
            return response;
        }


        /**
         * Gets called once the job is finished.
         *
         * @param result the result string
         */
        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            if (result.startsWith("Unable to")) {
                Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
            }
        }
    }
}
