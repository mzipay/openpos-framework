package org.jumpmind.jumppos.core.screen;

import java.util.ArrayList;
import java.util.List;

public class TableScreen extends DefaultScreen {

    private static final long serialVersionUID = 1L;
    
    private List<String> headerLabels;
    private List<List<String>> tableData = new ArrayList<>();
    
    public TableScreen() {
        setType(TABLE_SCREEN_TYPE);
    }
    
    public List<String> getHeaderLabels() {
        return headerLabels;
    }
    
    public void setHeaderLabels(List<String> headerLabels) {
        this.headerLabels = headerLabels;
    }
    
    public List<List<String>> getTableData() {
        return tableData;
    }
    
    public void setTableData(List<List<String>> tableData) {
        this.tableData = tableData;
    }
    
    public void addRow(List<String> tableRow) {
        this.getTableData().add(tableRow);
    }
    
    
}
