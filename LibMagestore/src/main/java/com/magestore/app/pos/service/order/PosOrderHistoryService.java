package com.magestore.app.pos.service.order;

import android.text.TextUtils;

import com.magestore.app.lib.model.checkout.cart.CartItem;
import com.magestore.app.lib.model.customer.Customer;
import com.magestore.app.lib.model.sales.Order;
import com.magestore.app.lib.model.sales.OrderCommentParams;
import com.magestore.app.lib.model.sales.OrderInvoiceParams;
import com.magestore.app.lib.model.sales.OrderItemParams;
import com.magestore.app.lib.model.sales.OrderRefundParams;
import com.magestore.app.lib.model.sales.OrderShipmentParams;
import com.magestore.app.lib.model.sales.OrderShipmentTrackParams;
import com.magestore.app.lib.model.sales.OrderStatus;
import com.magestore.app.lib.resourcemodel.DataAccessFactory;
import com.magestore.app.lib.resourcemodel.sales.OrderDataAccess;
import com.magestore.app.lib.service.order.OrderHistoryService;
import com.magestore.app.pos.model.sales.PosOrderCommentParams;
import com.magestore.app.pos.model.sales.PosOrderInvoiceParams;
import com.magestore.app.pos.model.sales.PosOrderItemParams;
import com.magestore.app.pos.model.sales.PosOrderRefundParams;
import com.magestore.app.pos.model.sales.PosOrderShipmentParams;
import com.magestore.app.pos.model.sales.PosOrderShipmentTrackParams;
import com.magestore.app.pos.model.sales.PosOrderStatus;
import com.magestore.app.pos.service.AbstractService;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johan on 1/13/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class PosOrderHistoryService extends AbstractService implements OrderHistoryService {

    @Override
    public int count() throws ParseException, InstantiationException, IllegalAccessException, IOException {
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        OrderDataAccess orderDataAccess = factory.generateOrderDataAccess();
        return orderDataAccess.count();
    }

    @Override
    public Order create() {
        return null;
    }

    @Override
    public Order retrieve(String strID) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return null;
    }

    @Override
    public List<Order> retrieve(int page, int pageSize) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        // Khởi tạo order gateway factory
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        OrderDataAccess orderDataAccess = factory.generateOrderDataAccess();
        // giả dữ liêu với page = 32 và pageSize = 30
