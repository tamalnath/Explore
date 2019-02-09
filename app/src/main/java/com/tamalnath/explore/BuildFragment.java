package com.tamalnath.explore;

import android.os.Build;

import java.util.Collections;
import java.util.Map;

public class BuildFragment extends AbstractFragment {

    private static final Map<String, Object> VERSION = Utils.findConstants(Build.VERSION.class, null, null);

    @Override
    void refresh() {
        Map<String, Object> build = Utils.findConstants(Build.class, null, null);
        build.values().removeAll(Collections.singleton(Build.UNKNOWN));
        adapter.addHeader("Build");
        adapter.addMap(build);
        adapter.addHeader("Version");
        adapter.addMap(VERSION);
        adapter.addHeader("Environment variables");
        adapter.addMap(System.getenv());
        adapter.addHeader("System Properties");
        adapter.addMap(System.getProperties());
    }
}
