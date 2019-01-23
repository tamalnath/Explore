package com.tamalnath.explore;

import android.graphics.Typeface;
import android.widget.TextView;

import java.io.File;

public class FontsFragment extends AbstractFragment {

    public FontsFragment() {
        for (final File fontFile : new File("/system/fonts").listFiles()) {
            adapter.list.add(new Adapter.Decorator() {

                @Override
                public void decorate(Adapter.ViewHolder holder) {
                    String fontName = fontFile.getName().split("\\.")[0];
                    Typeface typeface = Typeface.createFromFile(fontFile);
                    TextView itemView = (TextView) holder.itemView;
                    itemView.setText(fontName);
                    itemView.setTextSize(16);
                    itemView.setTypeface(typeface);
                }

                @Override
                public int getViewType() {
                    return R.layout.view_header;
                }
            });
        }
    }

}