//        return orderDataAccess.retrieve(32, 30);
        return orderDataAccess.retrieve(page, pageSize);
    }

    @Override
    public List<Order> retrieve() throws IOException, InstantiationException, ParseException, IllegalAccessException {
        return null;
    }

    @Override
    public List<Order> retrieve(String searchString, int page, int pageSize) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        return null;
    }

    @Override
    public boolean update(Order oldModel, Order newModel) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        return false;
    }

    @Override
    public boolean insert(Order... orders) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        return false;
    }

    @Override
    public boolean delete(Order... orders) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        return false;
    }

    @Override
    public List<Order> retrieveOrderLastMonth(Customer customer) throws InstantiationException, IllegalAccessException, IOException, ParseException {
        return retrieve(1, 3);
    }

    @Override
    public String sendEmail(String email, String orderId) throws InstantiationException, IllegalAccessException, IOException, ParseException {
        // Khởi tạo order gateway factory
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        OrderDataAccess orderDataAccess = factory.generateOrderDataAccess();
        return orderDataAccess.sendEmail(email, orderId);
    }

    @Override
    public Order insertOrderStatus(Order order) throws InstantiationException, IllegalAccessException, IOException, ParseException {
        String orderId = order.getID();

        OrderStatus param = order.getParamStatus();
        OrderStatus orderStatus = createOrderStatus();
        orderStatus.setComment(param.getComment());
        orderStatus.setCreatedAt(param.getCreatedAt());
        orderStatus.setId(param.getID());
        orderStatus.setEntityName(param.getEntityName());
        orderStatus.setIsCustomerNotified(param.getIsCustomerNotified());
        orderStatus.setIsVisibleOnFront(param.getIsVisibleOnFront());
        orderStatus.setParentId(param.getParentId());
        orderStatus.setStatus(param.getStatus());
        orderStatus.setExtensionAttributes(param.getExtensionAttributes());

        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        OrderDataAccess orderDataAccess = factory.generateOrderDataAccess();

        return orderDataAccess.insertOrderStatus(orderStatus, orderId);
    }

    @Override
    public Order createShipment(Order order) throws InstantiationException, IllegalAccessException, IOException, ParseException {
        OrderShipmentParams shipmentParams = order.getParamShipment();

        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        OrderDataAccess orderDataAccess = factory.generateOrderDataAccess();

        return orderDataAccess.createShipment(shipmentParams);
    }

    @Override
    public Order orderRefund(Order order) throws InstantiationException, IllegalAccessException, IOException, ParseException {
        OrderRefundParams refundParams = order.getParamRefund();

        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        OrderDataAccess orderDataAccess = factory.generateOrderDataAccess();

        return orderDataAccess.orderRefund(refundParams);
    }

    @Override
    public Order orderInvoice(Order order) throws InstantiationException, IllegalAccessException, IOException, ParseException {
        OrderInvoiceParams invoiceParams = order.getParamInvoice();

        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        OrderDataAccess orderDataAccess = factory.generateOrderDataAccess();

        return orderDataAccess.orderInvoice(invoiceParams);
    }

    @Override
    public Order orderCancel(Order order) throws InstantiationException, IllegalAccessException, IOException, ParseException {
        OrderCommentParams cancelParams = order.getParamCancel();
        String orderID = order.getID();

        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        OrderDataAccess orderDataAccess = factory.generateOrderDataAccess();

        return orderDataAccess.orderCancel(cancelParams, orderID);
    }

    @Override
    public OrderStatus createOrderStatus() {
        PosOrderStatus orderStatus = new PosOrderStatus();
        return orderStatus;
    }

    @Override
    public OrderShipmentParams createOrderShipmentParams() {
        PosOrderShipmentParams orderShipmentParams = new PosOrderShipmentParams();
        return orderShipmentParams;
    }

    @Override
    public OrderShipmentTrackParams createOrderShipmentTrackParams() {
        PosOrderShipmentTrackParams orderShipmentTrackParams = new PosOrderShipmentTrackParams();
        return orderShipmentTrackParams;
    }

    @Override
    public List<OrderShipmentTrackParams> createListTrack() {
        List<OrderShipmentTrackParams> listTrack = new ArrayList<OrderShipmentTrackParams>();
        return listTrack;
    }

    @Override
    public OrderCommentParams createCommentParams() {
        PosOrderCommentParams orderCommentParams = new PosOrderCommentParams();
        return orderCommentParams;
    }

    @Override
    public List<OrderCommentParams> createListComment() {
        List<OrderCommentParams> listComment = new ArrayList<OrderCommentParams>();
        return listComment;
    }

    @Override
    public OrderItemParams createOrderItemParams() {
        OrderItemParams param = new PosOrderItemParams();
        return param;
    }

    @Override
    public OrderRefundParams createOrderRefundParams() {
        PosOrderRefundParams orderRefundParams = new PosOrderRefundParams();
        return orderRefundParams;
    }

    @Override
    public OrderInvoiceParams createOrderInvoiceParams() {
        PosOrderInvoiceParams orderInvoiceParams = new PosOrderInvoiceParams();
        return orderInvoiceParams;
    }

    @Override
    public boolean checkCanInvoice(Order order) {
        String status = order.getStatus();
        if (this.canUnhold(status) || status.equals("holded"))
            return false;
        if (status.equals("status") || status.equals("complete") || status.equals("closed"))
            return false;
        boolean allInvoiced = true;
        for (CartItem item : order.getOrderItems()) {
            if (item.getQtyOrdered() - item.getQtyInvoiced() - item.getQtyCanceled() > 0) {
                allInvoiced = false;
            }
        }
        if (!allInvoiced) {
            return true;
        }
        return false;
    }

    @Override
    public boolean checkCanCancel(Order order) {
        String status = order.getStatus();
        if (this.canUnhold(status)) {
            return false;
        }
        if (status.equals("")) {
            return false;
        }
        if (status.equals("status") || status.equals("complete") || status.equals("closed") || status.equals("canceled"))
            return false;
        boolean allInvoiced = true;
        for (CartItem item : order.getOrderItems()) {
            if (item.getQtyOrdered() - item.getQtyInvoiced() - item.getQtyCanceled() > 0) {
                allInvoiced = false;
            }
        }
        if (allInvoiced) {
            return false;
        }
        return true;
    }

    @Override
    public boolean checkCanRefund(Order order) {
        String status = order.getStatus();
        if (this.canUnhold(status) || status.equals("holded"))
            return false;
        if (status.equals("canceled") || status.equals("closed") || order.getGrandTotal() == 0)
            return false;
        if (order.getTotalPaid() - order.getTotalRefunded() < 0.0001) {
            return false;
        }
        return true;
    }

    @Override
    public boolean checkCanShip(Order order) {
        String status = order.getStatus();
        if (this.canUnhold(status) || status.equals("holded"))
            return false;
        if (order.getIsVirtual().equals("1") || status.equals("canceled")) {
            return false;
        }
        boolean allShip = true;
        for (CartItem item : order.getOrderItems()) {
            if (item.getProductType().equals("customsale") && item.getIsVirtual().equals("1")) {
                return true;
            }
            if (item.getProductType().equals("simple") && !TextUtils.isEmpty(item.getParentItemId())) {
                return true;
            }
            if (item.getQtyOrdered() - item.getQtyShipped() - item.getQtyRefunded() - item.getQtyCanceled() > 0)
                allShip = false;

        }
        if (!allShip) {
            return true;
        }
        return false;
    }

    private boolean canUnhold(String status) {
        if (status.equals("payment_review")) {
            return false;
        }
        if (status.equals("holded")) {
            return true;
        }
        return false;
    }
}
