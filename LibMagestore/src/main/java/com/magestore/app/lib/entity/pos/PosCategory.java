package com.magestore.app.lib.entity.pos;

import com.magestore.app.lib.entity.Category;
import com.magestore.app.lib.entity.Customer;

/**
 * Quản lý thông tin category
 * Created by Mike on 12/22/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class PosCategory extends AbstractEntity implements Category {
    private String id;
    private String name;
    private String image;
    private int position;
    private int level;
}
