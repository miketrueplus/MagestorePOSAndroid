package com.magestore.app.lib.controller;

import com.magestore.app.lib.context.MagestoreContext;
import com.magestore.app.lib.observ.State;
import com.magestore.app.lib.observ.SubjectObserv;
import com.magestore.app.lib.service.Service;
import com.magestore.app.lib.service.config.ConfigService;
import com.magestore.app.lib.view.MagestoreView;

/**
 * Task quản lý model, dùng service để xử lý
 * và ra lệnh update view
 * Created by Mike on 1/9/2017.
 * Magestore
 * mike@trueplus.vn
 */

public interface Controller<TView extends MagestoreView, TService extends Service> {
    void setSubject(SubjectObserv subjectObserv);
    SubjectObserv getSubject();
    void setMagestoreContext(MagestoreContext context);
    MagestoreContext getMagestoreContext();

    void setView(TView view);
    void doShowErrorMsg(Exception exp);
    void doShowErrorMsg(String msg);
    void doShowProgress(boolean blnShow);

    void hideAllProgressBar();

    void setConfigService(ConfigService service);

    ConfigService getConfigService();

    void setService(TService service);

    TService getService();

    void setObserveState(State state);

    void notifyState(State state);
}
