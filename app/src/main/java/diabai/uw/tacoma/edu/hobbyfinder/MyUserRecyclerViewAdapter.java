package diabai.uw.tacoma.edu.hobbyfinder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import diabai.uw.tacoma.edu.hobbyfinder.UserFragment.OnListUserListener;
import diabai.uw.tacoma.edu.hobbyfinder.user.User;


import java.util.List;

public class MyUserRecyclerViewAdapter extends RecyclerView.Adapter<MyUserRecyclerViewAdapter.ViewHolder> {

    private final List<User> mValues;
    private final OnListUserListener mListener;

    public MyUserRecyclerViewAdapter(List<User> users, OnListUserListener listener) {
        mValues = users;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_user, parent, false);
        return new ViewHolder(view);
    }

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

        @Override
        public String toString() {
            return super.toString() + "'";
        }
    }
}
