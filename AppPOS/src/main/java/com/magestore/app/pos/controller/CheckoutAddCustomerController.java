package com.magestore.app.pos.controller;

import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.model.customer.CustomerAddress;
import com.magestore.app.lib.model.customer.PlaceAddressComponent;
import com.magestore.app.lib.model.customer.PlaceAutoComplete;
import com.magestore.app.lib.model.directory.Region;
import com.magestore.app.pos.R;
import com.magestore.app.pos.panel.CheckoutAddCustomerPanel;
import com.magestore.app.pos.panel.CheckoutListPanel;
import com.magestore.app.pos.panel.CustomerAddNewPanel;
import com.magestore.app.util.ConfigUtil;
import com.magestore.app.util.StringUtil;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by Johan on 2/13/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class CheckoutAddCustomerController extends CustomerListController {
    CheckoutAddCustomerPanel mCheckoutAddCustomerPanel;
    CheckoutListPanel mCheckoutListPanel;
    CustomerAddNewPanel mCustomerAddNewPanel;

    public void setCustomerAddNewPanel(CustomerAddNewPanel mCustomerAddNewPanel) {
        this.mCustomerAddNewPanel = mCustomerAddNewPanel;
    }

    public void setCheckoutListPanel(CheckoutListPanel mCheckoutListPanel) {
        this.mCheckoutListPanel = mCheckoutListPanel;
    }

    public void setCheckoutAddCustomerPanel(CheckoutAddCustomerPanel mCheckoutAddCustomerPanel) {
        this.mCheckoutAddCustomerPanel = mCheckoutAddCustomerPanel;
    }

    @Override
    public void bindItem(Customer item) {
        mCheckoutAddCustomerPanel.updateCustomerToOrder(addAddressDefaultToCustomer(item));
    }

    @Override
    public void onInsertPostExecute(Boolean success, Customer... models) {
        Customer customer = ((Customer) models[0]);
        customer.setAddressPosition(0);
        if (success && customer != null) {
            mCheckoutAddCustomerPanel.updateCustomerToOrder(addAddressDefaultToCustomer(customer));
            mList.add(0, customer);
            bindList(mList);
            mCheckoutAddCustomerPanel.notifiDataListCustomer();
            mCheckoutAddCustomerPanel.scrollToTop();
        }
        if (mCheckoutListPanel != null) {
            mCheckoutListPanel.showToastMessage(0);
        }
    }

    public Customer addAddressDefaultToCustomer(Customer item) {
        if (ConfigUtil.isAddAddressDefault()) {
            Customer newCustomer = item;
            Customer guest_customer = null;
            String userAddressDefault = getMagestoreContext().getActivity().getString(R.string.customer_default_address);
            try {
                guest_customer = mConfigService.getGuestCheckout();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            mCustomerAddressService.addAddressDefaultToCustomer(newCustomer, guest_customer, userAddressDefault);
            return newCustomer;
        } else {
            return item;
        }
    }

    @Override
    public void onUpdatePostExecute(Boolean success, Customer oldModel, Customer newModels) {
        super.onUpdatePostExecute(success, oldModel, newModels);
        if (mCheckoutListPanel != null) {
            mCheckoutListPanel.updateCustomerToOrder(newModels);
            mCheckoutListPanel.showToastMessage(1);
        }
    }

    @Override
    public void displayPlaceSearch(PlaceAutoComplete model) {
        super.displayPlaceSearch(model);
        doInputPlaceDetail(model);
    }

    @Override
    public void convertDataPlaceDetail(List<PlaceAddressComponent> mListAddressComponent) {
        super.convertDataPlaceDetail(mListAddressComponent);
        if (mListAddressComponent != null && mListAddressComponent.size() > 0) {
            CustomerAddress mCustomerAddress = createCustomerAddress();
            String streetNumber = "";
            String route = "";
            String neightborhood = "";
            String locality = "";
            String subLocalityLevel1 = "";
            String administrativeAreaLevel1 = "";
            String stateCode = "";
            String administrativeAreaLevel2 = "";
            String countryName = "";
            String countryCode = "";
            String postalCode = "";
            String postalCodeSuffix = "";
            for (PlaceAddressComponent place : mListAddressComponent) {
                String type = place.getTypes().get(0);
                if (StringUtil.PLACE_STREET_NUMBER.equals(type)) {
                    streetNumber = place.getLongName();
                } else if (StringUtil.PLACE_ROUTE.equals(type)) {
                    route = place.getLongName();
                } else if (StringUtil.PLACE_NEIGHBORHOOD.equals(type)) {
                    neightborhood = place.getLongName();
                } else if (StringUtil.PLACE_LOCALITY.equals(type)) {
                    locality = place.getLongName();
                } else if (StringUtil.PLACE_SUBLOCALITY_LEVEL_1.equals(type)) {
                    subLocalityLevel1 = place.getLongName();
                } else if (StringUtil.PLACE_ADMINISTRATIVE_AREA_LEVEL_1.equals(type)) {
                    administrativeAreaLevel1 = place.getLongName();
                    stateCode = place.getShortName();
                } else if (StringUtil.PLACE_ADMINISTRATIVE_AREA_LEVEL_2.equals(type)) {
                    administrativeAreaLevel2 = place.getLongName();
                } else if (StringUtil.PLACE_COUNTRY.equals(type)) {
                    countryName = place.getLongName();
                    countryCode = place.getShortName();
                } else if (StringUtil.PLACE_POSTAL_CODE.equals(type)) {
                    postalCode = place.getLongName();
                } else if (StringUtil.PLACE_POSTAL_CODE_SUFFIX.equals(type)) {
                    postalCodeSuffix = place.getLongName();
                }
            }

            String street = streetNumber + route;
            mCustomerAddress.setStreet1(street);
            String street2 = subLocalityLevel1;
            mCustomerAddress.setStreet2(street2);
            String city = administrativeAreaLevel2;
            mCustomerAddress.setCity(city);
            String postCode = postalCode;
            mCustomerAddress.setPostCode(postCode);
            Region mRegion = createRegion();
            mRegion.setRegionName(administrativeAreaLevel1);
            mRegion.setRegionCode(stateCode);
            mCustomerAddress.setRegion(mRegion);
            mCustomerAddress.setCountry(countryCode);
            mCustomerAddNewPanel.updatePlaceAutoComplete(mCustomerAddress);
        }
    }
}
