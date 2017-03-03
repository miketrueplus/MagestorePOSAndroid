package com.magestore.app.pos.controller;

import com.google.gson.internal.LinkedTreeMap;
import com.magestore.app.lib.controller.AbstractListController;
import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.config.ConfigCountry;
import com.magestore.app.lib.model.customer.Complain;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.model.customer.CustomerAddress;
import com.magestore.app.lib.model.directory.Region;
import com.magestore.app.lib.service.Service;
import com.magestore.app.lib.service.ServiceFactory;
import com.magestore.app.lib.service.config.ConfigService;
import com.magestore.app.lib.service.customer.CustomerAddressService;
import com.magestore.app.lib.service.customer.CustomerComplainService;
import com.magestore.app.lib.service.customer.CustomerService;
import com.magestore.app.pos.model.customer.PosComplain;
import com.magestore.app.pos.panel.CustomerDetailPanel;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Mike on 1/9/2017.
 * Magestore
 * mike@trueplus.vn
 */

public class CustomerListController extends AbstractListController<Customer> {
    // đánh dấu action
    static final int ACTION_CODE_GET_COMPLAIN = 0;
    static final int ACTION_CODE_INPUT_COMPLAIN = 1;

    public static final String STATE_CODE_ON_CLICK_NEW_ADDRESS = "STATE_CODE_ON_CLICK_NEW_ADDRESS";

    /**
     * Service xử lý các vấn đề liên quan đến customer
     */
    CustomerService mCustomerService;
    CustomerComplainService mCustomerComplainService;
    CustomerAddressService mCustomerAddressService;
    ConfigService mConfigService;

    /**
     * Chứa customer group
     */
    Map<String, String> customerGroupList;

    /**
     * Thiết lập service xử lý các nghiệp vụ liên quan customer
     *
     * @param service
     */
    public void setCustomerService(CustomerService service) {
        setListService(service);
        mCustomerService = service;
        try {
            mConfigService = ServiceFactory.getFactory(getMagestoreContext()).generateConfigService();
            mCustomerComplainService = ServiceFactory.getFactory(getMagestoreContext()).generateCustomerComplainService();
            mCustomerAddressService = ServiceFactory.getFactory(getMagestoreContext()).generateCustomerAddressService();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Customer> onRetrieveBackground(int page, int pageSize) throws Exception {
        List<Customer> customers = super.onRetrieveBackground(page, pageSize);
        customerGroupList = mConfigService.getCustomerGroup();
        return customers;
    }

    @Override
    public void onRetrievePostExecute(List<Customer> list) {
        super.onRetrievePostExecute(list);
        if (mDetailView instanceof CustomerDetailPanel && mDetailView != null)
            ((CustomerDetailPanel) mDetailView).setCustomerGroupDataSet(customerGroupList);
    }

    /**
     * Tạo mới customer
     *
     * @return
     */
    public Customer createNewCustomer() {
        return getListService().create();
    }

    /**
     * Trả lại customer service xử lý các nghiệp vụ liên quan customer
     *
     * @return
     */
    public CustomerService getCustomerService() {
        return mCustomerService;
    }

//    public void doLoadComplain(Customer customer) {
//        doAction(ACTION_CODE_GET_COMPLAIN, null, null, customer);
//        if (mDetailView instanceof CustomerDetailPanel)
//            ((CustomerDetailPanel)mDetailView).showComplainProgress(true);
//    }

    /**
     * Tạo mới 1 complain
     *
     * @param newComplain
     */
    public void doInputNewComplain(String newComplain) {
        Complain complain = mCustomerComplainService.create(getSelectedItem());
        complain.setContent(newComplain);
        doAction(ACTION_CODE_INPUT_COMPLAIN, null, null, mItem, complain);
    }

    /**
     * Thực hiện các action trên background
     *
     * @param actionType
     * @param actionCode
     * @param wrapper
     * @param models
     * @return
     * @throws Exception
     */
    @Override
    public Boolean doActionBackround(int actionType, String actionCode, Map<String, Object> wrapper, Model... models) throws Exception {
        if (actionType == ACTION_CODE_GET_COMPLAIN) {
            mCustomerComplainService.retrieve(((Customer) models[0]));
            return true;
        }
        if (actionType == ACTION_CODE_INPUT_COMPLAIN) {
            mCustomerComplainService.insert((Customer) models[0], (Complain) models[1]);
            return true;
        }
        return false;
    }

    /**
     * Sau khi hoàn thành các action
     *
     * @param success
     * @param actionType
     * @param actionCode
     * @param wrapper
     * @param models
     */
    @Override
    public void onActionPostExecute(boolean success, int actionType, String actionCode, Map<String, Object> wrapper, Model... models) {
        // Nếu action là lấy complain
        if (success && actionType == ACTION_CODE_GET_COMPLAIN) {
            // tắt progress với complain
            if (mDetailView instanceof CustomerDetailPanel) {
                ((CustomerDetailPanel) mDetailView).showComplainProgress(false);
                ((CustomerDetailPanel) mDetailView).bindComplain((Customer) models[0]);
            }
        }
        // nếu action là input new complain
        if (success && actionType == ACTION_CODE_INPUT_COMPLAIN) {
            if (mDetailView instanceof CustomerDetailPanel) {
                ((CustomerDetailPanel) mDetailView).bindComplain((Customer) models[0]);
            }
        }
    }

    /**
     * Sau khi hoàn thành action insert
     *
     * @param success
     * @param models
     */
    @Override
    public void onInsertPostExecute(Boolean success, Customer... models) {
        super.onInsertPostExecute(success, models);
        if (success) {
            Customer customer = models[0];
            mList.add(0, customer);
            getView().notifyDataSetChanged();
        }
    }

    /**
     * Bind item, cập nhật giao diện
     *
     * @param item
     */
    @Override
    public void bindItem(Customer item) {
        super.bindItem(item);
        doAction(ACTION_CODE_GET_COMPLAIN, null, null, item);
        if (mDetailView instanceof CustomerDetailPanel)
            ((CustomerDetailPanel) mDetailView).showComplainProgress(true);
    }

    public Map<String, String> getCustomerGroupList() {
        try {
            return mConfigService.getCustomerGroup();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Map<String, ConfigCountry> getCountry() {
        try {
            return mConfigService.getCountry();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public CustomerAddress createCustomerAddress() {
        return mCustomerAddressService.create(null);
    }

    public Region createRegion() {
        return mCustomerAddressService.createRegion();
    }
}