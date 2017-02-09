package com.magestore.app.lib.model.catalog;

import com.magestore.app.lib.model.Model;

/**
 * Created by Mike on 12/22/2016.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public interface Category extends Model {
    String getName();
    String getImage();
    String getPosition();
    int getLevel();
    int getParentID();
    String getPath();
}
