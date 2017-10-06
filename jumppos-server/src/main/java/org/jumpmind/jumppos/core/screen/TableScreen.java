package org.jumpmind.jumppos.core.screen;

import java.util.ArrayList;
import java.util.List;


public class TableScreen extends DefaultScreen {

    private static final long serialVersionUID = 1L;
    
    public enum SelectionMode {
        None,
        MultipleRows,
        SingleRow
    }
    
    private List<String> headerLabels;
    private List<List<String>> tableData = new ArrayList<>();
    private String text;
    private SelectionMode selectionMode = SelectionMode.None;
    private int selectedRow = -1;
    private List<String> submitActionNames = new ArrayList<>();
    
    public TableScreen() {
        setType(ScreenType.Table);
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
    
    public String getText() {
        return this.text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
 
    public SelectionMode getSelectionMode() {
        return selectionMode;
    }
    
    public void setSelectionMode(SelectionMode selectionMode) {
        this.selectionMode = selectionMode;
    }

    public void setSelectedRow(int selectedRow) {
       this.selectedRow = selectedRow;
    }
    
    public int getSelectedRow() {
        return this.selectedRow;
    }

    public List<String> getSubmitActionNames() {
        return submitActionNames;
    }

    public void setSubmitActionNames(List<String> submitActionNames) {
        this.submitActionNames = submitActionNames;
    }
    
    public void addSubmitActionName(String submitActionName) {
        this.getSubmitActionNames().add(submitActionName);
    }
    
}
