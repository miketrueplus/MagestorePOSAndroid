package com.magestore.app.lib.service;

import com.magestore.app.lib.context.MagestoreContext;
import com.magestore.app.lib.context.MagestoreProgress;

/**
 * Created by Mike on 12/14/2016.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public interface Service {
    void setContext(MagestoreContext context);
    void setProgress(MagestoreProgress progress);
    MagestoreContext getContext();
    MagestoreProgress getProgress();
    void commit();
    void rollback();
}
