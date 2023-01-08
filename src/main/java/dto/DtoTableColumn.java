package dto;

import enums.SortOrder;

public class DtoTableColumn {
    private String name;
    private int dbColumn;
    private SortOrder sortOrder;
    private String sortable;

    public DtoTableColumn(String name, int dbColumn, SortOrder sortOrder, String sortable) {
        this.name = name;
        this.dbColumn = dbColumn;
        this.sortOrder = sortOrder;
        this.sortable = sortable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDbColumn() {
        return dbColumn;
    }

    public void setDbColumn(int dbColumn) {
        this.dbColumn = dbColumn;
    }

    public SortOrder getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(SortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getSortable() {
        return sortable;
    }

    public void setSortable(String sortable) {
        this.sortable = sortable;
    }
}
