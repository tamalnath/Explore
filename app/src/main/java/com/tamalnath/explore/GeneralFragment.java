package com.tamalnath.explore;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

public class GeneralFragment extends AbstractFragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        adapter.list.clear();
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Context context = getContext();
        if (context != null) {
            Intent batteryStatus = context.registerReceiver(null, intentFilter);
            if (batteryStatus != null) {
                addBatteryDetails(batteryStatus);
            }
        }
        return super.onCreateView(inflater, container, savedInstanceState);
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
