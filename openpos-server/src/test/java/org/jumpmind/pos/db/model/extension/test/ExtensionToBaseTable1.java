package org.jumpmind.pos.db.model.extension.test;

import java.sql.JDBCType;

import org.jumpmind.pos.db.Column;
import org.jumpmind.pos.db.Extends;

@Extends(clazz=org.jumpmind.pos.db.model.test.BaseTable1.class)
public class ExtensionToBaseTable1 {

    @Column(name="extension_fld1", type=JDBCType.VARCHAR, size="50",
            description="Description for column extension_fld1.")
    private String extensionFld1;

    @Column(name="extension_fld2", type=JDBCType.VARCHAR, size="50",
            description="Description for column extension_fld2.")
    private String extensionFld2;

}
