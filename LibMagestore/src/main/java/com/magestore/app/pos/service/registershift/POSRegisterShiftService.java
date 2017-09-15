package com.magestore.app.pos.service.registershift;

import com.magestore.app.lib.model.registershift.CashBox;
import com.magestore.app.lib.model.registershift.CashTransaction;
import com.magestore.app.lib.model.registershift.OpenSessionValue;
import com.magestore.app.lib.model.registershift.RegisterShift;
import com.magestore.app.lib.model.registershift.SessionParam;
import com.magestore.app.lib.resourcemodel.DataAccessFactory;
import com.magestore.app.lib.resourcemodel.registershift.RegisterShiftDataAccess;
import com.magestore.app.lib.service.registershift.RegisterShiftService;
import com.magestore.app.pos.model.registershift.PosCashBox;
import com.magestore.app.pos.model.registershift.PosCashTransaction;
import com.magestore.app.pos.model.registershift.PosOpenSessionValue;
import com.magestore.app.pos.model.registershift.PosRegisterShift;
import com.magestore.app.pos.model.registershift.PosSessionParam;
import com.magestore.app.pos.service.AbstractService;
import com.magestore.app.util.ConfigUtil;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Johan on 1/18/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class POSRegisterShiftService extends AbstractService implements RegisterShiftService {

    @Override
    public int count() throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return 0;
    }

    @Override
    public RegisterShift create() {
        return new PosRegisterShift();
    }

    @Override
    public RegisterShift retrieve(String strID) throws ParseException, InstantiationException, IllegalAccessException, IOException {
        return null;
    }

    @Override
    public List<RegisterShift> retrieve(int page, int pageSize) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        // Khởi tạo register shift gateway factory
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        RegisterShiftDataAccess registerShiftDataAccess = factory.generateRegisterShiftDataAccess();
        return registerShiftDataAccess.retrieve(page, pageSize);
    }

    @Override
    public List<RegisterShift> retrieve() throws IOException, InstantiationException, ParseException, IllegalAccessException {
        return null;
    }

    @Override
    public List<RegisterShift> retrieve(String searchString, int page, int pageSize) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        return null;
    }

    @Override
    public boolean update(RegisterShift oldModel, RegisterShift newModel) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        return false;
    }

    @Override
    public boolean insert(RegisterShift... registerShifts) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        return false;
    }

    @Override
    public boolean delete(RegisterShift... registerShifts) throws IOException, InstantiationException, ParseException, IllegalAccessException {
        return false;
    }

    @Override
    public List<RegisterShift> openSession(SessionParam sessionParam) throws InstantiationException, IllegalAccessException, IOException, ParseException {
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        RegisterShiftDataAccess registerShiftDataAccess = factory.generateRegisterShiftDataAccess();
        return registerShiftDataAccess.openSession(sessionParam);
    }

    @Override
    public List<RegisterShift> insertMakeAdjustment(RegisterShift registerShift) throws InstantiationException, IllegalAccessException, IOException, ParseException {
        // Khởi tạo register shift gateway factory
        CashTransaction cashTransaction = createCashTransaction();
        cashTransaction.setBalance(registerShift.getBalance());
        cashTransaction.setBaseBalance(registerShift.getBaseBalance());
        cashTransaction.setBaseCurrencyCode(registerShift.getBaseCurrencyCode());
        cashTransaction.setCreateAt(registerShift.getParamCash().getCreatedAt());
        cashTransaction.setBaseValue(ConfigUtil.convertToBasePrice(registerShift.getParamCash().getValue()));
        cashTransaction.setLocationId(registerShift.getLocationId());
        cashTransaction.setNote(registerShift.getParamCash().getNote());
        cashTransaction.setShiftId(registerShift.getShiftId());
        cashTransaction.setType(registerShift.getParamCash().getType());
        cashTransaction.setValue(registerShift.getParamCash().getValue());
        cashTransaction.setParamShiftId(registerShift.getID());
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        RegisterShiftDataAccess registerShiftDataAccess = factory.generateRegisterShiftDataAccess();
        return registerShiftDataAccess.insertMakeAdjustment(cashTransaction);
    }

    @Override
    public List<RegisterShift> closeSession(SessionParam sessionParam) throws InstantiationException, IllegalAccessException, IOException, ParseException {
        DataAccessFactory factory = DataAccessFactory.getFactory(getContext());
        RegisterShiftDataAccess registerShiftDataAccess = factory.generateRegisterShiftDataAccess();
        return registerShiftDataAccess.closeSession(sessionParam);
    }

    @Override
    public CashTransaction createCashTransaction() {
        CashTransaction cashTransaction = new PosCashTransaction();
        return cashTransaction;
    }

    @Override
    public OpenSessionValue createOpenSessionValue() {
        return new PosOpenSessionValue();
    }

    @Override
    public SessionParam createSessionParam() {
        return new PosSessionParam();
    }

    @Override
    public CashBox createCashBox() {
        return new PosCashBox();
    }

    @Override
    public List<CashTransaction> createListCashTransaction(RegisterShift registerShift, String openShift, String balance) {
        PosCashTransaction posCashTransaction = new PosCashTransaction();
        posCashTransaction.setCreateAt(registerShift.getOpenedAt());
        posCashTransaction.setOpenShiftTitle(openShift);
        posCashTransaction.setBalanceTitle(balance);
        posCashTransaction.setNote(registerShift.getOpenedNote());
        posCashTransaction.setFloatAmount(registerShift.getFloatAmount());
        posCashTransaction.setBaseFloatAmount(registerShift.getBaseFloatAmount());
        posCashTransaction.setCheckOpenShift(true);

        List<CashTransaction> nListCash = new ArrayList<CashTransaction>();
        nListCash.add(posCashTransaction);
        return nListCash;
    }
}
