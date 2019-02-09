package com.tamalnath.explore;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

public class LocationFragment extends AbstractFragment {

    @Override
    void refresh() {
        adapter.list.clear();
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return;
        }
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        List<Map<String, Object>> list = new ArrayList<>();
        List<String> names = locationManager.getAllProviders();
        for (String name : names) {
            LocationProvider provider = locationManager.getProvider(name);
            Map<String, Object> map = Utils.findProperties(provider, "(?:get|is|has|requires|supports)(.*)");
            Utils.expand(map, "Accuracy", Criteria.class, "ACCURACY_(.+)");
            Utils.expand(map, "PowerRequirement", Criteria.class, "POWER_(.+)");
            map.put("", name);
            list.add(map);
        }
        adapter.addTable(list);
    }

}
