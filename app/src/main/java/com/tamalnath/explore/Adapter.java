package com.tamalnath.explore;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    List<Decorator> list = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        list.get(position).decorate(holder);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getViewType();
    }

    void addHeader(final String header) {
        list.add(new Decorator() {
            @Override
            public void decorate(ViewHolder holder) {
                ((TextView) holder.itemView).setText(header);
            }

            @Override
            public int getViewType() {
                return R.layout.view_header;
            }
        });
    }

    void addButton(final String button, final View.OnClickListener listener) {
        list.add(new Decorator() {

            @Override
            public void decorate(ViewHolder holder) {
                ((Button) holder.itemView).setText(button);
                holder.itemView.setOnClickListener(listener);
            }

            @Override
            public int getViewType() {
                return R.layout.view_button;
            }

        });
    }

    void addKeyValue(final Object key, final Object value) {
        list.add(new Decorator() {

            @Override
            public void decorate(ViewHolder viewHolder) {
                TextView keyView = viewHolder.itemView.findViewById(R.id.key);
                TextView valueView = viewHolder.itemView.findViewById(R.id.value);
                keyView.setText(Utils.toString(key));
                valueView.setText(Utils.toString(value, "\n", "", "", null));
            }

            @Override
            public int getViewType() {
                return R.layout.view_key_value;
            }

        });
    }

    void addMap(Map<?, ?> map) {
        for (final Map.Entry<?, ?> entry : map.entrySet()) {
            addKeyValue(entry.getKey(), entry.getValue());
        }
    }

    void addTable(final List<Map<String, Object>> list) {
        this.list.add(new Adapter.Decorator() {
            @Override
            public void decorate(Adapter.ViewHolder viewHolder) {
                TableLayout layout = (TableLayout) viewHolder.itemView;
                Set<String> keys = new TreeSet<>();
                for (Map<String, Object> map : list) {
                    keys.addAll(map.keySet());
                }
                TableRow row;
                if (keys.remove("")) {
                    row = new TableRow(layout.getContext());
                    layout.addView(row);
                    row.addView(new TextView(row.getContext()));
                    for (Map<String, Object> map : list) {
                        TextView textView = new TextView(row.getContext());
                        textView.setText(Utils.toString(map.get("")));
                        textView.setTypeface(Typeface.DEFAULT_BOLD);
                        row.addView(textView);
                    }
                }
                for (String key : keys) {
                    row = new TableRow(layout.getContext());
                    layout.addView(row);
                    TextView textView = new TextView(row.getContext());
                    textView.setText(key);
                    textView.setTypeface(Typeface.DEFAULT_BOLD);
                    row.addView(textView);
                    for (Map<String, Object> map : list) {
                        textView = new TextView(row.getContext());
                        textView.setText(Utils.toString(map.get(key)));
                        row.addView(textView);
                    }
                }
            }

            @Override
            public int getViewType() {
                return R.layout.view_table;
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(View itemView) {
            super(itemView);
        }
    }

    interface Decorator {
        void decorate(ViewHolder viewHolder);

        @LayoutRes
        int getViewType();
    }

}
