package com.magestore.app.lib.usecase;

import com.magestore.app.lib.entity.Config;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

/**
 * Created by Mike on 12/28/2016.
 * Magestore
 * mike@trueplus.vn
 */

public interface ConfigUseCase extends UseCase {
    /**
     * Kết nối server lấy tham số config
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IOException
     * @throws ParseException
     */
    Config retrieveConfig() throws InstantiationException, IllegalAccessException, IOException, ParseException;

    /**
     * Lấy tham số config từ cache luôn
     * @return
     */
    Config getConfig();
}