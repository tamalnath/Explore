package com.tamalnath.explore;

import android.content.Context;
import android.os.Bundle;
import android.telephony.CellInfo;
import android.telephony.CellLocation;
import android.telephony.NeighboringCellInfo;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

public class TelephonyFragment extends AbstractFragment {

    @Override
    @SuppressWarnings("unchecked")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Context context = getContext();
        if (context == null) {
            return null;
        }
        adapter.list.clear();
        TelephonyManager telephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
        Map<String, Object> map = Utils.findProperties(telephonyManager);
        Utils.expand(map, "CallState", TelephonyManager.class, "CALL_STATE_(.+)");
        Utils.expand(map, "DataActivity", TelephonyManager.class, "DATA_ACTIVITY_(.+)");
        Utils.expand(map, "VoiceNetworkType", TelephonyManager.class, "NETWORK_TYPE_(.+)");
        Utils.expand(map, "DataNetworkType", TelephonyManager.class, "NETWORK_TYPE_(.+)");
        Utils.expand(map, "DataState", TelephonyManager.class, "DATA_(\\p{Alpha}+)$");
        Utils.expand(map, "PhoneType", TelephonyManager.class, "PHONE_TYPE_(.+)");
        Utils.expand(map, "SimState", TelephonyManager.class, "SIM_STATE_(.+)");
        List<CellInfo> cellInfos = (List<CellInfo>) map.remove("AllCellInfo");
        List<NeighboringCellInfo> neighboringCellInfos = (List<NeighboringCellInfo>) map.remove("NeighboringCellInfo");
        CellLocation location = (CellLocation) map.remove("CellLocation");
        adapter.addMap(map);
        if (cellInfos != null) {
            List<Map<String, Object>> list = new ArrayList<>();
            for (CellInfo cellInfo : cellInfos) {
                map = Utils.findProperties(cellInfo);
                map.put("TimeStamp", new Date((long) map.get("TimeStamp")));
                Map<String, Object> identity = Utils.findProperties(map.remove("CellIdentity"));
                Integer arfcn = (Integer) identity.get("Arfcn");
                if (arfcn != null && arfcn != Integer.MAX_VALUE) {
                    map.put(getString(R.string.telephony_arfcn), arfcn);
                }
                Integer bsic = (Integer) identity.get("Bsic");
                if (bsic != null && bsic != Integer.MAX_VALUE) {
                    map.put(getString(R.string.telephony_bsic), bsic);
                }
                Integer cid = (Integer) identity.get("Cid");
                if (cid != null && cid != Integer.MAX_VALUE) {
                    map.put(getString(R.string.telephony_cid), cid);
                }
                Integer lac = (Integer) identity.get("Lac");
                if (lac != null && lac != Integer.MAX_VALUE) {
                    map.put(getString(R.string.telephony_lac), lac);
                }
                Integer mcc = (Integer) identity.get("Mcc");
                if (mcc != null && mcc != Integer.MAX_VALUE) {
                    map.put(getString(R.string.telephony_mcc), mcc);
                }
                Integer mnc = (Integer) identity.get("Mnc");
                if (mnc != null && mnc != Integer.MAX_VALUE) {
                    map.put(getString(R.string.telephony_mnc), mnc);
                }
                Integer psc = (Integer) identity.get("Psc");
                if (psc != null && psc != Integer.MAX_VALUE) {
                    map.put(getString(R.string.telephony_psc), psc);
                }
                Integer uarfcn = (Integer) identity.get("Uarfcn");
                if (uarfcn != null && uarfcn != Integer.MAX_VALUE) {
                    map.put(getString(R.string.telephony_arfcn), uarfcn);
                }
                Integer ci = (Integer) identity.get("Ci");
                if (ci != null && ci != Integer.MAX_VALUE) {
                    map.put(getString(R.string.telephony_cid), ci);
                }
                Integer earfcn = (Integer) identity.get("Earfcn");
                if (earfcn != null && earfcn != Integer.MAX_VALUE) {
                    map.put(getString(R.string.telephony_arfcn), earfcn);
                }
                Integer pci = (Integer) identity.get("Pci");
                if (pci != null && pci != Integer.MAX_VALUE) {
                    map.put(getString(R.string.telephony_pci), pci);
                }
                Integer tac = (Integer) identity.get("Tac");
                if (tac != null && tac != Integer.MAX_VALUE) {
                    map.put(getString(R.string.telephony_tac), tac);
                }
                list.add(map);
            }
            adapter.addHeader(getString(R.string.telephony_cell_info));
            adapter.addTable(list);
        }
        if (neighboringCellInfos != null) {
            List<Map<String, Object>> list = new ArrayList<>();
            for (NeighboringCellInfo neighboringCellInfo : neighboringCellInfos) {
                list.add(Utils.findProperties(neighboringCellInfo));
            }
            adapter.addHeader(getString(R.string.telephony_neighboring_cell_info));
            adapter.addTable(list);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

}
