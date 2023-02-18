package testClass;

import dto.*;
import dto.builder.DtoUserBuilder;
import enums.SortOrder;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class TestDtoTable {

    public static DtoTable getTable() {

        List<DtoTableColumn> columnList = new ArrayList<>();

        IntStream.range(1,5).forEach(e->{
            DtoTableColumn column = new DtoTableColumn("Column"+e,"dbcolumn"+e, SortOrder.UNSORTED,"0","0");
            columnList.add(column);
        });

        DtoTableHead tableHead = new DtoTableHead();
        tableHead.setDtoColumns(columnList);
        tableHead.setSortColumn("1");
        tableHead.setSortOrder(SortOrder.ASC);

        DtoTableSearch tableSearch = new DtoTableSearch();
        DtoTablePagination tablePagination = new DtoTablePagination();


        return new DtoTable("TestDtoTable",tableHead,tableSearch,tablePagination);

    }

}

