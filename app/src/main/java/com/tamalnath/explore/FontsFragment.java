package com.tamalnath.explore;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FontsFragment extends AbstractFragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        for (final File fontFile : new File("/system/fonts").listFiles()) {
            final String fontName = fontFile.getName().split("\\.")[0];
            adapter.add(new Adapter.Decorator() {

                @Override
                public void decorate(RecyclerView.ViewHolder holder) {
                    Typeface typeface = Typeface.createFromFile(fontFile);
                    ((TextView) holder.itemView).setText(fontName);
                    ((TextView) holder.itemView).setTextSize(16);
                    ((TextView) holder.itemView).setTypeface(typeface);
                }

                @Override
                public int getViewType() {
                    return Adapter.TYPE_HEADER;
                }
            });
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

}
