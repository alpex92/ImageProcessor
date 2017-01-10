package com.alpex.imageprocessor.model.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.alpex.imageprocessor.R;

/**
 * Created by Alpex on 28.11.2014.
 */

public class SourceListAdapter extends ArrayAdapter<String> {

    private final String[] text;
    private final int[] imageId;
    private final LayoutInflater inflater;

    private static class ViewHolder {
        public TextView textView;
        public ImageView imageView;
    }

    public SourceListAdapter(Context context, LayoutInflater inflater, String[] text, int[] imageId) {

        super(context, R.layout.dialog_entry, text);
        this.inflater = inflater;
        this.text = text;
        this.imageId = imageId;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.dialog_entry, parent, false);
            holder.textView = (TextView) view.findViewById(R.id.dialog_text);
            holder.imageView = (ImageView) view.findViewById(R.id.dialog_image);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.textView.setText(text[position]);
        holder.imageView.setImageResource(imageId[position]);
        return view;
    }
}