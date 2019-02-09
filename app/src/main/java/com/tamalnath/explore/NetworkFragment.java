package com.tamalnath.explore;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.ProxyInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import androidx.annotation.NonNull;

public class NetworkFragment extends AbstractFragment {

    private static final Map<String, Integer> CAPABILITIES = Utils.findConstants(NetworkCapabilities.class, int.class, "NET_CAPABILITY_(.+)");
    private static final Map<String, Integer> TRANSPORT = Utils.findConstants(NetworkCapabilities.class, int.class, "TRANSPORT_(.+)");

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Context context = getContext();
        if (context == null) {
            return null;
        }
        adapter.list.clear();
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        List<Map<String, Object>> list = new ArrayList<>();
        for (Network network : connectivityManager.getAllNetworks()) {
            Map<String, Object> map = Utils.findProperties(connectivityManager.getNetworkInfo(network));
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
            map.put(getString(R.string.network_upstream_bandwidth), (capabilities.getLinkUpstreamBandwidthKbps()/1024) + " MBPS");
            map.put(getString(R.string.network_downstream_bandwidth), (capabilities.getLinkDownstreamBandwidthKbps()/1024) + " MBPS");
            StringBuilder transport = new StringBuilder();
            for (Map.Entry<String, Integer> entry : TRANSPORT.entrySet()) {
                if (capabilities.hasTransport(entry.getValue())) {
                    transport.append('\n').append(entry.getKey());
                }
            }
            map.put(getString(R.string.network_transport), transport.substring(1));
            map.putAll(Utils.findProperties(connectivityManager.getLinkProperties(network)));
            StringBuilder capability = new StringBuilder();
            for (Map.Entry<String, Integer> entry : CAPABILITIES.entrySet()) {
                if (capabilities.hasCapability(entry.getValue())) {
                    capability.append('\n').append(entry.getKey());
                }
            }
            map.put(getString(R.string.network_capability), capability.substring(1));
            list.add(map);
        }
        adapter.addTable(list);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ProxyInfo proxyInfo = connectivityManager.getDefaultProxy();
            if (proxyInfo != null) {
                adapter.addHeader(getString(R.string.network_proxy));
                adapter.addMap(Utils.findProperties(proxyInfo));
            }
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

}
