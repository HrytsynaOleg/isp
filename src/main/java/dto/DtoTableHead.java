package dto;

import enums.SortOrder;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static controller.manager.TableHeadManager.getColumns;

public class DtoTableHead {
    private List<DtoTableColumn> dtoColumns;
    private int sortColumn;
    private SortOrder sortOrder;

    private DtoTableHead() {
    }

    public int getSortColumn() {
        return sortColumn;
    }

    public void setSortColumn(int sortColumn) {
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
            this.setSorting(Integer.parseInt(request.getParameter("sortBy")),
                    SortOrder.valueOf(request.getParameter("orderBy")));
        }
    }

    public static DtoTableHead build(String columns) {
        String[] columnList = columns.split(",");
        List<DtoTableColumn> dtoColumnList = new ArrayList<>();
        for (String column : columnList) {
            String[] columnParam = getColumns(column).split(",");
            DtoTableColumn dtoTableColumn = new DtoTableColumn(column, Integer.parseInt(columnParam[0]),
                    SortOrder.UNSORTED, columnParam[1]);
            dtoColumnList.add(dtoTableColumn);
        }
        DtoTableHead result = new DtoTableHead();
        result.setDtoColumns(dtoColumnList);
        result.setSorting(1, SortOrder.ASC);
        return result;
    }

    private void setSorting(int column, SortOrder order) {

        for (DtoTableColumn dtoColumn : this.dtoColumns) {
            dtoColumn.setSortOrder(SortOrder.UNSORTED);
            if (dtoColumn.getDbColumn() == column) dtoColumn.setSortOrder(order);
        }
        this.sortOrder = order;
        this.sortColumn = column;

    }


}


