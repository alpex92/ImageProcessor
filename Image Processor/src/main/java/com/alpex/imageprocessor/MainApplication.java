package com.alpex.imageprocessor;

import android.app.Application;
import com.alpex.imageprocessor.model.components.ImageLoader;
import com.alpex.imageprocessor.model.components.ImageProcessor;

/**
 * Created by Alpex on 27.11.2014.
 */
public class MainApplication extends Application {

    private final ImageProcessor imageProcessor = new ImageProcessor();
    public ImageProcessor getImageProcessor() {
        return imageProcessor;
    }

    private final ImageLoader imageLoader = new ImageLoader();
    public ImageLoader getImageLoader() {
        return imageLoader;
    }
}
