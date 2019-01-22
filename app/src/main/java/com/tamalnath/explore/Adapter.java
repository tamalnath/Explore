package com.tamalnath.explore;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    static final int TYPE_HEADER = 1;
    static final int TYPE_BUTTON = 2;
    static final int TYPE_KEY_VALUE = 3;

    private List<Decorator> list = new ArrayList<>();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case TYPE_HEADER:
                return new Holder(inflater.inflate(R.layout.view_header, parent, false));
            case TYPE_BUTTON:
                return new Holder(inflater.inflate(R.layout.view_button, parent, false));
            case TYPE_KEY_VALUE:
                return new KeyValueHolder(inflater.inflate(R.layout.view_key_value, parent, false));
            default:
                throw new RuntimeException("Invalid view type: " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
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

    void add(Decorator decorator) {
        list.add(decorator);
    }

    void addHeader(final String header) {
        list.add(new Decorator() {
            @Override
            public void decorate(RecyclerView.ViewHolder holder) {
                ((TextView) holder.itemView).setText(header);
            }

            @Override
            public int getViewType() {
                return TYPE_HEADER;
            }
        });
    }

    void addButton(final String button, final View.OnClickListener listener) {
        list.add(new Decorator() {
            @Override
            public void decorate(RecyclerView.ViewHolder holder) {
                ((Button) holder.itemView).setText(button);
                holder.itemView.setOnClickListener(listener);
            }

            @Override
            public int getViewType() {
                return TYPE_BUTTON;
            }

        });
    }

    void addMap(Map<?, ?> map) {
        for (final Map.Entry<?, ?> entry : map.entrySet()) {
            list.add(new Decorator() {

                @Override
                public void decorate(RecyclerView.ViewHolder viewHolder) {
                    KeyValueHolder holder = (KeyValueHolder) viewHolder;
                    holder.keyView.setText(Utils.toString(entry.getKey()));
                    holder.valueView.setText(Utils.toString(entry.getValue(), "\n", "", "", null));
                }

                @Override
                public int getViewType() {
                    return TYPE_KEY_VALUE;
                }

            });
        }
    }

    static class Holder extends RecyclerView.ViewHolder {

        Holder(View itemView) {
            super(itemView);
        }
    }

    private static class KeyValueHolder extends RecyclerView.ViewHolder {

        TextView keyView;
        TextView valueView;

        KeyValueHolder(View itemView) {
            super(itemView);
            keyView = itemView.findViewById(R.id.key);
            valueView = itemView.findViewById(R.id.value);
        }
    }

    interface Decorator {
        void decorate(RecyclerView.ViewHolder viewHolder);
        int getViewType();
    }

}
