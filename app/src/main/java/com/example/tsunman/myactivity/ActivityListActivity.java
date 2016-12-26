package com.example.tsunman.myactivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.tsunman.myactivity.ActivityItem.ActivityItem;

import java.util.ArrayList;

/**
 * An activity representing a list of Activities. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ActivityDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ActivityListActivity extends AppCompatActivity {
    private ActivityAdapter mAdapter;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityListActivity.this, CreateActivity.class);
                startActivity(intent);
            }
        });

        View recyclerView = findViewById(R.id.activity_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.activity_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        mAdapter = new ActivityAdapter();

        recyclerView.setAdapter(mAdapter);

        mAdapter.add(new ActivityItem("name", System.currentTimeMillis()));
    }

    public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder> {
        private ArrayList<ActivityItem> activities = new ArrayList<>();

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
        public void onBindViewHolder(final ViewHolder holder, int position) {
            // set the view
            holder.getNameTextView().setText(activities.get(position).getName());
            holder.getTimeTextView().setText(activities.get(position).getTimeString());

            // set the item
            holder.setItem(activities.get(position));

            // set the on-click listener for each item
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(ActivityDetailFragment.ARG_ITEM_NAME, holder.getItemName());
                        ActivityDetailFragment fragment = new ActivityDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.activity_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, ActivityDetailActivity.class);
                        intent.putExtra(ActivityDetailFragment.ARG_ITEM_NAME, holder.getItemName());

                        context.startActivity(intent);
                    }
                }
            });
        }

        // append an activity to the array list
        public void add(ActivityItem activity) {
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


        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            private final TextView nameTextView;
            private final TextView timeTextView;

            private ActivityItem item;

            public ViewHolder(View itemView) {
                super(itemView);

                mView = itemView;

                nameTextView = (TextView) itemView.findViewById(R.id.activity_item_name);
                timeTextView = (TextView) itemView.findViewById(R.id.activity_item_time);
            }

            public TextView getNameTextView() {
                return nameTextView;
            }

            public TextView getTimeTextView() {
                return timeTextView;
            }

            public ActivityItem getItem() {
                return item;
            }

            public String getItemName() {
                return item.getName();
            }

            public void setItem(ActivityItem item) {
                this.item = item;
            }
        }
    }
}
