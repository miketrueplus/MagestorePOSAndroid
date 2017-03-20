package com.magestore.app.lib.connection.http;

import com.magestore.app.lib.connection.StatementAction;

/**
 * Created by folio on 3/20/2017.
 */

public class MagestoreStatementAction implements StatementAction {
    public static final MagestoreStatementAction ACTION_DELETE = new MagestoreStatementAction();
    public static final MagestoreStatementAction ACTION_UPDATE = new MagestoreStatementAction();
    public static final MagestoreStatementAction ACTION_INSERT = new MagestoreStatementAction();
    public static final MagestoreStatementAction ACTION_IRETRIEVE = new MagestoreStatementAction();

}
