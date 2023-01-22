package dto;

import java.util.HashMap;
import java.util.Map;

public class DtoTable {
    private String name;
    private DtoTableHead head;
    private DtoTableSearch search;
    private DtoTablePagination pagination;

    public DtoTable(String name, DtoTableHead head, DtoTableSearch search, DtoTablePagination pagination) {
        this.name = name;
        this.head = head;
        this.search = search;
        this.pagination = pagination;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DtoTableHead getHead() {
        return head;
    }

    public void setHead(DtoTableHead head) {
        this.head = head;
    }

    public DtoTableSearch getSearch() {
        return search;
    }

    public void setSearch(DtoTableSearch search) {
        this.search = search;
    }

    public DtoTablePagination getPagination() {
        return pagination;
    }

    public void setPagination(DtoTablePagination pagination) {
        this.pagination = pagination;
    }

    public Map<String, String> buildQueryParameters() {
        Map<String, String> resultMap = new HashMap<>();
        int startRow = pagination.getStartRow() == 0 ? 0 : pagination.getStartRow() - 1;
        resultMap.put("limit", String.valueOf(startRow));
        resultMap.put("total", String.valueOf(pagination.getRowsPerPage()));
        resultMap.put("sortColumn", String.valueOf(head.getSortColumn()));
        resultMap.put("order", String.valueOf(head.getSortOrder()));
        resultMap.put("searchColumn", String.valueOf(search.getSearchColumn()));
        resultMap.put("criteria", String.valueOf(search.getSearchCriteria()));
        return resultMap;

    }
}
