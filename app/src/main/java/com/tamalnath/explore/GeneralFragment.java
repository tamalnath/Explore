package com.tamalnath.explore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

public class GeneralFragment extends AbstractFragment {

    public GeneralFragment() {
        title = "General";
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recycler_view, container, false);
        return rootView;
    }

}
