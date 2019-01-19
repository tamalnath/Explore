package com.example.explore;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BuildFragment extends AbstractFragment {

    private static final Map<String, Object> BUILD = Utils.findConstants(Build.class, null, null);

    public BuildFragment() {
        title = "Build";
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.recycler_view, container, false);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Adapter adapter = new Adapter();
        adapter.addHeader("Build");
        adapter.addMap(BUILD);
        adapter.addHeader("Environment variables");
        adapter.addMap(System.getenv());
        adapter.addHeader("System Properties");
        adapter.addMap(System.getProperties());
        recyclerView.setAdapter(adapter);
        return recyclerView;
    }

}
