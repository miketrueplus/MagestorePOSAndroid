package com.magestore.app.lib.view.adapter;

import android.graphics.Bitmap;
import android.view.View;

import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.view.ViewState;

/**
 * Created by Mike on 2/20/2017.
 * Magestore
 * mike@trueplus.vn
 */

public interface ModelView {
    View getLayoutMainView();

    void setViewState(ViewState state);
    ViewState getViewState();

    void setModel(Model model);
    Model getModel();

    void holdView(View v);
    void bindModel();
    void displayView();

    String getDisplayContent();
    String getSubDisplayContent();
    Bitmap getImageBitmap();

    int getMainLayoutID();
    void setMainLayoutID(int mainLayoutID);

}