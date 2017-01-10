package com.alpex.imageprocessor.model.listeners;

import com.alpex.imageprocessor.model.commands.ProcessCommand;

/**
 * Created by Alpex on 26.11.2014.
 */

public interface CommandCompletedListener {
    public abstract void onCommandCompleted(ProcessCommand cmd);
}
