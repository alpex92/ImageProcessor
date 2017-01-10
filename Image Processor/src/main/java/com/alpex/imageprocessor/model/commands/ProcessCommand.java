package com.alpex.imageprocessor.model.commands;

import android.graphics.Bitmap;
import com.alpex.imageprocessor.R;
import com.alpex.imageprocessor.model.listeners.CommandCompletedListener;
import com.alpex.imageprocessor.model.listeners.CommandProgressListener;

/**
 * Created by Alpex on 25.11.2014.
 */

public abstract class ProcessCommand {

    private String description;
    protected Bitmap result;

    protected CommandProgressListener commandProgressListener;
    public void setCommandProgressListener(CommandProgressListener listener) {
        commandProgressListener = listener;
    }

    protected CommandCompletedListener commandCompletedListener;
    public void setCommandCompletedListener(CommandCompletedListener listener) {
        commandCompletedListener = listener;
    }

    public int getDescriptionId() {
        return R.string.description_default;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String desc) {
        description = desc;
    }

    public boolean isReady() {
        return true;
    }

    public int getProgress() {
        return 0;
    }

    public Bitmap getResult() {
        return result;
    }

    public void cancel() {
        // Do nothing. Override in subclasses
    }

    public abstract void start(Bitmap src);

    protected abstract Bitmap process(Bitmap src);

}



