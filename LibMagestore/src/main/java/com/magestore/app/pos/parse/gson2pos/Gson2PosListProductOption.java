package com.magestore.app.pos.parse.gson2pos;

import com.magestore.app.lib.parse.ParseModel;
import com.magestore.app.pos.model.catalog.PosProductOption;
import com.magestore.app.pos.model.catalog.PosProductOptionJsonConfig;

import java.util.List;

/**
 * Cấu trúc list của customer, convert từ gson thành
 * Created by Mike on 12/27/2016.
 * Magestore
 * mike@trueplus.vn
 */

public class Gson2PosListProductOption implements ParseModel {
    public List<PosProductOption> custom_options;
    public PosProductOptionJsonConfig json_config;
}
