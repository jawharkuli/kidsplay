// ItemAdapter.java (Generic adapter for all categories)
package com.example.kidsplay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class FruitAdapter extends BaseAdapter {
    private Context context;
    private List<Item> items;

    public FruitAdapter(Context context, List<Item> items) {
        this.context = context;
        this.items = items;
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
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.item_grid, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Item item = items.get(position);
        holder.imageView.setImageResource(item.getImageResId());
        holder.textView.setText(item.getName());

        return convertView;
    }

    private static class ViewHolder {
        ImageView imageView;
        TextView textView;

        ViewHolder(View view) {
            imageView = view.findViewById(R.id.itemImage);
            textView = view.findViewById(R.id.itemName);
        }
    }
}