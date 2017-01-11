package com.magestore.app.pos.task;

import android.os.AsyncTask;

import com.magestore.app.lib.task.Task;
import com.magestore.app.lib.task.TaskListener;

/**
 * Các tiến trình load, xử lý dữ liệu và phản hổi với UI
 * Created by Mike on 12/24/2016.
 * Magestore
 * mike@trueplus.vn
 */

public abstract class AsyncTaskAbstractTask<Params, Progress, Result> extends
        AsyncTask<Params, Progress, Result>
        implements Task<Params, Progress, Result> {

    // Listener nắm giữ các tương tác với UI
    protected TaskListener<Params, Progress, Result> mListner;

    // Lỗi khi thao tác với dữ liệu
    protected Exception mException;

    /**
     * Khởi tạo với listener
     * @param listener
     */
    public AsyncTaskAbstractTask(TaskListener<Params, Progress, Result> listener) {
        setListener(listener);
    }

    /**
     * Thiết lập listener
     * @param listener
     */
    @Override
    public void setListener(TaskListener<Params, Progress, Result> listener) {
        mListner = listener;
    }

    /**
     * Get tham chiếu listener
     * @return
     */
    public TaskListener<Params, Progress, Result> getListner() {
        return mListner;
    }

    /**
     * Ngừng thực hiện tiến trình do có exception
     * @param exp
     * @param mayinterupt
     */
    public void cancel(Exception exp, boolean mayinterupt) {
        mException = exp;
        cancel(mayinterupt);
    }

    /**
     * Sự kiện trước khi thực hiện tiến trình
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (mListner != null) mListner.onPreController(this);
    }

    /**
     * Sự kiện sau khi thực hiện tiến trình
     * @param result Được trả trong hàm doInBackground
     */
    @Override
    protected void onPostExecute(Result result) {
        super.onPostExecute(result);
        if (mListner != null) mListner.onPostController(this, result);
    }

    /**
     * Kích hoạt bởi doInBackground bằng hàm updateProgress
     * @param values
     */
    @Override
    protected void onProgressUpdate(Progress... values) {
        super.onProgressUpdate(values);
        if (mListner != null) mListner.onProgressController(this, values);
    }

    /**
     * Sự kiện khi ngừng thực hiện tiến trình
     */
    @Override
    protected void onCancelled() {
        super.onCancelled();
        if (mListner != null) mListner.onCancelController(this, mException);
    }

}