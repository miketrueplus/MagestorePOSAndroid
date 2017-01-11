package com.magestore.app.lib.task;

/**
 * Created by Mike on 12/24/2016.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public interface Task<Params, Progress, Result> {
    void setListener(TaskListener<Params, Progress, Result> listener);
}
