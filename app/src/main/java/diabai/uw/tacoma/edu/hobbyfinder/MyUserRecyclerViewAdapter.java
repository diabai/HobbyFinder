package diabai.uw.tacoma.edu.hobbyfinder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import diabai.uw.tacoma.edu.hobbyfinder.UserFragment.OnListUserListener;
import diabai.uw.tacoma.edu.hobbyfinder.user.User;


import java.util.List;


/**
 * RecyclerView Class
 *
 * @Aurhor: Ibrahim Diabate, Edgard S.
 * @version: 2.0
 */
public class MyUserRecyclerViewAdapter extends RecyclerView.Adapter<MyUserRecyclerViewAdapter.ViewHolder> {

    /**
     * List of users.
     */
    private final List<User> mValues;
    /**
     * Listener object.
     */
    private final OnListUserListener mListener;

    /**
     * Construct
     * @param users List of users
     * @param listener the listener
     */
    public MyUserRecyclerViewAdapter(List<User> users, OnListUserListener listener) {
        mValues = users;
        mListener = listener;
    }

    /**
     * Inflates the fragment requested
     * @param parent the fragment
     * @param viewType unused auto-generated parameter
     * @return new ViewHolder object - the data
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_user, parent, false);
        return new ViewHolder(view);
    }


    /**
     * Populates the list of uesrs
     * @param holder the holder object
     * @param position the index being populated
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mNameView.setText(mValues.get(position).getmName());
        holder.mGender.setText(mValues.get(position).getmGender());
        holder.mHometown.setText(mValues.get(position).getmHomeTown());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    /**
     * Gets the count of users passed.
     * @return
     */
    @Override
    public int getItemCount() {
        return mValues.size();
    }

    /**
     * Inner class view holder
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mNameView;
        public final TextView mGender;
        public final TextView mHometown;


        public User mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.name_list_frag);
            mGender = (TextView) view.findViewById(R.id.gender_list_frag);
            mHometown = (TextView) view.findViewById(R.id.hometown_list_frag);
        }

        /**
         * To string method.
         * @return a string representation of this object.
         */
        @Override
        public String toString() {
            return super.toString() + "'";
        }
    }
}
