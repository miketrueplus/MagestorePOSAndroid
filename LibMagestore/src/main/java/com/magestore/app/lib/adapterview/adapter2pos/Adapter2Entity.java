package com.magestore.app.lib.adapterview.adapter2pos;

import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.magestore.app.lib.adapterview.Adapter;
import com.magestore.app.lib.entity.Entity;

/**
 * Created by Mike on 1/3/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class Adapter2Entity implements Adapter {
    private static final int PARSE_FROM_VIEW = 1;
    private static final int PARSE_TO_VIEW = 2;
    Entity mEntity;
    int mintParseFromView;

    public void fromView(View view, Entity entity) {
        mEntity = entity;
        mintParseFromView = PARSE_FROM_VIEW;
//        debugViewIds(view);
    }

    public void toView(View view, Entity entity) {
        mEntity = entity;
        mintParseFromView = PARSE_TO_VIEW;
//        debugViewIds(view);
    }

    private View debugViewIds(View view) {
        int resID = view.getId();
        if (resID != -1) {
             parseEntity(view, mEntity, view.getResources().getResourceName(resID), mintParseFromView);
        }
//        Log.v(logtag, "traversing: " + view.getClass().getSimpleName() + ", id: " + view.getId());
        if (view.getParent() != null && (view.getParent() instanceof ViewGroup)) {
            return debugViewIds((View)view.getParent());
        }
        else {
            debugChildViewIds(view);
            return view;
        }
    }

    private void debugChildViewIds(View view) {
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup)view;
            for (int i = 0; i < group.getChildCount(); i++) {
                View child = group.getChildAt(i);
                int resID = child.getId();
                if (resID != -1) parseEntity(child, mEntity, child.getResources().getResourceName(resID), mintParseFromView);
                debugChildViewIds(child);
            }
        }
    }

    private void parseEntity(View view, Entity entity, String resName, int fromOrToView) {
        if (fromOrToView == PARSE_TO_VIEW) parseToView(view, entity, resName);
        else parseFromView(view, entity, resName);
    }

    private void parseToView(View view, Entity entity, String resName) {
        Object object = entity.getValue("firstname");
        if (object == null) return;
        if (view instanceof EditText) {
            ((EditText) view).setText(object.toString());
        }
        return;
    }

    private static void parseFromView(View view, Entity entity, String resName) {
        Object object = entity.getValue(resName);
        if (object == null) return;
        if (view instanceof EditText) {
            String value = ((EditText) view).getText().toString();
            entity.setValue(resName, value);
        }
        return;
    }
}
