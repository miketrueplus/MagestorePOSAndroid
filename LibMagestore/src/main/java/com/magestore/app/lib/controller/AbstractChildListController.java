package com.magestore.app.lib.controller;

import com.magestore.app.lib.model.Model;
import com.magestore.app.lib.panel.AbstractDetailPanel;
import com.magestore.app.lib.panel.AbstractListPanel;
import com.magestore.app.lib.task.DeleteListTask;
import com.magestore.app.lib.task.InsertListTask;
import com.magestore.app.lib.task.RetrieveListTask;
import com.magestore.app.lib.task.UpdateListTask;

import java.util.ArrayList;
import java.util.List;

/**
 * Task quản lý việc hiển thị một List và một View
 * Created by Mike on 1/9/2017.
 * Magestore
 * mike@trueplus.vn
 */
public abstract class AbstractChildListController<TParent extends Model, TModel extends Model>
        extends AbstractListController<TModel>
        implements ChildListController<TParent, TModel> {


}