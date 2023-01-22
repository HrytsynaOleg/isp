package service.impl;

import dto.DtoTable;
import dto.DtoTableHead;
import dto.DtoTablePagination;
import dto.DtoTableSearch;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

import static controller.manager.TableHeadManager.getColumns;

public class DtoTablesService {
    private static DtoTablesService instance;
    private static Map<String,DtoTable> tableMap;

    private DtoTablesService() {
        tableMap=new HashMap<>();
    }

    public DtoTable getDtoTable(String name) {
            DtoTable table = tableMap.get(name);
            if (table == null) {
                table = new DtoTable(name, DtoTableHead.build(getColumns(name)),
                        new DtoTableSearch(), new DtoTablePagination());
                addTable(table);
            }
            return table;
    }

    public void addTable(DtoTable table) {
        tableMap.put(table.getName(), table);
    }

    public void updateSessionDtoTable(HttpSession session, DtoTable dtoTable) {

        session.setAttribute("tableHead", dtoTable.getHead());
        session.setAttribute("tableSearch", dtoTable.getSearch());
        session.setAttribute("tablePagination", dtoTable.getPagination());
        addTable(dtoTable);
    }


    public static DtoTablesService getInstance() {
        if (instance == null) {
            instance = new DtoTablesService();
        }
        return instance;
    }

}
