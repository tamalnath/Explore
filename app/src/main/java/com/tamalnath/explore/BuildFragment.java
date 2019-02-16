package com.tamalnath.explore;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;

import java.text.DateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

public class BuildFragment extends AbstractFragment {

    @Override
    void refresh() {
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Context context = getContext();
        if (context != null) {
            Intent batteryStatus = context.registerReceiver(null, intentFilter);
            if (batteryStatus != null) {
                addBatteryDetails(batteryStatus);
            }
        }
        Map<String, Object> build = Utils.findConstants(Build.class, null, null);
        build.values().removeAll(Collections.singleton(Build.UNKNOWN));
        Long time = (Long) build.get("TIME");
        if (time != null) {
            build.put("TIME", DateFormat.getDateInstance().format(new Date(time)));
        }
        adapter.addHeader("Build");
        adapter.addMap(build);
        adapter.addHeader("Version");
        Map<String, Object> VERSION = Utils.findConstants(Build.VERSION.class, null, null);
        String versionCode = Utils.findConstant(Build.VERSION_CODES.class, Build.VERSION.SDK_INT, null);
        VERSION.put("Version Code", versionCode);
        adapter.addMap(VERSION);
        adapter.addHeader("Environment variables");
        adapter.addMap(System.getenv());
        adapter.addHeader("System Properties");
        adapter.addMap(System.getProperties());
    }

    private void addBatteryDetails(Intent batteryStatus) {
        adapter.addHeader("Battery");

        boolean present = batteryStatus.getBooleanExtra(BatteryManager.EXTRA_PRESENT, false);
        adapter.addKeyValue("Battery Present", present);
        if (!present) {
            return;
        }

        int key = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        String value = Utils.findConstant(BatteryManager.class, key, "BATTERY_STATUS_(.*)");
        adapter.addKeyValue("Battery Status", value);

        key = batteryStatus.getIntExtra(BatteryManager.EXTRA_HEALTH, -1);
        value = Utils.findConstant(BatteryManager.class, key, "BATTERY_HEALTH_(.*)");
        adapter.addKeyValue("Battery Health", value);

        value = getString(R.string.unknown);
        key = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        if (key > 0) {
            value = Utils.findConstant(BatteryManager.class, key, "BATTERY_PLUGGED_(.*)");
        } else if (key == 0) {
            value = "Unplugged";
        }
        adapter.addKeyValue("Battery Plugged", value);

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        int percent = 100 * level / scale;
        adapter.addKeyValue("Battery Charge", percent + "%");

        int voltage = batteryStatus.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
        adapter.addKeyValue("Battery Voltage", (voltage / 1000f) + "V");

        float temperature = batteryStatus.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1) / 10f;
        adapter.addKeyValue("Battery Temperature", temperature + getString(R.string.sensor_unit_deg));

        String technology = batteryStatus.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);
        adapter.addKeyValue("Battery Technology", technology);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            Boolean low = batteryStatus.getBooleanExtra(BatteryManager.EXTRA_BATTERY_LOW, false);
            adapter.addKeyValue("Battery Low", low);
        }
    }

}
