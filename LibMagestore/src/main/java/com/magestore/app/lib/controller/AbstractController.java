package com.magestore.app.lib.controller;

import com.magestore.app.lib.context.MagestoreContext;
import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.view.MagestoreView;

/**
 * Task abstract cho các controller phổ biến của MageStorePOS
 * Created by Mike on 1/9/2017.
 * Magestore
 * mike@trueplus.vn
 */
public abstract class AbstractController<TModel extends Model, TView extends MagestoreView> implements Controller<TView> {
    // item và view
    protected TModel mItem;
    protected TView mView;

    // task chung quản lý các hoạt động
    protected ActionTask<TModel> mActionTask;

    // Controller chủ
    protected Controller mParrentController;

    // context
    protected MagestoreContext mMagestoreContext;

    public void setView(TView view) {
        mView = view;
        mView.setController(this);
    }

    @Override
    public void setMagestoreContext(MagestoreContext context) {
        mMagestoreContext = context;
    }

    @Override
    public MagestoreContext getMagestoreContext() {
        return mMagestoreContext;
    }

    @Override
    public void setParentController(Controller controller) {
        mParrentController = controller;
    }


    @Override
    public void doShowErrorMsg(Exception exp) {
        if (mView != null) mView.showErrorMsg(exp);
    }

    @Override
    public void doShowErrorMsg(String msg) {
        if (mView != null) mView.showErrorMsg(msg);
    }

    @Override
    public void doShowProgress(boolean blnShow) {

    }

    public void bindItem(TModel item) {
        mItem = item;
    }

    /**
     * Thực hiện action
     * @param actionType
     * @param actionCode
     * @param models
     */
    void doAction(int actionType, String actionCode, TModel... models) {
        ActionTask<TModel> actionTask = new ActionTask<TModel>(this, actionType, actionCode);
        actionTask.doExcute(models);
    }

    /**
     * Thực hiện action, trả về true nếu thành công
     * @param actionType
     * @param actionCode
     * @param models
     * @return
     */
    Boolean doActionBackround(int actionType, String actionCode, TModel... models) throws Exception {
        return false;
    }

    /**
     * Trả về sự kiện khi action thành công
     * @param actionType
     * @param actionCode
     * @param models
     */
    void onActionPostExecute(int actionType, String actionCode, TModel... models) {

    }

    /**
     * Sự kiện lúc canceled load data
     */
    public void onCancelledBackground(Exception exp) {
        if (exp != null)
            doShowErrorMsg(exp);
    }

}
