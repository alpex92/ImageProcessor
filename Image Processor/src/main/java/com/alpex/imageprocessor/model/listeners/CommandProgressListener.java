package com.alpex.imageprocessor.model.listeners;

import com.alpex.imageprocessor.model.commands.ProcessCommand;

/**
 * Created by Alpex on 27.11.2014.
 */
public interface CommandProgressListener {
    public void onProgressUpdated(ProcessCommand cmd, int progress);
}
