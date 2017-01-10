package com.alpex.imageprocessor.model.commands;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.SystemClock;
import com.alpex.imageprocessor.model.components.PreviewManager;

import java.util.Random;

/**
 * Created by Alpex on 27.11.2014.
 */
public abstract class ProcessCommandAsync extends ProcessCommand {

    private static final int PROGRESS_MAX = 100;
    private static final int DELAY_TICK_MS = 100;
    private static final int MS_IN_SEC = 1000;
    private static final int DELAY_MIN_SEC = 5;
    private static final int DELAY_MAX_SEC = 30;

    private class ProcessTask extends AsyncTask<Bitmap, Integer, Bitmap> {

        @Override
        protected Bitmap doInBackground(Bitmap... bitmaps) {

            int max = getRandTime();
            for (int i = 0; !isCancelled() && i < max; i++) {
                SystemClock.sleep(DELAY_TICK_MS);
                publishProgress(i * PROGRESS_MAX / max);
            }
            return process(bitmaps[0]);
        }

        private int getRandTime() {
            Random rnd = new Random();
            int ratio = MS_IN_SEC / DELAY_TICK_MS;
            return (rnd.nextInt(DELAY_MAX_SEC - DELAY_MIN_SEC) + DELAY_MIN_SEC) * ratio;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progress = values[0];
            if (commandProgressListener != null) {
                commandProgressListener.onProgressUpdated(ProcessCommandAsync.this, values[0]);
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            result = bitmap;
            if (commandCompletedListener != null) {
                commandCompletedListener.onCommandCompleted(ProcessCommandAsync.this);
            }
        }
    }
    private ProcessTask task = new ProcessTask();
    private int progress = 0;

    @Override
    public int getProgress() {
        return progress;
    }

    @Override
    public boolean isReady() {
        return task.getStatus() == AsyncTask.Status.FINISHED;
    }

    @Override
    public void cancel() {
        task.cancel(true);
    }

    @Override
    public void start(Bitmap src) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, src);
        }
        else {
            task.execute(src);
        }
    }

}
