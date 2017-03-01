package com.magestore.app.lib.controller;

import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.observ.State;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.lib.panel.AbstractListPanel;
import com.magestore.app.lib.service.ChildListService;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Task để làm việc với list panel
 * Created by Mike on 1/10/2017.
 * Magestore
 * mike@trueplus.vn
 */

public interface ChildListController<TParent extends Model, TModel extends Model>
        extends ListController<TModel> {



    /**
     * Trả lại parent model tham chiếu
     * @return
     */
    TParent getParent();

    /**
     * Bind parent theo cơ chế observ
     * @param state
     */
    void bindParent(State<ListController<TParent>> state);

    /**
     * Chỉ định parent model tham chiếu
     * @param parent
     */
    void bindParent(TParent parent);

    /**
     * Tạo mới 1 model theo parent
     * @return
     */
    TModel createItem(TParent parent) throws IOException, InstantiationException, ParseException, IllegalAccessException;

    /**
     * Chỉ định child list service xử lý thêm, sửa xóa
     * @param service
     */
    void setChildListService(ChildListService<TParent, TModel> service);

    /**
     * Trả lại child list service xử lý thêm, sửa, xpóa
     * @return
     */
    ChildListService<TParent, TModel> getChildListService();
}
