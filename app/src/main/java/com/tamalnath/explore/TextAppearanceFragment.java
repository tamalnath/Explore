package com.tamalnath.explore;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Map;

public class TextAppearanceFragment extends AbstractFragment implements View.OnClickListener {

    private static final Map<String, Integer> textAppearance = Utils.findConstants(R.style.class, int.class, "TextAppearance_(.*)");
    private EditText sampleText;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ConstraintLayout layout = (ConstraintLayout) inflater.inflate(R.layout.fragment_text_appearance, container, false);
        sampleText = layout.findViewById(R.id.sample);
        RecyclerView recyclerView = layout.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        refresh();
        return layout;
    }

    @Override
    void refresh() {
        for (final Map.Entry<String, Integer> entry: textAppearance.entrySet()) {
            adapter.list.add(new Adapter.Decorator() {
                @Override
                public void decorate(Adapter.ViewHolder viewHolder) {
                    TextView textView = (TextView) viewHolder.itemView;
                    textView.setTextAlignment(View.TEXT_ALIGNMENT_INHERIT);
                    textView.setText(entry.getKey());
                    textView.setOnClickListener(TextAppearanceFragment.this);
                    textView.setTextAppearance(entry.getValue());
                }

                @Override
                public int getViewType() {
                    return R.layout.view_header;
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        TextView textView = (TextView) v;
        Integer appearance = textAppearance.get(textView.getText().toString());
        if (appearance != null) {
            sampleText.setTextAppearance(appearance);
        }
    }
}
