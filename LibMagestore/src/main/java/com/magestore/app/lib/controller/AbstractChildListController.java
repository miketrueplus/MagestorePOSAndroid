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

    @Override
    public void bindParent(TParent parent) {
        mParentModel = parent;
        doRetrieve();
    }

    @Override
    public TParent getParent() {
        return mParentModel;
    }

    @Override
    public void setChildListService(ChildListService<TParent, TModel> service) {
        mChildListService = service;
    }

    @Override
    public ChildListService<TParent, TModel> getChildListService() {
        return mChildListService;
    }

    @Override
    public List<TModel> onRetrieveBackground(int page, int pageSize) throws Exception {
        if (mChildListService != null && mParentModel != null)
            return mChildListService.retrieve(mParentModel, page, pageSize);
        return null;
    }

    @Override
    public boolean onInsertBackground(TModel... models) throws Exception {
        if (mChildListService != null && mParentModel != null)
            return mChildListService.insert(mParentModel, models);
        return false;
    }

    @Override
    public boolean onUpdateBackGround(TModel oldModel, TModel newModel) throws Exception {
        if (mChildListService != null && mParentModel != null)
            return mChildListService.update(mParentModel, oldModel, newModel);
        return false;       }

    @Override
    public boolean onDeleteBackGround(TModel... models) throws Exception {
        if (mChildListService != null && mParentModel != null)
            return mChildListService.delete(mParentModel, models);
        return false;
    }
}