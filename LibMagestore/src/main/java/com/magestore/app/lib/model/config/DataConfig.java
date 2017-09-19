package com.magestore.app.lib.model.config;

import com.magestore.app.lib.model.Model;

import java.util.List;

/**
 * Created by Johan on 9/19/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface DataConfig extends Model{
    List<ConfigOdoo> getItems();
    int getTotalCount();
}
