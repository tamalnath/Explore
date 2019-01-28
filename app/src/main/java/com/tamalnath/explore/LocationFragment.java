package com.tamalnath.explore;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

public class LocationFragment extends AbstractFragment implements View.OnClickListener {

    private static final String TAG = "LocationFragment";
    private FragmentActivity activity;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = getActivity();
        if (activity == null) {
            return null;
        }
        adapter.list.clear();
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        List<String> names = locationManager.getAllProviders();
        for (String name : names) {
            adapter.addHeader(name);
            LocationProvider provider = locationManager.getProvider(name);
            Map<String, Object> map = Utils.findProperties(provider, "(?:get|is|has|requires|supports)(.*)");
            Utils.expand(map, "Accuracy", Criteria.class, "ACCURACY_(.+)");
            Utils.expand(map, "PowerRequirement", Criteria.class, "POWER_(.+)");
            map.remove("Name");
            adapter.addMap(map);
        }
        adapter.addButton(getString(R.string.details), this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        boolean coarse = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        boolean fine = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        if (coarse && fine) {
            launch();
            return;
        }
        String[] perms = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        ActivityCompat.requestPermissions(activity, perms, this.getId());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (this.getId() != requestCode || permissions.length != grantResults.length) {
            String message = getString(R.string.permission_denied, Arrays.toString(permissions));
            Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
            return;
        }
        for (int i = 0; i <= permissions.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                String message = getString(R.string.permission_denied, permissions[i]);
                Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
                return;
            }
        }
        launch();
    }

    private void launch() {
        Log.d(TAG, "TODO: show location.");
    }

}
