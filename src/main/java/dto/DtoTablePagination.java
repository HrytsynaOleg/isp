package dto;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class DtoTablePagination {
    private int totalRows;
    private int totalPages;
    private int rowsPerPage;
    private int activePage;
    private List<String> pagesList;
    private int startRow;
    private int endRow;

    public DtoTablePagination() {
        this.totalRows = 0;
        this.totalPages = 1;
        this.rowsPerPage = 10;
        this.activePage = 1;
        this.pagesList = new ArrayList<>();
        this.startRow = 0;
        this.endRow = 0;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getRowsPerPage() {
        return rowsPerPage;
    }

    public void setRowsPerPage(int rowsPerPage) {
        this.rowsPerPage = rowsPerPage;
    }

    public int getActivePage() {
        return activePage;
    }

    public void setActivePage(int activePage) {
        this.activePage = activePage;
    }

    public List<String> getPagesList() {
        return pagesList;
    }

    public void setPagesList(List<String> pagesList) {
        this.pagesList = pagesList;
    }

    public int getStartRow() {
        return startRow;
    }

    public void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    public int getEndRow() {
        return endRow;
    }

    public void setEndRow(int endRow) {
        this.endRow = endRow;
    }

    public void setFromRequest(HttpServletRequest request, Integer size) {
        String rowsPerPage = request.getParameter("rowsRerPage");
        String activePage = request.getParameter("page");

        this.setTotalRows(size);

        if (activePage != null)
            this.setActivePage(Integer.parseInt(activePage));

        if (rowsPerPage != null) {
            this.setRowsPerPage(Integer.parseInt(rowsPerPage));
            this.setActivePage(1);
        }

        this.startRow = ((this.activePage - 1) * this.rowsPerPage) + 1;
        if (size == 0) this.startRow = 0;
        this.endRow = Math.min(this.activePage * this.rowsPerPage, this.totalRows);

        int divider = this.rowsPerPage;
        int pagesTotal = (size / divider) + (size % divider != 0 ? 1 : 0);

        this.setTotalPages(pagesTotal == 0 ? 1 : pagesTotal);

        this.pagesList = new ArrayList<>();

        if (this.totalPages <= 4)
            IntStream.range(1, this.totalPages + 1).forEach(e -> this.pagesList.add(String.valueOf(e)));
        else {

            if (this.activePage < 4) {
                IntStream.range(1, 5).forEach(e -> this.pagesList.add(String.valueOf(e)));
                this.pagesList.add("...");
                this.pagesList.add(String.valueOf(this.totalPages));
            } else {
                if (this.activePage > this.totalPages - 4) {
                    this.pagesList.add(String.valueOf(1));
                    this.pagesList.add("...");
                    IntStream.range(this.totalPages - 3, this.totalPages + 1).forEach(e -> this.pagesList.add(String.valueOf(e)));
                } else {
                    this.pagesList.add(String.valueOf(1));
                    this.pagesList.add("...");
                    IntStream.range(this.activePage - 1, this.activePage + 2).forEach(e -> this.pagesList.add(String.valueOf(e)));
                    this.pagesList.add("...");
                    this.pagesList.add(String.valueOf(this.totalPages));
                }
            }

        }

    }
}
