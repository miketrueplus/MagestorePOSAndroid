package com.magestore.app.lib.controller;

import android.view.View;

import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.observ.State;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.lib.panel.AbstractListPanel;
import com.magestore.app.lib.service.ListService;
import com.magestore.app.lib.service.config.ConfigService;

import java.util.List;

/**
 * Task để làm việc với list panel
 * Created by Mike on 1/10/2017.
 * Magestore
 * mike@trueplus.vn
 */

public interface ListController<TModel extends Model>
        extends Controller<AbstractListPanel<TModel>, ListService<TModel>> {

    /**
     * Tạo mới 1 model
     * @return
     */
    TModel createItem();

    /**
     * Lấy dữ liệu về, full không phân trang
     */
    void doRetrieve();

    /**
     * Lấy dữ liệu về, phân trang
     * @param page
     * @param pageSize
     */
    void doRetrieve(int page, int pageSize);

    /**
     * Xử lý sau khi lấy dữ liệu về thành công
     * @param models
     */
    void onRetrievePostExecute(List<TModel> models);

    /**
     * Tiến trình lấy dữ liệu về
     * @param page
     * @param pageSize
     * @return
     * @throws Exception
     */
    List<TModel> onRetrieveBackground(int page, int pageSize) throws Exception;

    /**
     * Kích hoạt cập nhật số liệu trong list
     */
    void doUpdate(TModel oldModel, TModel newModel);

    /**
     * Thực hiện cập nhật item trên background
     */
    boolean onUpdateBackGround(TModel oldModel, TModel newModel) throws Exception;

    /**
     * Thực hiện cập nhật xong
     * @param success
     */
    void onUpdatePostExecute(Boolean success, TModel oldModel, TModel newModel);

    /**
     * Kích hoạt xóa 1 item trên danh sách
     * @param item
     */
    void doDelete(TModel... item);

    /**
     * Thực thi xóa item trên danh sách trên background
     * @param models
     */
    boolean onDeleteBackGround(TModel... models) throws Exception;

    /**
     * Thực thi sau khi xóa item thành công trên api
     * @param success
     */
    void onDeletePostExecute(Boolean success, TModel... models);


    /**
     * Chèn 1 model vào danh sách
     * @param item
     */
    void doInsert(TModel... item);

    /**
     * Thực thi insert trên tiến trình background
     * @param models
     */
    boolean onInsertBackground(TModel... models) throws Exception;

    /**
     * Thực thi insert khi đã thành công
     * @param success
     * @param models
     */
    void onInsertPostExecute(Boolean success, TModel... models);

//    /**
//     * Thiết lập các phân trang
//     * @param pageSize
//     * @param pageMax
//     */
//    void setPage(int pageSize, int pageMax);

    void setConfigService(ConfigService service);

    ConfigService getConfigService();

    /**
     * Gán 1 list vào danh sách, có cập nhật view
     * @param list
     */
    void bindList(List<TModel> list);

    /**
     * Gán cho chọn 1 item trên list, có cập nhật view
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
     * Chỉ định service xử lý
     * @param service
     */
    void setListService(ListService<TModel> service);

    /**
     * Trả lại danh sách đang quản lý
     * @return
     */
    ListService<TModel> getListService();

    /**
     * Chỉ định 1 item được chọn về mặt dataset, k0 có update view
     * @param model
     */
    void setSelectedItem(TModel model);

    /**
     * Trả lại models được chọn trên danh sách
     * @return
     */
    TModel getSelectedItem();

    /**
     * Chỉ định nhiều item được chọn về mặt dataset, k0 có update view
     */
    List<TModel> getSelectedItems();

    /**
     * Chỉ định model được chọn, k0 cập nhật view
     * @param models
     */
    void setSelectedItem(List<TModel> models);

    void doShowDeleteItemInput(TModel item);

    void doShowUpdateItemInput(TModel item);

    void doShowInsertItemInput();

    void doShowInsertItemInput(State state);

    /**
     * Load thêm dữ liệu theo số trang (dùng trong lazy loading
     * @param page
     */
    void doRetrieveMore(int page);

    /**
     * Bật tắt hiển thị panel detail
     * @param show
     */
    void doShowDetailPanel(boolean show);

    AbstractDetailPanel<TModel> getDetailView();

    void doShowListPanel(boolean show);

    void enableLazyLoading(boolean enable);

    void displaySearch(TModel model);

    void displaySearch(List<TModel> model);

    void hideSearch();
}