package org.jumpmind.pos.db;

import java.lang.reflect.Field;
import java.util.Set;

import org.jumpmind.db.model.Column;
import org.jumpmind.db.model.Database;
import org.jumpmind.db.model.Table;
import org.jumpmind.db.platform.AbstractDatabasePlatform;
import org.jumpmind.db.platform.IDdlBuilder;
import org.jumpmind.db.platform.h2.H2DdlBuilder;
import org.jumpmind.db.sql.ISqlTemplate;
import org.jumpmind.pos.db.model.extension.test.ExtensionToBaseTable1;
import org.jumpmind.pos.db.model.test.BaseTable1;
import org.jumpmind.pos.db.model.test.BaseTable2;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DatabaseManagerTest {

    private static final String TEST_MODEL_PACKAGE = "org.jumpmind.pos.db.model.test";
    private static final String TEST_MODEL_EXTENSION_PACKAGE = "org.jumpmind.pos.db.model.extension.test";
    
    DatabaseManager dbMgr = null;

    @Before
    public void init() {
        dbMgr = new DatabaseManager(testDatabasePlatform);
    }
    
    @Test
    public void testGetClassesForPackageAndAnnotation() {
        Set<Class<?>> classes = dbMgr.getClassesForPackageAndAnnotation(TEST_MODEL_PACKAGE, org.jumpmind.pos.db.Table.class);
        Assert.assertNotEquals(null, classes);
        Assert.assertEquals(2,classes.size());
        Assert.assertEquals(true,classes.contains(BaseTable1.class));
        Assert.assertEquals(true,classes.contains(BaseTable2.class));
    }

    @Test
    public void testCreateColumn() throws NoSuchFieldException {
        Field fld = BaseTable1.class.getDeclaredField("fld1");
        Column col = dbMgr.createColumn(fld);
        org.jumpmind.pos.db.Column columnAnnotation = fld.getAnnotation(org.jumpmind.pos.db.Column.class);
        Assert.assertNotEquals(null, col);
        Assert.assertEquals(col.getName(),columnAnnotation.name());
        Assert.assertEquals(col.getDescription(),columnAnnotation.description());
        Assert.assertEquals(new Integer(col.getJdbcTypeCode()),columnAnnotation.type().getVendorTypeNumber());
        Assert.assertEquals(col.getJdbcTypeName(),columnAnnotation.type().toString());
        Assert.assertEquals(col.getSize(),columnAnnotation.size());
        Assert.assertEquals(col.isPrimaryKey(),columnAnnotation.primaryKey());
        Assert.assertEquals(col.isRequired(),columnAnnotation.required());       
    }

    @Test
    public void testCreateTable() {
        Class<?> clazz = BaseTable1.class;
        Table tbl = dbMgr.createTable(clazz);
        org.jumpmind.pos.db.Table tblAnnotation = clazz.getAnnotation(org.jumpmind.pos.db.Table.class);
        Assert.assertNotEquals(null, tbl);
        Assert.assertEquals(tbl.getName(), tblAnnotation.name());
        Assert.assertEquals(tbl.getDescription(), tblAnnotation.description());
        Assert.assertEquals(3,tbl.getColumnCount());
    }
    
    @Test
    public void testExtendTable() {
        Class<?> baseClass = BaseTable1.class;
        Table tbl = dbMgr.createTable(baseClass);
        Class<?> extendClass = ExtensionToBaseTable1.class;
        dbMgr.extendTable(tbl, extendClass);
        Assert.assertNotEquals(null, tbl);
        Assert.assertEquals(5,tbl.getColumnCount());
        int extensionFieldColumnIdx = tbl.getColumnIndex("extension_fld1"); 
        Assert.assertNotEquals(extensionFieldColumnIdx,-1);      
    }

    @Test
    public void testReadDatabaseFromClasses() {
        String basePackageName =  TEST_MODEL_PACKAGE;
        String extensionPackageName = TEST_MODEL_EXTENSION_PACKAGE;
        Database db = dbMgr.readDatabaseFromClasses(basePackageName, extensionPackageName);
        Assert.assertNotEquals(null, db);
        Assert.assertEquals(2,db.getTableCount());        
    }
    
    private AbstractDatabasePlatform testDatabasePlatform = new AbstractDatabasePlatform() {
        @Override
        public String getName() {
            return "Test";
        }
        
        @Override
        public String getDefaultSchema() {
            return "default Schema.";
        }
        
        @Override
        public String getDefaultCatalog() {
            return "";
        }
        
        @Override
        public <T> T getDataSource() {
            return null;
        }
        
        @Override
        public ISqlTemplate getSqlTemplate() {
            return null;
        }

        @Override
        public ISqlTemplate getSqlTemplateDirty() {
            return null;
        }

        @Override
        public IDdlBuilder getDdlBuilder() {
            return new H2DdlBuilder();
        }        
    };

}
