package com.magestore.app.lib.usecase.pos;

import com.magestore.app.lib.usecase.UseCase;
import com.magestore.app.lib.usecase.UseCaseContext;
import com.magestore.app.lib.usecase.UseCaseProgress;

/**
 * Created by Mike on 12/22/2016.
 * Magestore
 * mike@trueplus.vn
 * TODO: Add a class header comment!
 */

public class AbstractUseCase implements UseCase {
    UseCaseProgress mProgress;
    UseCaseContext mContext;

    @Override
    public void setContext(UseCaseContext context) {
        mContext = context;
    }

    @Override
    public void setProgress(UseCaseProgress progress) {
        mProgress = progress;
    }
}
