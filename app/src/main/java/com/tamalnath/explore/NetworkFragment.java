package com.tamalnath.explore;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        Map<String, Map<String, Object>> mapOfMaps = new TreeMap<>();
        for (Network network : connectivityManager.getAllNetworks()) {
            Map<String, Object> map = Utils.findProperties(connectivityManager.getNetworkInfo(network));
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
            map.put(getString(R.string.network_upstream_bandwidth), (capabilities.getLinkUpstreamBandwidthKbps()/1024) + " MBPS");
            map.put(getString(R.string.network_downstream_bandwidth), (capabilities.getLinkDownstreamBandwidthKbps()/1024) + " MBPS");
            StringBuilder trn = new StringBuilder();
            for (Map.Entry<String, Integer> entry : TRANSPORT.entrySet()) {
                if (capabilities.hasTransport(entry.getValue())) {
                    trn.append("\n").append(entry.getKey());
                }
            }
            map.put(getString(R.string.network_transport), trn.substring(1));
            LinkProperties link = connectivityManager.getLinkProperties(network);
            map.put("DNS Servers", Utils.toString(link.getDnsServers()));
            map.put("Domains", link.getDomains());
            map.put("HTTP Proxy", link.getHttpProxy());
            map.put("Link Addresses", Utils.toString(link.getLinkAddresses()));
            map.put("Routes", Utils.toString(link.getRoutes()));
            mapOfMaps.put(network.toString(), map);
        }
        adapter.addTable(mapOfMaps);
        adapter.addHeader("Capabilities");
        mapOfMaps = new TreeMap<>();
        for (Network network : connectivityManager.getAllNetworks()) {
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
            Map<String, Object> map = new TreeMap<>();
            for (Map.Entry<String, Integer> entry : CAPABILITIES.entrySet()) {
                map.put(entry.getKey(), capabilities.hasCapability(entry.getValue()));
            }
            mapOfMaps.put(network.toString(), map);
        }
        adapter.addTable(mapOfMaps);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

}
