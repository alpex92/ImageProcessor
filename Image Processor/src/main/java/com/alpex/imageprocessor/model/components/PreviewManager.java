package com.alpex.imageprocessor.model.components;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.support.v4.util.LruCache;
import com.alpex.imageprocessor.model.commands.ProcessCommand;

/**
 * Created by Alpex on 28.11.2014.
 */
public class PreviewManager {

    private static final int PREVIEW_SIZE = 256;

    // TODO: Add fancy preview caching

    public static Bitmap makePreview(Bitmap src) {
        return ThumbnailUtils.extractThumbnail(src, PREVIEW_SIZE, PREVIEW_SIZE);
    }

}

