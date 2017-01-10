package com.alpex.imageprocessor;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.Toast;
import com.alpex.imageprocessor.fragments.ChooseSourceFragment;
import com.alpex.imageprocessor.model.components.ImageLoader;
import com.alpex.imageprocessor.model.listeners.CheckPlaceHoldersListener;
import com.alpex.imageprocessor.model.listeners.RequestDialogListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity implements CheckPlaceHoldersListener, RequestDialogListener {

    private ImageLoader imageLoader;
    private final List<CheckPlaceHoldersListener> checkPlaceHoldersListeners = new ArrayList<CheckPlaceHoldersListener>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configureComponents();
    }

    private void configureComponents() {
        MainApplication app = (MainApplication) getApplication();
        imageLoader = app.getImageLoader();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && null != data) {
            imageLoader.onFileOpened(this, requestCode, data);
        } else {
            Toast.makeText(this, R.string.dialog_cantopen, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof CheckPlaceHoldersListener) {
            checkPlaceHoldersListeners.add((CheckPlaceHoldersListener)fragment);
        }
    }

    @Override
    public void onCheckPlaceHolders() {
        for (CheckPlaceHoldersListener listener: checkPlaceHoldersListeners) {
            listener.onCheckPlaceHolders();
        }
    }

    @Override
    public void onRequestDialog() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        new ChooseSourceFragment().show(ft, "dialog");
    }
}
