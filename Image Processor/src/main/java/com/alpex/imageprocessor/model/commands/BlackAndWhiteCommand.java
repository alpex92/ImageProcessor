package com.alpex.imageprocessor.model.commands;

import android.graphics.*;
import com.alpex.imageprocessor.R;

/**
 * Created by Alpex on 25.11.2014.
 */

public class BlackAndWhiteCommand extends ProcessCommandAsync {

    @Override
    public int getDescriptionId() {
        return R.string.btn_bnw;
    }

    @Override
    protected Bitmap process(Bitmap src) {
        Bitmap bmpGrayScale = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
        Canvas c = new Canvas(bmpGrayScale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(src, 0, 0, paint);
        return bmpGrayScale;
    }
}