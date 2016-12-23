package com.example.tsunman.myactivity;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder> {
    private ArrayList<MyActivity> activities = new ArrayList<>();

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView timeTextView;

        public ViewHolder(View itemView) {
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.activity_item_name);
            timeTextView = (TextView) itemView.findViewById(R.id.activity_item_time);
        }

        public TextView getNameTextView() {
            return nameTextView;
        }

        public TextView getTimeTextView() {
            return timeTextView;
        }
    }

    public ActivityAdapter() {

    }

    // create new views
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new item
        View activity = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item, parent, false);

        return new ViewHolder(activity);
    }

    // replace the contents of a view
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.getNameTextView().setText(activities.get(position).getName());
        holder.getTimeTextView().setText(activities.get(position).getTimeString());
    }

    // append an activity to the array list
    public void add(MyActivity activity) {
        activities.add(activity);

        notifyDataSetChanged();
    }

    // remove all activities from the array list
    public void clear() {
        activities.clear();

        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return activities.size();
    }
}
