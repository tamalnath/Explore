package com.tamalnath.explore;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

public class GeneralFragment extends AbstractFragment {

    @Override
    void refresh() {
        grantPermissions();
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Context context = getContext();
        if (context != null) {
            Intent batteryStatus = context.registerReceiver(null, intentFilter);
            if (batteryStatus != null) {
                addBatteryDetails(batteryStatus);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void grantPermissions() {
        adapter.addHeader("Permissions");
        String[] permissions;
        try {
            permissions = getContext().getPackageManager()
                    .getPackageInfo(getContext().getPackageName(), PackageManager.GET_PERMISSIONS)
                    .requestedPermissions;
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, String> map = new TreeMap<>();
        List<String> deniedPermissions = new ArrayList<>();
        for (String permission : permissions) {
            int p = ActivityCompat.checkSelfPermission(getContext(), permission);
            String grant = "Granted";
            if (p == PackageManager.PERMISSION_DENIED) {
                grant = "Denied";
                deniedPermissions.add(permission);
            }
            map.put(permission.substring(19), grant);
        }
        adapter.addMap(map);
        if (!deniedPermissions.isEmpty()) {
            String[] denied = deniedPermissions.toArray(new String[0]);
            ActivityCompat.requestPermissions(getActivity(), denied, this.getId() & 0xFFFF);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if ((this.getId() & 0xFFFF) != requestCode) {
            String message = getString(R.string.permission_denied, Arrays.toString(permissions));
            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
            return;
        }
        List<String> deniedPermissions = new ArrayList<>();
        for (int i = 0; i <= permissions.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                deniedPermissions.add(permissions[i].substring(19));
            }
        }
        String message = getString(R.string.permission_denied, Utils.toString(deniedPermissions, ", ", null, null, null));
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    private void addBatteryDetails(Intent batteryStatus) {
        adapter.addHeader(getString(R.string.fragment_battery));

        boolean present = batteryStatus.getBooleanExtra(BatteryManager.EXTRA_PRESENT, false);
        adapter.addKeyValue(getString(R.string.battery_present), present);
        if (!present) {
            return;
        }

        int key = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        String value = Utils.findConstant(BatteryManager.class, key, "BATTERY_STATUS_(.*)");
        adapter.addKeyValue(getString(R.string.battery_status), value);

        key = batteryStatus.getIntExtra(BatteryManager.EXTRA_HEALTH, -1);
        value = Utils.findConstant(BatteryManager.class, key, "BATTERY_HEALTH_(.*)");
        adapter.addKeyValue(getString(R.string.battery_health), value);

        value = getString(R.string.unknown);
        key = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        if (key > 0) {
            value = Utils.findConstant(BatteryManager.class, key, "BATTERY_PLUGGED_(.*)");
        } else if (key == 0) {
            value = getString(R.string.battery_plugged_unplugged);
        }
        adapter.addKeyValue(getString(R.string.battery_plugged), value);

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        int percent = 100 * level / scale;
        adapter.addKeyValue(getString(R.string.battery_charge), percent + "%");

        int voltage = batteryStatus.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
        adapter.addKeyValue(getString(R.string.battery_voltage), (voltage / 1000f) + "V");

        float temperature = batteryStatus.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1) / 10f;
        adapter.addKeyValue(getString(R.string.battery_temperature), temperature + getString(R.string.sensor_unit_deg));

        String technology = batteryStatus.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);
        adapter.addKeyValue(getString(R.string.battery_technology), technology);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            Boolean low = batteryStatus.getBooleanExtra(BatteryManager.EXTRA_BATTERY_LOW, false);
            adapter.addKeyValue(getString(R.string.battery_low), low);
        }
    }

}
