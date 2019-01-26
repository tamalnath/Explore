package com.tamalnath.explore;

import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.TreeMap;

public class FontsFragment extends AbstractFragment implements Adapter.Decorator,
        CompoundButton.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener, TextWatcher {

    private static final String TAG = "FontsFragment";

    private Map<String, Typeface> fonts;

    private Switch bold;
    private Switch italic;
    private SeekBar size;
    private EditText sampleText;

    @SuppressWarnings("unchecked")
    public FontsFragment() {
        try {
            String fieldName = "sSystemFontMap";
            Field field = Typeface.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            fonts = new TreeMap((Map<String, Typeface>) field.get(null));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            Log.e(TAG, e.getMessage(), e);
            fonts = Utils.findConstants(Typeface.class, Typeface.class, null);
        }
        draw();
    }

    private void draw() {
        adapter.list.clear();
        adapter.list.add(this);

        final int style = getStyle();
        for (final Map.Entry<String, Typeface> entry : fonts.entrySet()) {
            final Typeface typeface = entry.getValue();
            adapter.list.add(new Adapter.Decorator() {
                @Override
                public void decorate(Adapter.ViewHolder holder) {
                    TextView keyView = holder.itemView.findViewById(R.id.key);
                    TextView valueView = holder.itemView.findViewById(R.id.value);
                    keyView.setText(entry.getKey());
                    valueView.setTypeface(Typeface.create(typeface, style));
                    if (sampleText == null) {
                        valueView.setText(R.string.font_sample);
                    } else {
                        valueView.setText(sampleText.getText());
                    }
                    if (size == null) {
                        valueView.setTextSize(8);
                    } else {
                        valueView.setTextSize(size.getProgress() + 8);
                    }
                }

                @Override
                public int getViewType() {
                    return R.layout.view_key_value;
                }
            });
            adapter.notifyItemRangeChanged(1, fonts.size());
        }
        adapter.notifyDataSetChanged();
    }

    private int getStyle() {
        int boldStyle = (bold != null && bold.isChecked()) ? 0x1 : 0x0;
        int italicStyle = (italic != null && italic.isChecked()) ? 0x2 : 0x0;
        return boldStyle | italicStyle;
    }

    @Override
    public void decorate(Adapter.ViewHolder viewHolder) {
        View parent = viewHolder.itemView;
        bold = parent.findViewById(R.id.bold);
        italic = parent.findViewById(R.id.italic);
        size = parent.findViewById(R.id.size);
        sampleText = parent.findViewById(R.id.sample);

        bold.setOnCheckedChangeListener(this);
        italic.setOnCheckedChangeListener(this);
        size.setOnSeekBarChangeListener(this);
        sampleText.addTextChangedListener(this);
    }

    @Override
    public int getViewType() {
        return R.layout.card_font_filter;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        draw();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        // Don't do anything
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // Don't do anything
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        draw();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // Don't do anything
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        draw();
    }

    @Override
    public void afterTextChanged(Editable s) {
        // Don't do anything
    }

}
