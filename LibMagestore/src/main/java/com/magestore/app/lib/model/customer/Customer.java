package com.magestore.app.lib.model.customer;

import com.magestore.app.lib.model.Model;

import java.util.List;

/**
 * Created by Mike on 12/11/2016.
 */

public interface Customer extends Model {
    void setID(String strID);
    String getName();
    void setName(String strName);
    String getEmail();
    void setEmail(String email);
    String getFirstName();
    void setFirstName(String firstName);
    String getLastName();
    void setLastName(String lastName);
    String getTelephone();
    void setTelephone(String strTelephone);
    String getGroupID();
    void setGroupID(String groupID);
    List<CustomerAddress> getAddress();
    void setAddressList(List<CustomerAddress> addresses);
    List<Complain> getComplain();
    void setComplain(List<Complain> complains);
    String getSubscriber();
    void setSubscriber(String strSubscriber);
    boolean getUseOneAddress();
    void setUseOneAddress(boolean bUseOnleAddress);
    int getAddressPosition();
    void setAddressPosition(int intAddressPosition);
    void setEntityId(String strEntityId);
    String getEntityId();
    String getCreateAt();
    void setCreateAt(String strCreateAt);

    boolean getHasDefaultShipping();
    void setHasDefaultShipping(boolean bHasDefaultShipping);
    boolean getHasDefaultBilling();
    void setHasDefaultBilling(boolean bHasDefaultBilling);
}