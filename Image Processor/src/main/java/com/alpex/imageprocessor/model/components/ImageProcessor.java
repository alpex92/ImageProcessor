package com.alpex.imageprocessor.model.components;

import android.graphics.Bitmap;
import com.alpex.imageprocessor.model.commands.ProcessCommand;
import com.alpex.imageprocessor.model.listeners.BitmapChangedListener;
import com.alpex.imageprocessor.model.listeners.CommandAddedListener;
import com.alpex.imageprocessor.model.listeners.CommandRemovedListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Alpex on 25.11.2014.
 */
public class ImageProcessor {

    private Bitmap sourceBitmap;

    private CommandAddedListener commandAddedListener;
    public void setCommandAddedListener(CommandAddedListener listener) {
        commandAddedListener = listener;
    }

    private CommandRemovedListener commandRemovedListener;
    public void setCommandRemovedListener(CommandRemovedListener listener) {
        commandRemovedListener = listener;
    }

    private BitmapChangedListener bitmapChangedListener;
    public void setBitmapChangedListener(BitmapChangedListener listener) {
        bitmapChangedListener = listener;
    }

    private final List<ProcessCommand> commands = new ArrayList<ProcessCommand>();
    public List<ProcessCommand> getCommands() {
        return commands;
    }

    public void makeCurrentAt(int i) {
        ProcessCommand cmd = commands.get(i);
        if (cmd != null && cmd.isReady()) {
            setSource(cmd.getResult());
        }
    }

    public void removeAt(int i) {
        ProcessCommand cmd = commands.get(i);
        commands.remove(i);
        onCommandRemoved(cmd);
        cmd.cancel();
    }

    public void removeAll() {

        Iterator<ProcessCommand> i = commands.iterator();
        while (i.hasNext()) {
            i.next().cancel();
            i.remove();
        }
        onCommandRemoved(null);
    }

    public boolean isReadyAt(int i) {
        ProcessCommand cmd = commands.get(i);
        return (cmd != null) && cmd.isReady();
    }

    public boolean hasCommands() {
        return commands.isEmpty();
    }

    public boolean hasSource() {
        return sourceBitmap != null;
    }

    public void setSource(Bitmap bmp) {
        sourceBitmap = bmp;
        onBitmapChanged(sourceBitmap);
    }

    public void updateBitmap() {
        onBitmapChanged(this.sourceBitmap);
    }

    public void addCommand(ProcessCommand cmd) {
        commands.add(cmd);
        cmd.start(sourceBitmap);

        if (commandAddedListener != null) {
            commandAddedListener.OnCommandAdded(cmd);
        }
    }

    private void onBitmapChanged(Bitmap bmp) {
        if (bitmapChangedListener != null) {
            bitmapChangedListener.onBitmapChanged(bmp);
        }
    }

    private void onCommandRemoved(ProcessCommand cmd) {
        if (commandRemovedListener != null) {
            commandRemovedListener.onCommandRemoved(cmd);
        }
    }
}
