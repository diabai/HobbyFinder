package diabai.uw.tacoma.edu.hobbyfinder;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
    }


    /**
     * This method gets launched one a users clicks the search button
     */
    public void launch(View v) {

        Log.i("Bye", "**");

        DialogFragment fragment = null;

        if (v.getId() == R.id.btn_search) {
            fragment = new HobbyFragment();

        }
        if (fragment != null)
            fragment.show(getSupportFragmentManager(), "search");
    }
}
