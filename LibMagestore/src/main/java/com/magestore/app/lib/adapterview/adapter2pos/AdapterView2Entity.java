package com.magestore.app.lib.adapterview.adapter2pos;

import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.magestore.app.lib.adapterview.AdapterView;
import com.magestore.app.lib.entity.Entity;
import com.magestore.app.util.NotFoundObject;

/**
 * Created by Mike on 1/3/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class AdapterView2Entity implements AdapterView {
    private static final int PARSE_FROM_VIEW = 1;
    private static final int PARSE_TO_VIEW = 2;
    Entity mEntity;
    int mintParseFromView;

    /**
     * Map dữ liệu từ view vào engity
     * @param view
     * @param entity
     */
    public void fromView(View view, Entity entity) {
        mEntity = entity;
        mintParseFromView = PARSE_FROM_VIEW;
        debugViewIds(view);
    }

    /**
     * Map dữ liệu tử entity vào view
     * @param view
     * @param entity
     */
    public void toView(View view, Entity entity) {
        mEntity = entity;
        mintParseFromView = PARSE_TO_VIEW;
        debugViewIds(view);
    }

    /**
     * Duyệt toàn bộ view
     * @param view
     */
    private View debugViewIds(View view) {
        int resID = view.getId();
        if (resID != -1) {
            parseEntity(view, mEntity, view.getResources().getResourceEntryName(resID), mintParseFromView);
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

    /**
     * Duyệt toàn bộ child view
     * @param view
     */
    private void debugChildViewIds(View view) {
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup)view;
            for (int i = 0; i < group.getChildCount(); i++) {
                View child = group.getChildAt(i);
                int resID = child.getId();
                if (resID != -1) {
                    parseEntity(child, mEntity, child.getResources().getResourceEntryName(resID), mintParseFromView);
                }
                debugChildViewIds(child);
            }
        }
    }

    private void parseEntity(View view, Entity entity, String resName, int fromOrToView) {
        if (fromOrToView == PARSE_TO_VIEW) parseToView(view, entity, resName);
        else parseFromView(view, entity, resName);
    }

    private void parseToView(View view, Entity entity, String resName) {
        Object object = entity.getValue(resName);
        if (object instanceof NotFoundObject) return;
        if (object == null) return;
        if (view instanceof EditText) {
            ((EditText) view).setText(object.toString());
        }
        return;
    }

    private static void parseFromView(View view, Entity entity, String resName) {
        Object object = entity.getValue(resName);
        if (object instanceof NotFoundObject) return;
        if (view instanceof EditText) {
            String value = ((EditText) view).getText().toString();
            entity.setValue(resName, value);
        }
        return;
    }
}
