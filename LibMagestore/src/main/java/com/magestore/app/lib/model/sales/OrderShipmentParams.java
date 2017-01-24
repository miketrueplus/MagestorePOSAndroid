package com.magestore.app.lib.model.sales;

import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.model.checkout.cart.Items;

import java.util.List;

/**
 * Created by Johan on 1/24/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public interface OrderShipmentParams extends Model {
    void setOrderId(String strOrderId);

    String getOrderId();

    void setEmailSent(String strEmailSent);

    String getEmailSent();

    List<OrderShipmentItemParams> getItems();

    void setItems(List<OrderShipmentItemParams> items);

    List<OrderShipmentTrackParams> getTracks();

    void setTracks(List<OrderShipmentTrackParams> tracks);

    List<OrderCommentParams> getComments();

    void setComments(List<OrderCommentParams> comments);
}
