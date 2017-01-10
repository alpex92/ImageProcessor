package com.alpex.imageprocessor.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.alpex.imageprocessor.MainApplication;
import com.alpex.imageprocessor.R;
import com.alpex.imageprocessor.model.adapters.CommandsAdapter;
import com.alpex.imageprocessor.model.commands.ProcessCommand;
import com.alpex.imageprocessor.model.components.ImageProcessor;
import com.alpex.imageprocessor.model.listeners.CheckPlaceHoldersListener;
import com.alpex.imageprocessor.model.listeners.CommandAddedListener;

/**
 * Created by Alpex on 27.11.2014.
 */
public class ProcessFragment extends Fragment implements CheckPlaceHoldersListener {

    private CheckPlaceHoldersListener checkPlaceHoldersListener;
    private ImageProcessor imageProcessor;

    private TextView listPlaceHolder;
    private View root;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof CheckPlaceHoldersListener) {
            checkPlaceHoldersListener = (CheckPlaceHoldersListener) activity;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.out_layout, container, false);
        configureComponents();
        configureViews(inflater);

        return root;
    }

    private void configureComponents() {
        MainApplication app = (MainApplication) getActivity().getApplication();
        imageProcessor = app.getImageProcessor();
    }

    private void configureViews(LayoutInflater inflater) {

        listPlaceHolder = (TextView) root.findViewById(R.id.listPlaceHolder);

        // Configure commands listView
        ListView list = (ListView) root.findViewById(R.id.listView);
        list.setAdapter(new CommandsAdapter(getActivity(), inflater, imageProcessor, list));

        // Add context menu
        registerForContextMenu(list);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                view.showContextMenu();
            }
        });

        onCheckPlaceHolders();
    }

    private void checkPlaceHolders() {
        if (checkPlaceHoldersListener != null) {
            checkPlaceHoldersListener.onCheckPlaceHolders();
        }
    }

    @Override
    public void onCheckPlaceHolders() {
        listPlaceHolder.setVisibility(imageProcessor.hasCommands() ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.list_context, menu);

        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) menuInfo;

        boolean ready = imageProcessor.isReadyAt(info.position);
        menu.getItem(0).setVisible(ready);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int i = info.position;
        switch (item.getItemId()) {
            case R.id.context_make_current:
                imageProcessor.makeCurrentAt(i);
                return true;
            case R.id.context_remove:
                imageProcessor.removeAt(i);
                checkPlaceHolders();
                return true;
            case R.id.context_remove_all:
                imageProcessor.removeAll();
                checkPlaceHolders();
            default:
                return super.onContextItemSelected(item);
        }
    }
}
