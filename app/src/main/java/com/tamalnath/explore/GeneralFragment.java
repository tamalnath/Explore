package com.tamalnath.explore;

import android.content.pm.PackageManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

public class GeneralFragment extends AbstractFragment {

    private int REQUEST_CODE;

    @Override
    void refresh() {
        grantPermissions();
    }

    private void grantPermissions() {
        if (getContext() == null || getActivity() == null) {
            return;
        }
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
            String[] split = permission.split("\\.");
            permission = split[split.length -1];
            map.put(permission, grant);
        }
        adapter.addMap(map);
        if (!deniedPermissions.isEmpty()) {
            String[] denied = deniedPermissions.toArray(new String[0]);
            REQUEST_CODE = this.getId() & 0xFFFF;
            ActivityCompat.requestPermissions(getActivity(), denied, REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != REQUEST_CODE) {
            String message = "Expected Request Code: " + REQUEST_CODE + " but found: " + requestCode;
            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
            return;
        }
        List<String> deniedPermissions = new ArrayList<>();
        for (int i = 0; i <= permissions.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                String[] split = permissions[i].split("\\.");
                String permission = split[split.length -1];
                deniedPermissions.add(permission);
            }
        }
        String message = getString(R.string.permission_denied, Utils.toString(deniedPermissions, ", ", null, null, null));
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }


}
