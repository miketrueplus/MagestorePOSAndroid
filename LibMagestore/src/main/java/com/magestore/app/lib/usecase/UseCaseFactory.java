package com.magestore.app.lib.usecase;

import com.magestore.app.lib.context.MagestoreContext;
import com.magestore.app.lib.context.MagestoreProgress;
import com.magestore.app.lib.usecase.pos.POSConfigUseCase;
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
    public static UserUseCase generateUserUseCase(MagestoreContext context, MagestoreProgress progress) {
        return new POSUserUseCase();
    }

    public static ProductUseCase generateProductUseCase(MagestoreContext context, MagestoreProgress progress) {
        return new POSProductUseCase();
    }

    public static OrderUseCase generateOrderUseCase(MagestoreContext context, MagestoreProgress progress) {
        return new POSOrderUseCase();
    }

    public static CustomerUseCase generateCustomerUseCase(MagestoreContext context, MagestoreProgress progress) {
        return new POSCustomerUseCase();
    }

    public static ConfigUseCase generateConfigUseCase(MagestoreContext context, MagestoreProgress progress) {
        return new POSConfigUseCase();
    }
}
