package com.alpex.imageprocessor.fragments;

/**
 * Created by Alpex on 30.11.2014.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import com.alpex.imageprocessor.MainApplication;
import com.alpex.imageprocessor.R;
import com.alpex.imageprocessor.model.adapters.SourceListAdapter;
import com.alpex.imageprocessor.model.components.ImageLoader;

public class ChooseSourceFragment extends DialogFragment {

    private ImageLoader imageLoader;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureComponents();
    }

    private void configureComponents() {
        MainApplication app = (MainApplication) getActivity().getApplication();
        imageLoader = app.getImageLoader();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        String[] strings = {
                getResources().getString(R.string.dialog_gallery),
                getResources().getString(R.string.dialog_camera)
        };

        int[] icons = {
                android.R.drawable.ic_menu_gallery,
                android.R.drawable.ic_menu_camera
        };

        builder.setAdapter(
                new SourceListAdapter(getActivity(), getActivity().getLayoutInflater(), strings, icons),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                imageLoader.openFileGallery(getActivity());
                                break;
                            case 1:
                                imageLoader.openFileCamera(getActivity());
                                break;
                        }
                    }
                });

        return builder.create();
    }

}
