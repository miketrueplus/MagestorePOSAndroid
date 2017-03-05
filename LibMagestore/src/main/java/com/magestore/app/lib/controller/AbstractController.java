package com.magestore.app.lib.controller;

import com.magestore.app.lib.context.MagestoreContext;
import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.observ.GenericState;
import com.magestore.app.lib.observ.State;
import com.magestore.app.lib.observ.SubjectObserv;
import com.magestore.app.lib.service.Service;
import com.magestore.app.lib.service.config.ConfigService;
import com.magestore.app.lib.task.ActionModelTask;
import com.magestore.app.lib.task.LoadModelTask;
import com.magestore.app.lib.view.MagestoreView;

import java.util.Map;

/**
 * Task abstract cho các controller phổ biến của MageStorePOS
 * Created by Mike on 1/9/2017.
 * Magestore
 * mike@trueplus.vn
 */
public abstract class AbstractController<TModel extends Model, TView extends MagestoreView, TService extends Service> implements Controller<TView, TService> {
    private SubjectObserv mSubjectObserv;

    // item và view
    private TService mService;
    protected TModel mItem;
    protected TView mView;

    // Controller chủ
    private Controller mParrentController;

    // context
    protected MagestoreContext mMagestoreContext;
    private ConfigService mConfigService;


    public void setView(TView view) {
        mView = view;
        mView.setController(this);
    }

    public TView getView() {
        return mView;
    }

    @Override
    public void setMagestoreContext(MagestoreContext context) {
        mMagestoreContext = context;
    }

    @Override
    public MagestoreContext getMagestoreContext() {
        return mMagestoreContext;
    }

    @Deprecated
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
        if (mView != null) {
            mView.showProgress(blnShow);
        }
    }

    public void bindItem(TModel item) {
        mItem = item;
    }

    /**
     * Load item
     * @param item
     */
    public void doLoadItem(TModel... item) {
        LoadModelTask loadTask = new LoadModelTask(this);
        loadTask.doExcute(item);
    }

    /**
     * Thực hiện load item trên tiến trình ở background
     * @param item
     * @return
     */
    public boolean doLoadItemBackground(TModel... item) throws Exception  {
        return true;
    }

    /**
     * Trước khi load item
     * @param item
     */
    public void onLoadItemPreExecute(TModel... item) {
        doShowProgress(true);
    }

    /**
     * Load thành công item
     * @param success
     * @param item
     */
    public void onLoadItemPostExecute(boolean success, TModel... item) {
        // giấu các progress bar
        getView().hideAllProgressBar();

        // báo cho các observ khác về việc bind item
        GenericState<AbstractController<TModel, TView, TService>> state = new GenericState<AbstractController<TModel, TView, TService>>(this, GenericState.DEFAULT_STATE_CODE_ON_LOAD_ITEM);
        if (getSubject() != null) getSubject().setState(state);
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

    public void onActionPreExecute(int actionType, String actionCode, Map<String, Object> wraper, Model... models) {

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
        // báo cho các observ khác về việc bind item
        GenericState<AbstractController<TModel, TView, TService>> state = new GenericState<AbstractController<TModel, TView, TService>>(this, actionCode);
        if (getSubject() != null) getSubject().setState(state);
    }

    /**
     * Sự kiện lúc canceled load data
     */
    public void onCancelledBackground(Exception exp) {
        if (exp != null)
            doShowErrorMsg(exp);
        mView.hideAllProgressBar();
    }

    /**
     * Đặt config service để sử dụng
     * @param service
     */
    @Override
    public void setConfigService(ConfigService service) {
        mConfigService = service;
    }

    /**
     * Trả lại config service
     * @return
     */
    @Override
    public ConfigService getConfigService() {
        return mConfigService;
    }

    /**
     * Đặt config service để sử dụng
     * @param service
     */
    @Override
    public void setService(TService service) {
        mService = service;
    }

    /**
     * Trả lại config service
     * @return
     */
    @Override
    public TService getService() {
        return mService;
    }

    /**
     * Gửi 1 state và thông báo cho các controller observe khác
     * @param state
     */
    @Override
    public void setObserveState(State state) {
        state.setController(this);
        if (getSubject() != null) getSubject().setState(state);
    }

    /**
     * Khởi tạo observ
     * @return
     */
    public SubjectObserv.Observe attachListenerObserve() {
        if (getSubject() != null)
            return getSubject().attach(this);
        return null;
    }

    public void attachListenerObserve(Class<State> stateClass, String stateCode, Class<Controller> controllerStateClazz, Controller controllerState) {
        if (getSubject() != null) getSubject().attach(this, null, stateClass, stateCode, controllerStateClazz, controllerState);
    }

    public void attachListenerObserve(String stateCode, Class controllerStateClazz) {
        if (getSubject() != null) getSubject().attach(this, null, null, stateCode, controllerStateClazz, null);
    }

    public void attachListenerObserve(String stateCode, Controller controllerState) {
        if (getSubject() != null) getSubject().attach(this, null, null, stateCode, null, controllerState);
    }

    public void attachListenerObserve(String methodName, Class<State> stateClass, String stateCode, Class<Controller> controllerStateClazz, Controller controllerState) {
        if (getSubject() != null) getSubject().attach(this, methodName, stateClass, stateCode, controllerStateClazz, controllerState);
    }

    public void attachListenerObserve(String methodName, String stateCode, Class controllerStateClazz) {
        if (getSubject() != null) getSubject().attach(this, methodName, null, stateCode, controllerStateClazz, null);
    }

    public void attachListenerObserve(String methodName, String stateCode, Controller controllerState) {
        if (getSubject() != null) getSubject().attach(this, methodName, null, stateCode, null, controllerState);
    }

    /**
     * Tiếp nhận state do controller observer khác thông báo
     * @param state
     */
    @Override
    public void notifyState(State state) {

    }

    /**
     * Gán subjectObserv thông báo cho các controller observer khác
     * @param subjectObserv
     */
    @Override
    public void setSubject(SubjectObserv subjectObserv) {
        mSubjectObserv = subjectObserv;
    }

    /**
     * Trả về subject để thông báo cho các observer khác
     * @return
     */
    @Override
    public SubjectObserv getSubject() {
        return mSubjectObserv;
    }
}
