package com.magestore.app.lib.controller;

import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.lib.panel.AbstractListPanel;
import com.magestore.app.lib.service.ChildListService;
import com.magestore.app.lib.task.DeleteListTask;
import com.magestore.app.lib.task.InsertListTask;
import com.magestore.app.lib.task.RetrieveListTask;
import com.magestore.app.lib.task.UpdateListTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Task quản lý việc hiển thị một List và một View
 * Created by Mike on 1/9/2017.
 * Magestore
 * mike@trueplus.vn
 */
public abstract class AbstractChildListController<TParent extends Model, TModel extends Model>
        extends AbstractListController<TModel>
        implements ChildListController<TParent, TModel> {

    // service kết nối child
    protected ChildListService<TParent, TModel> mChildListService;

    // model parent
    protected TParent mParentModel;

    /**
     * Chỉ định parent model
     * @return
     */
    @Override
    public void bindParent(TParent parent) {
        mParentModel = parent;
        doRetrieve();
    }

    /**
     * Trả lại parent model
     * @return
     */
    @Override
    public TParent getParent() {
        return mParentModel;
    }

    /**
     * Chỉ định child list service xử lý
     * @param service
     */
    @Override
    public void setChildListService(ChildListService<TParent, TModel> service) {
        mChildListService = service;
    }

    /**
     * Trả lại danh sách child list service xử lý
     * @return
     */
    @Override
    public ChildListService<TParent, TModel> getChildListService() {
        return mChildListService;
    }

    /**
     * Tiến trình ngầm thực hiện retrrieve lâ, trên background
     * @param page
     * @return
     * @throws Exception
     */
    @Override
    public List<TModel> onRetrieveBackground(int page, int pageSize) throws Exception {
        if (mChildListService != null && mParentModel != null)
            return mChildListService.retrieve(mParentModel, page, pageSize);
        return null;
    }

    /**
     * Tiến trình ngầm thực hiện insert, trên background
     * @param models
     * @return
     * @throws Exception
     */
    @Override
    public boolean onInsertBackground(TModel... models) throws Exception {
        if (mChildListService != null && mParentModel != null)
            return mChildListService.insert(mParentModel, models);
        return false;
    }

    /**
     * Tiến trình ngầm thực hiện update, trên background
     * @param oldModel
     * @return
     * @throws Exception
     */
    @Override
    public boolean onUpdateBackGround(TModel oldModel, TModel newModel) throws Exception {
        if (mChildListService != null && mParentModel != null)
            return mChildListService.update(mParentModel, oldModel, newModel);
        return false;       }

    /**
     * Tiến trình ngầm thực hiện delete, trên background
     * @param models
     * @return
     * @throws Exception
     */
    @Override
    public boolean onDeleteBackGround(TModel... models) throws Exception {
        if (mChildListService != null && mParentModel != null)
            return mChildListService.delete(mParentModel, models);
        return false;
    }

    @Override
    public TModel createItem(TParent parent) {
        return createItem();
    }
}