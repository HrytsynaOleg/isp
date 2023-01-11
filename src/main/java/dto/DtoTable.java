package dto;

public class DtoTable {
    private String name;
    private DtoTableHead head;
    private DtoTableSearch search;
    private DtoTablePagination pagination;

    public DtoTable(String name,DtoTableHead head, DtoTableSearch search, DtoTablePagination pagination) {
        this.name=name;
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
}
