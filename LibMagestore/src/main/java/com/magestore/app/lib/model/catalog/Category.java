package com.magestore.app.lib.model.catalog;

import com.magestore.app.lib.model.Model;

import java.util.List;

/**
 * Created by Mike on 12/22/2016.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public interface Category extends Model {
    void setId(String strId);
    String getName();
    void setName(String strName);
    String getImage();
    String getPosition();
    int getLevel();
    void setLevel(int intLevel);
    int getParentID();
    void setParentId(int intParentId);
    String getPath();
    List<String> getChildren();
    void setChildren(List<String> listChildren);
    List<Category> getSubCategory();
    boolean checkListSubCategory();
}
