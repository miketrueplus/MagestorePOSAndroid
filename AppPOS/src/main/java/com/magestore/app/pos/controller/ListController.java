package com.magestore.app.pos.controller;

import com.magestore.app.pos.panel.AbstractDetailPanel;
import com.magestore.app.pos.panel.AbstractListPanel;

import java.util.List;

/**
 * Controller để làm việc với list panel
 * Created by Mike on 1/10/2017.
 * Magestore
 * mike@trueplus.vn
 */

public interface ListController<TModel> extends Controller<AbstractListPanel<TModel>> {
    void doLoadData();
    void doUpdateItem(TModel item);
    void doDeleteItem(TModel item);
    void bindList(List<TModel> list);
    void onSelectItem(TModel item);
    void setListPanel(AbstractListPanel<TModel> view);
    void setDetailPanel(AbstractDetailPanel<TModel> detailPanel);
}
