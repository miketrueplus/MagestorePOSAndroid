package com.magestore.app.lib.task;

/**
 * Created by Mike on 12/25/2016.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public interface TaskListener<Params, Progress, Result> {
    void onPreController(Task task);
    void onPostController(Task task, Result result);
    void onCancelController(Task task, Exception exp);
    void onProgressController(Task task, Progress ...progress);
}
