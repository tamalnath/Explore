package com.tamalnath.explore;

import android.os.Build;
import android.view.View;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

import androidx.annotation.RequiresApi;

public class TextAppearanceFragment extends AbstractFragment {

    private static final Map<String, Integer> textAppearance = Utils.findConstants(R.style.class, int.class, "TextAppearance_(.*)");

    @Override
    void refresh() {
        for (final Map.Entry<String, Integer> entry: textAppearance.entrySet()) {
            adapter.list.add(new Adapter.Decorator() {
                @Override
                public void decorate(Adapter.ViewHolder viewHolder) {
                    TextView textView = (TextView) viewHolder.itemView;
                    textView.setTextAlignment(View.TEXT_ALIGNMENT_INHERIT);
                    textView.setText(entry.getKey());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        textView.setTextAppearance(entry.getValue());
                    }
                }

                @Override
                public int getViewType() {
                    return R.layout.view_header;
                }
            });
        }
    }
}
