package dto;

import enums.SortOrder;

public class DtoTableColumn {
    private String name;
    private String dbColumn;
    private SortOrder sortOrder;
    private String sortable;
    private String searchable;

    public DtoTableColumn(String name, String dbColumn, SortOrder sortOrder, String sortable, String searchable) {
        this.name = name;
        this.dbColumn = dbColumn;
        this.sortOrder = sortOrder;
        this.sortable = sortable;
        this.searchable = searchable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDbColumn() {
        return dbColumn;
    }

    public void setDbColumn(String dbColumn) {
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

    public String getSearchable() {
        return searchable;
    }

    public void setSearchable(String searchable) {
        this.searchable = searchable;
    }
}
