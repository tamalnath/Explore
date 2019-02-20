package com.tamalnath.explore;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.ProxyInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.util.Locale;
import java.util.Map;

public class NetworkFragment extends AbstractFragment {

    private static final Map<String, Integer> CAPABILITIES = Utils.findConstants(NetworkCapabilities.class, int.class, "NET_CAPABILITY_(.+)");
    private static final Map<String, Integer> TRANSPORT = Utils.findConstants(NetworkCapabilities.class, int.class, "TRANSPORT_(.+)");

    @Override
    void refresh() {
        final Context context = getContext();
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

        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled()) {
            adapter.addHeader("Wifi Manager");
            map = Utils.findProperties(wifiManager);
            WifiInfo wifiInfo = (WifiInfo) map.remove("ConnectionInfo");
            DhcpInfo dhcpInfo = (DhcpInfo) map.remove("DhcpInfo");
            Utils.expand(map, "WifiApState", WifiManager.class, "WIFI_AP_STATE_(.*)");
            Utils.expand(map, "WifiState", WifiManager.class, "WIFI_STATE_(.*)");
            // TODO: Expand below
            map.remove("ChannelList");
            map.remove("ConfiguredNetworks");
            map.remove("ConfiguredNetworks");
            map.remove("ScanResults");
            map.remove("WifiApConfiguration");
            adapter.addMap(map);
            adapter.addHeader("WiFi Info");
            map = Utils.findProperties(wifiInfo);
            updateIp(map, "IpAddress");
            adapter.addMap(map);
            adapter.addHeader("DHCP");
            map = Utils.findFields(dhcpInfo);
            updateIp(map, "dns1");
            updateIp(map, "dns2");
            updateIp(map, "gateway");
            updateIp(map, "ipAddress");
            updateIp(map, "netmask");
            updateIp(map, "serverAddress");
            adapter.addMap(map);
        }
    }

    private void updateIp(Map<String, Object> map, String key) {
        Integer ip = (Integer) map.get(key);
        if (ip != null) {
            map.put(key, String.format(Locale.getDefault(), "%d.%d.%d.%d", 0xff & ip, 0xff & (ip >> 8), 0xff & (ip >> 16), 0xff & (ip >> 24)));
        }
    }

}
