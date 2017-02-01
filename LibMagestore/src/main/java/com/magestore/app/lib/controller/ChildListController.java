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

public interface ChildListController<TParent extends Model, TModel extends Model>
        extends Controller<AbstractListPanel<TModel>> {

    /**
     * Lấy full danh sách
     */
    void doRetrieve();

    /**
     * Lấy danh sách theo phân trang
     * @param page
     * @param pageSize
     */
    void doRetrieve(int page, int pageSize);

    /**
     * Lấy sanh sách thành công
     * @param models
     */
    void onRetrievePostExecute(TParent parent, List<TModel> models);

    /**
     * Lấy danh sách theo background
     * @param parent
     * @param page
     * @param pageSize
     * @return
     * @throws Exception
     */
    List<TModel> onRetrieveBackground(TParent parent, int page, int pageSize) throws Exception;

    /**
     * Kích hoạt cập nhật số liệu trong list
     */
    void doUpdate(TParent parent, TModel oldModel, TModel newModel);

    /**
     * Thực hiện cập nhật item trên background
     */
    boolean onUpdateBackGround(TParent parent, TModel oldModel, TModel newModel) throws Exception;

    /**
     * Thực hiện cập nhật xong
     * @param success
     */
    void onUpdatePostExecute(Boolean success, TParent parent, TModel oldItem, TModel newModel);

    /**
     * Kích hoạt xóa 1 item trên danh sách
     * @param models
     */
    void doDelete(TParent parent, TModel... models);

    /**
     * Thực thi xóa item trên danh sách trên background
     * @param models
     */
    boolean onDeleteBackGround(TParent parent, TModel... models) throws Exception;

    /**
     * Thực thi sau khi xóa item thành công trên api
     * @param success
     */
    void onDeletePostExecute(Boolean success, TParent parent, TModel... models);


    /**
     * Chèn 1 model vào danh sách
     * @param models
     */
    void doInsert(TParent parent, TModel... models);

    /**
     * Thực thi insert trên tiến trình background
     * @param models
     */
    boolean onInsertBackground(TParent parent, TModel... models) throws Exception;

    /**
     * Thực thi insert khi đã thành công
     * @param success
     * @param models
     */
    void onInsertPostExecute(Boolean success, TParent parent, TModel... models);

    /**
     * Gán 1 list vào danh sách
     * @param list
     */
    void bindList(TParent parent, List<TModel> list);

    /**
     * Gán cho chọn 1 item trên list
     */
    void bindModel(TParent parent, TModel item);

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
    void setSelectedModel(TModel model);
    TModel getSelectedModel();

    List<TModel> getSelectedList();
    void setSelectedList(List<TModel> models);

    /**
     * Xác định parent
     * @param parent
     */
    void setParentModel(TParent parent);
    TParent getParentModel();
}
