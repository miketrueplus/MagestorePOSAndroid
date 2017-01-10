package com.magestore.app.pos.panel;

import android.view.View;

import com.magestore.app.lib.model.customer.Customer;

import java.util.List;

/**
 * Các sự kiện khi load được danh sách
 * Created by Mike on 1/9/2017.
 * Magestore
 * mike@trueplus.vn
 */

public interface AbstractListPanelListnener<T> {
    /**
     * Khi user chọn 1 item trên danh sách
     * @param item
     */
    void onSelectItemInList(T item);

    /**
     * Khi load danh sách thành công
     * @param list
     */
    void onSuccessLoadList(List<T> list);
}
