package com.alpex.imageprocessor.model.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.alpex.imageprocessor.R;
import com.alpex.imageprocessor.model.commands.ProcessCommand;
import com.alpex.imageprocessor.model.components.*;
import com.alpex.imageprocessor.model.listeners.CommandAddedListener;
import com.alpex.imageprocessor.model.listeners.CommandCompletedListener;
import com.alpex.imageprocessor.model.listeners.CommandProgressListener;
import com.alpex.imageprocessor.model.listeners.CommandRemovedListener;

/**
 * Created by Alpex on 26.11.2014.
 */
public class CommandsAdapter extends BaseAdapter implements CommandCompletedListener, CommandProgressListener {

    private LayoutInflater inflater;
    private ImageProcessor imageProcessor;
    private int alternateColor;
    private Context context;
    private ListView listView;

    @Override
    public void onCommandCompleted(ProcessCommand cmd) {
        notifyDataSetChanged();
    }

    @Override
    public void onProgressUpdated(ProcessCommand cmd, int progress) {
        // Update view manually instead of calling notifyDataSetChanged
        // to prevent performance issues
        refreshListItem(cmd);
    }

    private static class ViewHolder {
        ImageView imageView;
        ProgressBar progressBar;
        TextView progressText;

        public void updateViews(ProcessCommand cmd) {
            if (cmd.isReady()) {
                progressBar.setVisibility(View.INVISIBLE);
                imageView.setVisibility(View.VISIBLE);
                progressText.setVisibility(View.INVISIBLE);
                imageView.setImageBitmap(cmd.getResult());
            } else {
                imageView.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(cmd.getProgress());
                progressText.setVisibility(View.VISIBLE);

                String format = cmd.getDescription();

                if (format != null) {
                    progressText.setText(String.format(format, cmd.getProgress()));
                }
            }
        }
    }

    public CommandsAdapter(Context context, LayoutInflater inflater, ImageProcessor imageProcessor, ListView listView) {

        this.listView = listView;
        this.context = context;
        this.inflater = inflater;
        this.imageProcessor = imageProcessor;
        alternateColor = context.getResources().getColor(R.color.light_grey);

        imageProcessor.setCommandAddedListener(new CommandAddedListener() {
            @Override
            public void OnCommandAdded(ProcessCommand cmd) {
              prepareCommand(cmd);
              notifyDataSetChanged();
            }
        });
        imageProcessor.setCommandRemovedListener(new CommandRemovedListener() {
            @Override
            public void onCommandRemoved(ProcessCommand cmd) {
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getCount() {
        return imageProcessor.getCommands().size();
    }

    @Override
    public Object getItem(int i) {
        return imageProcessor.getCommands().get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        // Get command by index
        ProcessCommand cmd = imageProcessor.getCommands().get(i);

        ViewHolder holder;

        if (convertView == null) {

            holder = new ViewHolder();

            convertView = inflater.inflate(R.layout.list_entry, parent, false);
            holder.imageView = (ImageView) convertView.findViewById(R.id.listEntryImageView);
            holder.progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
            holder.progressText = (TextView) convertView.findViewById(R.id.progressText);

            // Save ViewHolder reference
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        cmd.setCommandProgressListener(this);
        cmd.setCommandCompletedListener(this);

        // Set alternate row color
        convertView.setBackgroundColor((i % 2 == 1) ? Color.WHITE : alternateColor);

        holder.updateViews(cmd);

       return convertView;
    }

    private void refreshListItem(ProcessCommand cmd) {
        int i = imageProcessor.getCommands().indexOf(cmd);
        if (i != -1) {
            int pos = i - listView.getFirstVisiblePosition();
            View view = listView.getChildAt(pos);
            getView(i, view, listView);
        }
    }

    private void prepareCommand(ProcessCommand cmd) {
        makeDescription(cmd);
    }

    private void makeDescription(ProcessCommand cmd) {
        String format = context.getResources().getString(cmd.getDescriptionId()) + " %d%%";
        cmd.setDescription(format);
    }


}
