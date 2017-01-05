package com.magestore.app.customer;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;


public class Customer implements Parcelable {
    String group_id;
    String default_billing;
    String default_shipping;
    String created_at;
    String updated_at;
    String created_in;
    String email;
    String firstname;
    String lastname;
    String full_name;
    String gender;
    String store_id;
    String website_id;
    String subscriber_status;
    String telephone;
    String disable_auto_group_change;
    List<Address> addresses;

    public String getName() {
        return full_name;
    }

    public String getEmail() {
        return email;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getFirstName() {
        return firstname;
    }

    public String getLastName() {
        return lastname;
    }

    public String getGroupID() {
        return group_id;
    }

    public List<Address> getAddress() {
        return (List<Address>)(List<?>) addresses;
    }

    public List<Address> newAddressList() {
        addresses = new ArrayList<Address>();
        return getAddress();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Ghi nội dung class lên Parcel
     * @param dest
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Gson gson = new Gson();
        dest.writeString(gson.toJson(this));
    }

    /**
     * Tạo Customer từ Parcel
     * @param source
     * @return
     */
    public Customer createFromParcel(Parcel source) {
        Gson gson = new Gson();
        Customer customer = gson.fromJson(source.readString(), Customer.class);
        return customer;
    }

    /**
     * Khởi tạo các object tử Parcel
     */
    public static final Parcelable.Creator<Customer> CREATOR =
            new Parcelable.Creator<Customer>() {
                @Override
                public Customer createFromParcel(Parcel source) {
                    Customer customer = createFromParcel(source);
                    return customer;
                }

                @Override
                public Customer[] newArray(int size) {
                    return new Customer[size];
                }
            };
}
