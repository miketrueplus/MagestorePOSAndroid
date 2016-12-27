package com.magestore.app.lib.usecase;

import com.magestore.app.lib.usecase.pos.POSCustomerUseCase;
import com.magestore.app.lib.usecase.pos.POSOrderUseCase;
import com.magestore.app.lib.usecase.pos.POSUserUseCase;
import com.magestore.app.lib.usecase.pos.POSProductUseCase;

/**
 * Khởi tạo các instance của các use case
 * Created by Mike on 12/18/2016.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public class UseCaseFactory {
    public static UserUseCase generateUserUseCase(UseCaseContext context, UseCaseProgress progress) {
        return new POSUserUseCase();
    }

    public static ProductUseCase generateProductUseCase(UseCaseContext context, UseCaseProgress progress) {
        return new POSProductUseCase();
    }

    public static OrderUseCase generateOrderUseCase(UseCaseContext context, UseCaseProgress progress) {
        return new POSOrderUseCase();
    }

    public static CustomerUseCase generateCustomerUseCase(UseCaseContext context, UseCaseProgress progress) {
        return new POSCustomerUseCase();
    }
}
