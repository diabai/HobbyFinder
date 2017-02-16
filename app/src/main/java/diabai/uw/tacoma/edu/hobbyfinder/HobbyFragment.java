package diabai.uw.tacoma.edu.hobbyfinder;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class HobbyFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    DownloadHobbiesTask task = new DownloadHobbiesTask();


    /**
     * A list of hobbies
     */
    private List<String> hobbyList = new ArrayList<String>();

    /**
     * The URL to query Hobbies from the Web server
     */
    private static final String HOBBIES_URL
            = "http://cssgate.insttech.washington.edu/~_450bteam1/hobbies_list.php?cmd=hobbies";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


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
    // TODO: Rename and change types and number of parameters
    public static HobbyFragment newInstance(String param1, String param2) {
        HobbyFragment fragment = new HobbyFragment();
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
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final List mSelectedItems = new ArrayList();  // Where we track the selected items
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()); // ***********Have to be launched from an activty to work
        // Set the dialog title

        //Creating the hobby list
        DownloadHobbiesTask task = new DownloadHobbiesTask();
        task.execute(new String[]{HOBBIES_URL});

        CharSequence [] mArray = (CharSequence[])hobbyList.toArray();
        builder.setTitle(R.string.pick_hobbies)
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected

                //First parameter should be an array but currently it only works if you use xml
                .setMultiChoiceItems(mArray, null,
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
                // Set the action buttons
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog
                        Resources res = getActivity().getResources();
                        String[] toppings = res.getStringArray(R.array.hobbies_array);
                        StringBuilder builder = new StringBuilder();
                        for (int i = 0; i < mSelectedItems.size(); i++) {
                            builder.append(toppings[(int) mSelectedItems.get(i)]);
                            builder.append(" ");
                        }
                        // ***********Have to be launched from an activty to work
                        Toast.makeText(getActivity(), builder.toString(), Toast.LENGTH_LONG)
                                .show();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        return builder.create();
    }


    /**
     * Converts the jsonArray to something useful
     *
     * @param jArray
     */
    private void convertJsonArray(JSONArray jArray) {
        for (int i = 0; i < jArray.length(); i++) {
            JSONObject obj = null;
            try {
                obj = jArray.getJSONObject(i);
                hobbyList.add((String) obj.get("hobbyName"));
                // items[i] = (String)obj.get("hobbyName");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Gets the hobbies from the database and calls a method within it
     * to parse out the hobbies from json format to a list.
     */
    private class DownloadHobbiesTask extends AsyncTask<String, Void, String> {
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
            // Adding all the hobbies being retrieved
            JSONArray jArray = null;
            try {
                jArray = new JSONArray(response);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            convertJsonArray(jArray); // grabs data and adds to list
            return response;
        }


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
