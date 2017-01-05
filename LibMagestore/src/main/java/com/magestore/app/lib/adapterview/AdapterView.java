package com.magestore.app.lib.adapterview;

import android.view.View;

import com.magestore.app.lib.entity.Entity;

/**
 * Created by Mike on 1/3/2017.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public interface AdapterView {
    void fromView(View view, Entity entity);
    void toView(View view, Entity entity);
}
