package com.magestore.app.lib.controller;

import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.lib.panel.AbstractListPanel;
import com.magestore.app.lib.service.ChildListService;

import java.util.List;

/**
 * Task để làm việc với list panel
 * Created by Mike on 1/10/2017.
 * Magestore
 * mike@trueplus.vn
 */

public interface ChildListController<TParent extends Model, TModel extends Model>
        extends ListController<TModel> {
    TParent getParent();
    void bindParent(TParent parent);

    void setChildListService(ChildListService<TParent, TModel> service);
    ChildListService<TParent, TModel> getChildListService();
}
