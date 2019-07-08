package com.tamalnath.explore;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import androidx.fragment.app.FragmentActivity;

public class LocationFragment extends AbstractFragment {

    private FragmentActivity activity;
    private List<Map<String, Object>> locations = new ArrayList<>();

    @Override
    void refresh() {
        adapter.list.clear();
         activity = getActivity();
        if (activity == null) {
            return;
        }
        boolean coarse = activity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean fine = activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        if (!(coarse && fine)) {
            return;
        }
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        List<Map<String, Object>> providers = new ArrayList<>();
        List<String> names = locationManager.getAllProviders();
        for (String name : names) {
            Map<String, Object> provider = Utils.findProperties(locationManager.getProvider(name), "(?:get|is|has|requires|supports)(.*)");
            Utils.expand(provider, "Accuracy", Criteria.class, "ACCURACY_(.+)");
            Utils.expand(provider, "PowerRequirement", Criteria.class, "POWER_(.+)");
            provider.put("", name);
            providers.add(provider);
            addLocation(locationManager.getLastKnownLocation(name));
            locationManager.requestSingleUpdate(name, new LocationUpdater(), null);
        }
        adapter.addHeader("Provider");
        adapter.addTable(providers);
        adapter.addHeader("Location");
        adapter.addTable(locations);
    }

    private void addLocation(Location location) {
        if (location == null) {
            return;
        }
        Map<String, Object> map = Utils.findProperties(location);
        map.remove("Extras");
        map.remove("ElapsedRealtimeNanos");
        Long time = (Long) map.remove("Time");
        if (time != null) {
            map.put("Time", DateFormat.getTimeInstance().format(new Date(time)));
        }
        String provider = location.getProvider();
        map.put("", provider);
        for (int i = 0; i <locations.size(); i++) {
            if (provider.equals(locations.get(i).get(""))) {
                locations.remove(i);
                break;
            }
        }
        locations.add(map);
    }

    class LocationUpdater implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            String msg = location.getProvider() + " location: " + location.getLatitude() + ", " + location.getLongitude();
            Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
            adapter.list.remove(adapter.list.size() - 1);
            adapter.addTable(locations);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            String sts = Utils.findConstant(LocationProvider.class, status, null);
            Toast.makeText(activity, provider + " : " + sts, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(activity, provider + " disabled", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(activity, provider + " disabled", Toast.LENGTH_LONG).show();
        }
    }
}
