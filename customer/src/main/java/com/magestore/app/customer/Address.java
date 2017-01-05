package com.magestore.app.customer;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

import java.util.List;

public class Address implements Parcelable  {
    String customer_id;
    String region_id;
    String country_id;
    List<String> street;
    String telephone;
    String postcode;
    String city;
    String firstname;
    String lastname;
    String company;


    public String getName() {
        return firstname + " " + lastname;
    }

    public String getFullAddress() {
        if (street == null) return "";
        StringBuilder builder = new StringBuilder();
        for (String streetline : street) {
            if (streetline != null)
                builder.append(streetline);
        }
        builder.append(", ");
        builder.append(city);
        builder.append(", ");
        builder.append(country_id);
        return builder.toString();
    }

    public String getPostCode() {
        return postcode;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getCountry() {
        return country_id;
    }

    public String getRegion() {
        return region_id;
    }

    public String getCity() {
        return city;
    }

    public List<String> getStreet() {
        return street;
    }

    public String getCompany() {
        return company;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Viết tử object sang Parcel theo JSON
     * @param dest
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Gson gson = new Gson();
        dest.writeString(gson.toJson(this));
    }

    /**
     * Khởi tạo Address từ Json
     * @param source
     * @return
     */
    public Address createFromParcel(Parcel source) {
        Gson gson = new Gson();
        Address address = gson.fromJson(source.readString(), Address.class);
        return address;
    }

    /**
     * Khởi tạo Address từ json
     */
    public static final Parcelable.Creator<Address> CREATOR =
            new Parcelable.Creator<Address>() {
                @Override
                public Address createFromParcel(Parcel source) {
                    Address address = createFromParcel(source);
                    return address;
                }

                @Override
                public Address[] newArray(int size) {
                    return new Address[size];
                }
            };
}
