package org.jumpmind.pos.context;

import java.sql.Types;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jumpmind.db.model.Column;
import org.jumpmind.db.model.Table;
import org.jumpmind.pos.context.model.ConfigModel;
import org.jumpmind.pos.context.model.ContextRepository;
import org.jumpmind.pos.context.model.DeviceModel;
import org.jumpmind.pos.context.model.TagModel;
import org.jumpmind.pos.persist.DBSessionFactory;
import org.jumpmind.pos.persist.impl.EntityMetaData;
import org.jumpmind.pos.service.IDBSchemaListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("ContextDBSchemaListener")
public class ContextSchemaEnhancer implements IDBSchemaListener {
    
    private final static Logger log = LoggerFactory.getLogger(ContextSchemaEnhancer.class);
    
    private DBSessionFactory sessionFactory;
    
    private final static boolean MODIFY_PK = true;
    private final static boolean PRESERVE_PK = false;

    @Override
    public void beforeSchemaCreate(DBSessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
        List<TagModel> tags = ContextRepository.getTagConfig().getTags();
        enchanceTaggedTable(DeviceModel.class, tags, PRESERVE_PK);
        enchanceTaggedTable(ConfigModel.class, tags, MODIFY_PK);
    }

    @Override
    public void afterSchemaCreate(DBSessionFactory sessionFactory) {
        // No action.
    }
    
    protected void enchanceTaggedTable(Class<?> entityClass, List<TagModel> tags, boolean includeInPk) {
        Table table = sessionFactory.getTableForEnhancement(entityClass);
        warnOrphanedTagColumns(tags, table);
        modifyTagColumns(tags, table, includeInPk);
        addTagColumns(tags, table, includeInPk);
    }

    protected void modifyTagColumns(List<TagModel> tags, Table table, boolean includeInPk) {
        for (Column existingColumn : table.getColumns()) {
            for (TagModel tag : tags) {
                if (StringUtils.equalsIgnoreCase(getColumnName(tag), existingColumn.getName())) {
                    setColumnInfo(existingColumn, tag, includeInPk);
                    break;
                }
            }
        }        
    }

    protected void addTagColumns(List<TagModel> tags, Table table, boolean modifyPk) {
        for (TagModel tag : tags) {
            if (table.getColumnIndex(getColumnName(tag)) == -1) {
                Column tagColumn = generateTagColumn(tag, modifyPk);
                table.addColumn(tagColumn);
            }
        }
    }

    protected void warnOrphanedTagColumns(List<TagModel> tags, Table table) {
        for (Column existingColumn : table.getColumns()) {
            if (! existingColumn.getName().toUpperCase().startsWith(TagModel.TAG_PREFIX)) {
                continue;
            }
            
            boolean matched = false;
            for (TagModel tag : tags) {
                if (StringUtils.equalsIgnoreCase(getColumnName(tag), existingColumn.getName())) {
                    matched = true;
                    break;
                }
            }
            if (!matched) {
                log.info("Orphaned tag column detected.  This column should be manually dropped if no longer needed: " + 
                        table + " " + existingColumn);
            }
        }
    }
    
    protected Column generateTagColumn(TagModel tag, boolean modifyPk) {
        return setColumnInfo(new Column(), tag, modifyPk);
    }
    
    protected Column setColumnInfo(Column column, TagModel tag, boolean includeInPk) {
        column.setName(getColumnName(tag));
        column.setPrimaryKey(includeInPk);
        column.setRequired(true);
        column.setTypeCode(Types.VARCHAR);
        if (tag.getSize() > 0) {
            column.setSize(String.valueOf(tag.getSize()));
        } else {            
            column.setSize("32");
        }
        return column;        
    }
    
    protected String getColumnName(TagModel tag) {
        return TagModel.TAG_PREFIX + tag.getName().toUpperCase();
    }

}
