package com.tamalnath.explore;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Map;

import androidx.annotation.NonNull;

public class BuildFragment extends AbstractFragment {

    private static final Map<String, Object> BUILD = Utils.findConstants(Build.class, null, null);

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        adapter.addHeader("Build");
        adapter.addMap(BUILD);
        adapter.addHeader("Environment variables");
        adapter.addMap(System.getenv());
        adapter.addHeader("System Properties");
        adapter.addMap(System.getProperties());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

}
