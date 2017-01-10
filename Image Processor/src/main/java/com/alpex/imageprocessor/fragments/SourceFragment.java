package com.alpex.imageprocessor.fragments;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.alpex.imageprocessor.MainApplication;
import com.alpex.imageprocessor.R;
import com.alpex.imageprocessor.model.commands.BlackAndWhiteCommand;
import com.alpex.imageprocessor.model.commands.FlipCommand;
import com.alpex.imageprocessor.model.commands.ProcessCommand;
import com.alpex.imageprocessor.model.commands.RotateCommand;
import com.alpex.imageprocessor.model.listeners.BitmapChangedListener;
import com.alpex.imageprocessor.model.components.ImageLoader;
import com.alpex.imageprocessor.model.components.ImageProcessor;
import com.alpex.imageprocessor.model.listeners.CheckPlaceHoldersListener;
import com.alpex.imageprocessor.model.listeners.RequestDialogListener;

/**
 * Created by Alpex on 27.11.2014.
 */
public class SourceFragment extends Fragment implements CheckPlaceHoldersListener {

    private CheckPlaceHoldersListener checkPlaceHoldersListener;
    private RequestDialogListener requestDialogListener;

    private final SparseArray<Class> commandMapping = new SparseArray<Class>() {{
        put(R.id.btn_rotate, RotateCommand.class);
        put(R.id.btn_bnw, BlackAndWhiteCommand.class);
        put(R.id.btn_mirror, FlipCommand.class);
    }};

    private ImageProcessor imageProcessor;
    private ImageLoader imageLoader;

    private ImageView imageView;
    private TextView previewPlaceHolder;
    private View root;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof CheckPlaceHoldersListener) {
            checkPlaceHoldersListener = (CheckPlaceHoldersListener) activity;
        }

        if (activity instanceof RequestDialogListener) {
            requestDialogListener = (RequestDialogListener) activity;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.in_layout, container, false);
        configureComponents();
        configureViews();
        setListeners();
        return root;
    }

    private void configureComponents() {
        MainApplication app = (MainApplication) getActivity().getApplication();
        imageProcessor = app.getImageProcessor();
        imageLoader = app.getImageLoader();
    }

    private void configureViews() {
        imageView = (ImageView) root.findViewById(R.id.imageView);
        previewPlaceHolder = (TextView) root.findViewById(R.id.previewPlaceHolder);
    }

    private void setListeners() {

        // Buttons
        View.OnClickListener buttonsListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCommandButtonClick(view);
            }
        };

        for (int i = 0; i < commandMapping.size(); i++) {
            int key = commandMapping.keyAt(i);
            root.findViewById(key).setOnClickListener(buttonsListener);
        }

        root.findViewById(R.id.imageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOpenDialog();
            }
        });

        // Bitmap change events
        imageLoader.setBitmapChangedListener(new BitmapChangedListener() {
            @Override
            public void onBitmapChanged(Bitmap bmp) {
                imageProcessor.setSource(bmp);
            }
        });

        imageProcessor.setBitmapChangedListener(new BitmapChangedListener() {
            @Override
            public void onBitmapChanged(Bitmap bmp) {
                imageView.setImageBitmap(bmp);
                checkPlaceHolders();
            }
        });

        imageProcessor.updateBitmap();
        onCheckPlaceHolders();

    }

    private void checkPlaceHolders() {
        if (checkPlaceHoldersListener != null) {
            checkPlaceHoldersListener.onCheckPlaceHolders();
        }
    }

    @Override
    public void onCheckPlaceHolders() {
        previewPlaceHolder.setVisibility(imageProcessor.hasSource() ? View.INVISIBLE : View.VISIBLE);
    }

    private void onCommandButtonClick(View v) {
        Class c = commandMapping.get(v.getId());
        if (c != null && imageProcessor.hasSource()) {
            try {
                ProcessCommand cmd = (ProcessCommand) c.newInstance();
                imageProcessor.addCommand(cmd);
                checkPlaceHolders();
            } catch (Exception e) {
                Log.e("Main", "Can\'t instantiate command object of type " + c.getName());
            }
        } else {
            Log.w("Main", "Unknown command. view ID: " + v.getId());
        }
    }

    private void showOpenDialog() {
        requestDialogListener.onRequestDialog();
    }
}
