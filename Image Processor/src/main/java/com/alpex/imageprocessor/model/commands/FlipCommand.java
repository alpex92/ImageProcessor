package com.alpex.imageprocessor.model.commands;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import com.alpex.imageprocessor.R;

/**
 * Created by Alpex on 25.11.2014.
 */

public class FlipCommand extends ProcessCommandAsync {

    @Override
    public int getDescriptionId() {
        return R.string.btn_mirror;
    }

    @Override
    protected Bitmap process(Bitmap src) {
        Bitmap targetBitmap = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
        Canvas canvas = new Canvas(targetBitmap);
        Matrix matrix = new Matrix();
        matrix.setScale(-1, 1);
        matrix.postTranslate(src.getWidth(), 0);
        canvas.drawBitmap(src, matrix, new Paint());
        return targetBitmap;
    }
}