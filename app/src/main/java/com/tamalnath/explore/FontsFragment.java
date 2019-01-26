package com.tamalnath.explore;

import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import java.io.File;
import java.util.Map;
import java.util.TreeMap;

public class FontsFragment extends AbstractFragment implements Adapter.Decorator,
        CompoundButton.OnCheckedChangeListener, SeekBar.OnSeekBarChangeListener, TextWatcher {

    private Map<String, Typeface> fonts = new TreeMap<>();
    private Switch bold;
    private Switch italic;
    private SeekBar size;
    private EditText sampleText;

    public FontsFragment() {
        for (final File fontFile : new File("/system/fonts").listFiles()) {
            String fontName = fontFile.getName().split("\\.")[0];
            Typeface typeface = Typeface.createFromFile(fontFile);
            fonts.put(fontName, typeface);
        }
        draw();
    }

    private void draw() {
        adapter.list.clear();
        adapter.list.add(this);
        for (final Map.Entry<String, Typeface> entry : fonts.entrySet()) {
            final Typeface typeface = entry.getValue();
            if ((bold != null && bold.isChecked() != typeface.isBold())
                    || (italic != null && italic.isChecked() != typeface.isItalic())) {
                continue;
            }
            adapter.list.add(new Adapter.Decorator() {
                @Override
                public void decorate(Adapter.ViewHolder holder) {
                    TextView keyView = holder.itemView.findViewById(R.id.key);
                    TextView valueView = holder.itemView.findViewById(R.id.value);
                    keyView.setText(entry.getKey());
                    valueView.setTypeface(typeface);
                    valueView.setText(sampleText.getText());
                    valueView.setTextSize(size.getProgress() + 8);
                }

                @Override
                public int getViewType() {
                    return R.layout.view_key_value;
                }
            });
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void decorate(Adapter.ViewHolder viewHolder) {
        bold = viewHolder.itemView.findViewById(R.id.bold);
        italic = viewHolder.itemView.findViewById(R.id.italic);
        size = viewHolder.itemView.findViewById(R.id.size);
        sampleText = viewHolder.itemView.findViewById(R.id.sample);

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
