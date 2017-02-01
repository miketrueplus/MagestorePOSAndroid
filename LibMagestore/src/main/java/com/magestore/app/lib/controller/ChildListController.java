package com.magestore.app.lib.controller;

import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.lib.panel.AbstractListPanel;

import java.util.List;

/**
 * Task để làm việc với list panel
 * Created by Mike on 1/10/2017.
 * Magestore
 * mike@trueplus.vn
 */

public interface ChildListController<TModel extends Model>
        extends Controller<AbstractListPanel<TModel>> {

    void doLoadData();
    void doRetrieveItem();
    void doRetrieveItem(int page, int pageSize);
    void onRetrievePostExecute(List<TModel> models);
    List<TModel> onRetrieveDataBackground(int page, int pageSize) throws Exception;

    /**
     * Kích hoạt cập nhật số liệu trong list
     */
    void doUpdateItem(TModel oldModel, TModel newModel);

    /**
     * Thực hiện cập nhật item trên background
     */
    boolean onUpdateDataBackGround(TModel oldModel, TModel newModel) throws Exception;

    /**
     * Thực hiện cập nhật xong
     * @param success
     */
    void onUpdatePostExecute(Boolean success, TModel oldModel, TModel newModel);

    /**
     * Kích hoạt xóa 1 item trên danh sách
     * @param item
     */
    void doDeleteItem(TModel... item);

    /**
     * Thực thi xóa item trên danh sách trên background
     * @param models
     */
    boolean onDeleteDataBackGround(TModel... models) throws Exception;

    /**
     * Thực thi sau khi xóa item thành công trên api
     * @param success
     */
    void onDeletePostExecute(Boolean success);


    /**
     * Chèn 1 model vào danh sách
     * @param item
     */
    void doInsertItem(TModel... item);

    /**
     * Thực thi insert trên tiến trình background
     * @param models
     */
    boolean onInsertDataBackground(TModel... models) throws Exception;

    /**
     * Thực thi insert khi đã thành công
     * @param success
     * @param models
     */
    void onInsertPostExecute(Boolean success, TModel... models);

    /**
     * Gán 1 list vào danh sách
     * @param list
     */
    void bindList(List<TModel> list);

    /**
     * Gán cho chọn 1 item trên list
     * @param item
     */
    void bindItem(TModel item);

    /**
     * Chỉ định panel list master
     * @param view
     */
    void setListPanel(AbstractListPanel<TModel> view);

    /**
     * Chỉ định panel detail
     * @param detailPanel
     */
    void setDetailPanel(AbstractDetailPanel<TModel> detailPanel);

    /**
     * Xác định item nào đang được chọn và xử lý
     * @param model
     */
    void setSelectedItem(TModel model);
    TModel getSelectedItem();
    List<TModel> getSelectedItems();
    void setSelectedItem(List<TModel> models);
}
