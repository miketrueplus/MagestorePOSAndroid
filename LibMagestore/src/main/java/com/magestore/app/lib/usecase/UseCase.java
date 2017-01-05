package com.magestore.app.lib.usecase;

import com.magestore.app.lib.context.MagestoreContext;
import com.magestore.app.lib.context.MagestoreProgress;

/**
 * Created by Mike on 12/14/2016.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public interface UseCase {
    void setContext(MagestoreContext context);
    void setProgress(MagestoreProgress progress);
    MagestoreContext getContext();
    MagestoreProgress getProgress();
    void commit();
    void rollback();
}
