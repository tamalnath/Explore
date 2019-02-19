package com.tamalnath.explore;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.ProxyInfo;

import java.util.Map;

public class NetworkFragment extends AbstractFragment {

    private static final Map<String, Integer> CAPABILITIES = Utils.findConstants(NetworkCapabilities.class, int.class, "NET_CAPABILITY_(.+)");
    private static final Map<String, Integer> TRANSPORT = Utils.findConstants(NetworkCapabilities.class, int.class, "TRANSPORT_(.+)");

    @Override
    void refresh() {
        Context context = getContext();
        if (context == null) {
            return;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        adapter.addHeader("Connectivity Manager");
        adapter.addKeyValue("Active Network Metered", connectivityManager.isActiveNetworkMetered());
        adapter.addKeyValue("Default Network Active", connectivityManager.isDefaultNetworkActive());
        ProxyInfo proxyInfo = connectivityManager.getDefaultProxy();
        if (proxyInfo != null) {
            adapter.addHeader("Proxy");
            adapter.addMap(Utils.findProperties(proxyInfo));
        }
        Network network = connectivityManager.getActiveNetwork();
        if (network == null) {
            return;
        }
        adapter.addHeader("Active Network");
        Map<String, Object> map = Utils.findProperties(connectivityManager.getNetworkInfo(network));
        NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
        map.put("Upstream Bandwidth", (capabilities.getLinkUpstreamBandwidthKbps()/1024) + " MBPS");
        map.put("Downstream Bandwidth", (capabilities.getLinkDownstreamBandwidthKbps()/1024) + " MBPS");
        StringBuilder transport = new StringBuilder();
        for (Map.Entry<String, Integer> entry : TRANSPORT.entrySet()) {
            if (capabilities.hasTransport(entry.getValue())) {
                transport.append('\n').append(entry.getKey());
            }
        }
        map.put("Transport", transport.substring(1));
        StringBuilder capability = new StringBuilder();
        for (Map.Entry<String, Integer> entry : CAPABILITIES.entrySet()) {
            if (capabilities.hasCapability(entry.getValue())) {
                capability.append('\n').append(entry.getKey());
            }
        }
        map.put("Capabilities", capability.substring(1));
        adapter.addMap(map);

        adapter.addHeader("Link Properties");
        adapter.addMap(Utils.findProperties(connectivityManager.getLinkProperties(network)));
    }
}
