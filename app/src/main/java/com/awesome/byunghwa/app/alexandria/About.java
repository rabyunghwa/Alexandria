package com.awesome.byunghwa.app.alexandria;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class About extends Fragment {

    public About() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar_about);

        AppCompatActivity activity = (AppCompatActivity) getActivity();

        if (toolbar != null) {
            activity.setSupportActionBar(toolbar);

            final ActionBar actionBar = activity.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
                actionBar.setHomeButtonEnabled(true);

                actionBar.setTitle(getResources().getString(R.string.drawer_about));
            }

        } else {
            // its tablet layout
            if (activity.getSupportActionBar() != null) {
                activity.getSupportActionBar().setTitle(getResources().getString(R.string.drawer_about));
            }

        }

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        activity.setTitle(R.string.about);
    }

}
