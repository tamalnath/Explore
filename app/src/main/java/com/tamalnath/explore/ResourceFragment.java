package com.tamalnath.explore;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.view.View;

import java.util.Map;

public class ResourceFragment extends AbstractFragment {

    @Override
    void refresh() {
        Resources resources = getResources();
        Configuration configuration = resources.getConfiguration();
        Map<String, Object> map = Utils.findFields(configuration);
        map.putAll(Utils.findProperties(configuration));
        Utils.expand(map, "LayoutDirection", View.class, "LAYOUT_DIRECTION_(.*)");
        Utils.expand(map, "hardKeyboardHidden", Configuration.class, "HARDKEYBOARDHIDDEN_(.*)");
        Utils.expand(map, "keyboard", Configuration.class, "KEYBOARD_(.*)");
        Utils.expand(map, "keyboardHidden", Configuration.class, "KEYBOARDHIDDEN_(.*)");
        Utils.expand(map, "navigation", Configuration.class, "NAVIGATION_(.*)");
        Utils.expand(map, "navigationHidden", Configuration.class, "NAVIGATIONHIDDEN_(.*)");
        Utils.expand(map, "orientation", Configuration.class, "ORIENTATION_(.*)");
        int layout = (int) map.remove("screenLayout");
        String value = Utils.findConstant(Configuration.class, layout & Configuration.SCREENLAYOUT_SIZE_MASK, "SCREENLAYOUT_SIZE_(.*)");
        map.put("Screen Layout Size", value);
        value = Utils.findConstant(Configuration.class, layout & Configuration.SCREENLAYOUT_LONG_MASK, "SCREENLAYOUT_LONG_(.*)");
        map.put("Screen Layout Long", value);
        value = Utils.findConstant(Configuration.class, layout & Configuration.SCREENLAYOUT_LAYOUTDIR_MASK, "SCREENLAYOUT_LAYOUTDIR_(.*)");
        map.put("Screen Layout Direction", value);
        value = Utils.findConstant(Configuration.class, layout & Configuration.SCREENLAYOUT_ROUND_MASK, "SCREENLAYOUT_ROUND_(.*)");
        map.put("Screen Layout Round", value);
        Utils.expand(map, "touchscreen", Configuration.class, "TOUCHSCREEN_(.*)");
        int uiMode = (int) map.remove("uiMode");
        value = Utils.findConstant(Configuration.class, uiMode & Configuration.UI_MODE_TYPE_MASK, "UI_MODE_TYPE_(.*)");
        map.put("UI Mode Type", value);
        value = Utils.findConstant(Configuration.class, uiMode & Configuration.UI_MODE_NIGHT_MASK, "UI_MODE_NIGHT_(.*)");
        map.put("UI Mode Night", value);

        adapter.addHeader("Resources Configuration");
        adapter.addMap(map);
        adapter.addHeader("Resources Display");
        adapter.addMap(Utils.findFields(resources.getDisplayMetrics()));
    }

}
