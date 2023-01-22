package dto;

import enums.SortOrder;

import javax.servlet.http.HttpServletRequest;

public class DtoTableSearch {
    private String searchColumn;
    private String searchCriteria;

    public DtoTableSearch() {
        this.searchColumn="0";
        this.searchCriteria="";
    }

    public String getSearchColumn() {
        return searchColumn;
    }

    public void setSearchColumn(String searchColumn) {
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
            String searchBy = request.getParameter("searchBy");
            this.setSearchColumn(request.getParameter("searchBy"));
            this.setSearchCriteria(request.getParameter("searchString"));
            if (searchBy.equals("0")) {
                this.setSearchCriteria("");
            }
        }

    }
}
