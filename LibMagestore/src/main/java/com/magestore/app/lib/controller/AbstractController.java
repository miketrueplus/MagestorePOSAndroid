package com.magestore.app.lib.controller;

import com.magestore.app.lib.context.MagestoreContext;
import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.observe.State;
import com.magestore.app.lib.observe.Subject;
import com.magestore.app.lib.service.config.ConfigService;
import com.magestore.app.lib.task.ActionModelTask;
import com.magestore.app.lib.view.MagestoreView;

import java.util.Map;

/**
 * Task abstract cho các controller phổ biến của MageStorePOS
 * Created by Mike on 1/9/2017.
 * Magestore
 * mike@trueplus.vn
 */
public abstract class AbstractController<TModel extends Model, TView extends MagestoreView> implements Controller<TView> {
    private Subject mSubject;

    // item và view
    protected TModel mItem;
    protected TView mView;

    // Controller chủ
    protected Controller mParrentController;

    // context
    protected MagestoreContext mMagestoreContext;
    private ConfigService mConfigService;


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
        if (mView != null) mView.showProgress(blnShow);
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
    public void doAction(int actionType, String actionCode, Map<String, Object> wraper, Model... models) {
        ActionModelTask actionTask = new ActionModelTask(this, actionType, actionCode, wraper);
        actionTask.doExcute(wraper, models);
    }

    /**
     * Thực hiện action, trả về true nếu thành công
     * @param actionType
     * @param actionCode
     * @param models
     * @return
     */
    public Boolean doActionBackround(int actionType, String actionCode, Map<String, Object> wraper, Model... models) throws Exception {
        return false;
    }

    /**
     * Trả về sự kiện khi action thành công
     * @param actionType
     * @param actionCode
     * @param models
     */
    public void onActionPostExecute(boolean success, int actionType, String actionCode, Map<String, Object> wraper, Model... models) {

    }

    /**
     * Sự kiện lúc canceled load data
     */
    public void onCancelledBackground(Exception exp) {
        if (exp != null)
            doShowErrorMsg(exp);
        doShowProgress(false);
    }

    @Override
    public void setConfigService(ConfigService service) {
        mConfigService = service;
    }

    @Override
    public ConfigService getConfigService() {
        return mConfigService;
    }

    @Override
    public void notifyState(State state) {

    }

    @Override
    public void setSubject(Subject subject) {
        mSubject = subject;
    }

    @Override
    public Subject getSubject() {
        return mSubject;
    }
}
