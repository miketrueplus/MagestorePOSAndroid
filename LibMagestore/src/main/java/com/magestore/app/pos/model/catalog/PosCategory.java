package com.magestore.app.pos.model.catalog;

import com.magestore.app.lib.model.catalog.Category;
import com.magestore.app.pos.model.PosAbstractModel;

import java.util.List;

/**
 * Quản lý thông tin category
 * Created by Mike on 12/22/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class PosCategory extends PosAbstractModel implements Category {
    List<String> children;
    String first_category;
    String name;
    String image;
    String position;
    int level;
    int parent_id;
    String path;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getImage() {
        return image;
    }

    @Override
    public String getPosition() {
        return position;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public int getParentID() {
        return parent_id;
    }

    @Override
    public String getPath() {
        return path;
    }
}
