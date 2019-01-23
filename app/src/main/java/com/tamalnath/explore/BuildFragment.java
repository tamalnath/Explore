package com.tamalnath.explore;

import android.os.Build;

import java.util.Map;

public class BuildFragment extends AbstractFragment {

    private static final Map<String, Object> BUILD = Utils.findConstants(Build.class, null, null);

    public BuildFragment() {
        adapter.addHeader("Build");
        adapter.addMap(BUILD);
        adapter.addHeader("Environment variables");
        adapter.addMap(System.getenv());
        adapter.addHeader("System Properties");
        adapter.addMap(System.getProperties());
    }

}
