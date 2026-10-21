package com.honor.tvlauncher;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class AppAdapter extends BaseAdapter {

    private final Context context;
    private final List<AppItem> items;
    private final LayoutInflater inflater;

    public AppAdapter(Context context, List<AppItem> items) {
        this.context = context;
        this.items = items;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.item_app, parent, false);
        }
        AppItem item = items.get(position);
        ImageView icon = view.findViewById(R.id.icon);
        TextView label = view.findViewById(R.id.label);
        icon.setImageDrawable(item.icon);
        label.setText(item.label);
        label.setLines(2);
        label.setEllipsize(android.text.TextUtils.TruncateAt.END);
        return view;
    }
}
