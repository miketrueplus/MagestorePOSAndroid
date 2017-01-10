package com.magestore.app.pos.model.catalog;

import com.magestore.app.lib.model.catalog.Category;
import com.magestore.app.pos.model.AbstractModel;

/**
 * Quản lý thông tin category
 * Created by Mike on 12/22/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class PosCategory extends AbstractModel implements Category {
    private String id;
    private String name;
    private String image;
    private int position;
    private int level;
}
