package com.magestore.app.lib.controller;

import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.lib.panel.AbstractListPanel;

import java.util.List;

/**
 * Task để làm việc với list panel
 * Created by Mike on 1/10/2017.
 * Magestore
 * mike@trueplus.vn
 */

public interface ListController<TModel extends Model>
        extends Controller<AbstractListPanel<TModel>> {
    void doLoadData();

    void doUpdateItem(TModel item);

    void doDeleteItem(TModel item);

    void bindList(List<TModel> list);

    void bindItem(TModel item);

    void setListPanel(AbstractListPanel<TModel> view);

    void setDetailPanel(AbstractDetailPanel<TModel> detailPanel);
}
