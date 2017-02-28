package com.magestore.app.pos.controller;

import com.magestore.app.lib.controller.AbstractListController;
import com.magestore.app.lib.model.setting.Setting;
import com.magestore.app.lib.service.config.ConfigService;

/**
 * Created by Johan on 2/27/17.
 * Magestore
 * dong.le@trueplus.vn
 */

public class SettingListController extends AbstractListController<Setting> {
    ConfigService mConfigService;

    public void setConfigService(ConfigService mConfigService) {
        this.mConfigService = mConfigService;
    }
}
