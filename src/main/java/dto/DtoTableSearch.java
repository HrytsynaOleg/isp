package dto;

import enums.SortOrder;

import javax.servlet.http.HttpServletRequest;

public class DtoTableSearch {
    private int searchColumn;
    private String searchCriteria;

    public DtoTableSearch() {
        this.searchColumn=0;
        this.searchCriteria="";
    }

    public int getSearchColumn() {
        return searchColumn;
    }

    public void setSearchColumn(int searchColumn) {
        this.searchColumn = searchColumn;
    }

    public String getSearchCriteria() {
        return searchCriteria;
    }

    public void setSearchCriteria(String searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    public void setFromRequest(HttpServletRequest request) {

        if (request.getParameter("searchBy") != null) {
            this.setSearchColumn(Integer.parseInt(request.getParameter("searchBy")));
            this.setSearchCriteria(request.getParameter("searchString"));
        }
    }
}
