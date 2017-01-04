package com.magestore.app.lib.usecase;

/**
 * Created by Mike on 12/14/2016.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public interface UseCase {
    void setContext(UseCaseContext context);
    void setProgress(UseCaseProgress progress);
    void commit();
    void rollback();
}
