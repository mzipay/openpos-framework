package org.jumpmind.pos.service.util;

import static org.jumpmind.pos.service.util.ClassUtils.getClassesForPackageAndAnnotation;
import static org.jumpmind.pos.service.util.ModelMarkDownGeneratorConstants.COLUMN_TABLE_DIVIDER;
import static org.jumpmind.pos.service.util.ModelMarkDownGeneratorConstants.COLUMN_TABLE_HEADING;
import static org.jumpmind.pos.service.util.ModelMarkDownGeneratorConstants.LINE_SKIP;
import static org.jumpmind.pos.service.util.ModelMarkDownGeneratorConstants.MODELS_HEADING;
import static org.jumpmind.pos.service.util.ModelMarkDownGeneratorConstants.MODEL_HEADING_START;
import static org.jumpmind.pos.service.util.ModelMarkDownGeneratorConstants.MODULE_HEADING;
import static org.jumpmind.pos.service.util.ModelMarkDownGeneratorConstants.TABLE_DIVISION;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

import org.jumpmind.db.model.Column;
import org.jumpmind.db.model.Table;
import org.jumpmind.pos.persist.TableDef;
import org.jumpmind.pos.persist.impl.DatabaseSchema;
import org.jumpmind.pos.persist.impl.EntityMetaData;
import org.jumpmind.pos.service.IModule;

public class ModelMarkDownGenerator {

    boolean autoGenerateExamples = true;
    String outputDir = "";

    StringBuilder header = new StringBuilder();
    StringBuilder markdown = new StringBuilder();

    Queue<Class<?>> models = new LinkedList<Class<?>>();

    public ModelMarkDownGenerator() {
        header.append(MODELS_HEADING).append(LINE_SKIP);
    }

    public void setAutoGenerateExamples(boolean autoGenerateExamples) {
        this.autoGenerateExamples = autoGenerateExamples;
    }

    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }

    public void document(Class<?> moduleClass) throws Exception {

        IModule module = (IModule) moduleClass.newInstance();
        markdown.append(MODULE_HEADING + module.getName().toUpperCase()).append(" MODULE").append(LINE_SKIP);
        // TODO description of module?

        List<Class<?>> models = getClassesForPackageAndAnnotation(moduleClass.getPackage().getName(), TableDef.class);
        Set<Table> tables = new TreeSet<>();
        for (Class<?> model : models) {
            List<EntityMetaData> metadatas = DatabaseSchema.createMetaDatas(model);
            for (EntityMetaData entityMetaData : metadatas) {
                tables.add(entityMetaData.getTable());
            }
        }

        for (Table table : tables) {
            document(module.getTablePrefix(),table);
        }
    }

    public void document(String prefix, Table table) {
        markdown.append(MODEL_HEADING_START + (prefix + "_" + table.getName()).toUpperCase()).append(LINE_SKIP);
        markdown.append(table.getDescription()).append(LINE_SKIP);
        markdown.append(COLUMN_TABLE_HEADING + "\n");
        markdown.append(COLUMN_TABLE_DIVIDER + "\n");
        Column[] columns = table.getColumns();
        for (Column column : columns) {
            markdown.append(TABLE_DIVISION);
            markdown.append(column.getName().toUpperCase());
            markdown.append(TABLE_DIVISION);
            markdown.append(column.isPrimaryKey() ? ":ballot_box_with_check:" : "");
            markdown.append(TABLE_DIVISION);
            markdown.append(column.getJdbcTypeName());
            markdown.append(TABLE_DIVISION);
            markdown.append(column.getSize() == null ? "" : column.getSize());
            markdown.append(TABLE_DIVISION);

            markdown.append(column.getDescription());
            markdown.append(TABLE_DIVISION);
            markdown.append("\n");
        }
        markdown.append(LINE_SKIP);
    }

    public void finish(String filename) throws IOException {
        BufferedWriter writer = null;
        if (outputDir != null)
            writer = new BufferedWriter(new FileWriter(outputDir + filename));
        else
            writer = new BufferedWriter(new FileWriter(filename));
        writer.write(header.toString());
        writer.write(markdown.toString());
        writer.close();
    }
}
