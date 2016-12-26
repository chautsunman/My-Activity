package com.example.tsunman.myactivity;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tsunman.myactivity.ActivityItem.ActivityItem;

/**
 * A fragment representing a single Activity detail screen.
 * This fragment is either contained in a {@link ActivityListActivity}
 * in two-pane mode (on tablets) or a {@link ActivityDetailActivity}
 * on handsets.
 */
public class ActivityDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    public static final String ARG_ITEM_NAME = "item_name";

    private ActivityItem mItem;
    private String mItemName;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ActivityDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_NAME)) {
            mItemName = getArguments().getString(ARG_ITEM_NAME);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItemName);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_detail, container, false);

        return rootView;
    }
}
