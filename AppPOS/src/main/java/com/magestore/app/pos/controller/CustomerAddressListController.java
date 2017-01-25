package com.magestore.app.pos.controller;

import com.magestore.app.lib.controller.AbstractListController;
import com.magestore.app.lib.controller.DeleteListTask;
import com.magestore.app.lib.controller.ListController;
import com.magestore.app.lib.controller.UpdateListTask;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.model.customer.CustomerAddress;
import com.magestore.app.lib.service.customer.CustomerService;

import java.util.List;

/**
 * Task cho danh sách địa chỉ khách hàng
 * Magestore
 * mike@trueplus.vn
 */

public class CustomerAddressListController
        extends AbstractListController<CustomerAddress>
        implements ListController<CustomerAddress> {

    /**
     * Task xử lý các vấn đề liên quan đến customer
     */
    CustomerListController mCustomerListController;
    CustomerService mCustomerService;
    Customer mSelectedCustomer;

    /**
     * Thiết lập service
     *
     * @param controller
     */
    public void setCustomerListController(CustomerListController controller) {
        mCustomerListController = controller;
        setParentController(controller);
    }

    /**
     * Thiết lập service
     *
     * @param service
     */
    public void setCustomerService(CustomerService service) {
        mCustomerService = service;
    }

    @Override
    protected List<CustomerAddress> loadDataBackground(Void... params) throws Exception {
        return null;
    }

    /**
     * Thiết lập 1 customer cho androidess
     *
     * @param customer
     */
    public void doSelectCustomer(Customer customer) {
        mSelectedCustomer = customer;
        mList = customer.getAddress();
        mView.bindList(mList);
    }


    @Override
    public boolean onUpdateDataBackGround(CustomerAddress oldAddress, CustomerAddress newAddress) throws Exception {
        if (mCustomerService != null) mCustomerService.updateAddress(mSelectedCustomer, oldAddress, newAddress);
        return true;    }

    /**
     * Cập nhật address thành công
     * @param success
     */
    @Override
    public void onUpdatePostExecute(Boolean success, CustomerAddress oldAddress, CustomerAddress newAddress) {
        if (success) mView.notifyDataSetChanged();
    }

    /**
     * Tạo mới address trên tiến trnihf
     * @param params
     * @return
     * @throws Exception
     */
    @Override
    public boolean onInsertDataBackground(CustomerAddress... params) throws Exception {
        for (CustomerAddress address: params) {
            if (mCustomerService != null) mCustomerService.insertAddress(mSelectedCustomer, address);
        }
        return true;
    }

    /**
     * Tạo mới address thành công
     * @param success
     * @param addresses
     */
    @Override
    public void onInsertPostExecute(Boolean success, CustomerAddress... addresses) {
        if (success) mView.notifyDataSetChanged();
    }

    /**
     * Thực hiện xóa trên tiến trình
     * @param params
     * @return
     * @throws Exception
     */
    @Override
    public boolean onDeleteDataBackGround(CustomerAddress... params) throws Exception {
        for (CustomerAddress address: params) {
            if (mCustomerService != null) mCustomerService.deleteAddress(mSelectedCustomer, address);
        }
        return true;
    }

    /**
     * Xóa thành công
     * @param success
     */
    @Override
    public void onDeletePostExecute(Boolean success) {
        if (success) mView.notifyDataSetChanged();
    }

    /**
     * Khởi tạo 1 customer address để thực hiện tạo mới
     * @return
     */
    public CustomerAddress createNewCustomerAddress() {
        return mCustomerService.createAddress();
    }
}
