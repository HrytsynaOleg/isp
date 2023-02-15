package dto;

import enums.SortOrder;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static settings.properties.TableHeadManager.getColumns;

public class DtoTableHead {
    private List<DtoTableColumn> dtoColumns;
    private String sortColumn;
    private SortOrder sortOrder;

    public DtoTableHead() {
    }

    public String getSortColumn() {
        return sortColumn;
    }

    public void setSortColumn(String sortColumn) {
        this.sortColumn = sortColumn;
    }

    public SortOrder getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(SortOrder sortOrder) {
        this.sortOrder = sortOrder;
    }

    public List<DtoTableColumn> getDtoColumns() {
        return dtoColumns;
    }

    public void setDtoColumns(List<DtoTableColumn> dtoColumns) {
        this.dtoColumns = dtoColumns;
    }


    public void setFromRequest(HttpServletRequest request) {
        if (request.getParameter("sortBy") != null && request.getParameter("orderBy") != null) {
            this.setSorting(request.getParameter("sortBy"),
                    SortOrder.valueOf(request.getParameter("orderBy")));
        }
    }

    public static DtoTableHead build(String columns) {
        String[] columnList = columns.split(",");
        List<DtoTableColumn> dtoColumnList = new ArrayList<>();
        for (String column : columnList) {
            String[] columnParam = getColumns(column).split(",");
            DtoTableColumn dtoTableColumn = new DtoTableColumn(column, columnParam[0],
                    SortOrder.UNSORTED, columnParam[1], columnParam[2]);
            dtoColumnList.add(dtoTableColumn);
        }
        DtoTableHead result = new DtoTableHead();
        result.setDtoColumns(dtoColumnList);
        result.setSorting(getColumns(columnList[0]).split(",")[0], SortOrder.ASC);
        return result;
    }

    private void setSorting(String column, SortOrder order) {

        for (DtoTableColumn dtoColumn : this.dtoColumns) {
            dtoColumn.setSortOrder(SortOrder.UNSORTED);
            if (dtoColumn.getDbColumn().equals(column)) dtoColumn.setSortOrder(order);
        }
        this.sortOrder = order;
        this.sortColumn = column;

    }


}


