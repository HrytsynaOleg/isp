package controller.impl.tariff;

import controller.ICommand;
import dto.DtoService;
import entity.Service;
import enums.UserRole;
import exceptions.DbConnectionException;
import service.IServicesService;
import service.impl.ServicesService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

import static controller.manager.PathNameManager.getPathName;

public class AddTariffPageCommand implements ICommand {
    private static final IServicesService service = new ServicesService();
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws DbConnectionException {
        UserRole user = (UserRole) request.getSession().getAttribute("role");
        HttpSession session = request.getSession();
        List<DtoService> dtoServiceList=new ArrayList<>();
        List<Service> serviceList =  service.getAllServicesList();
        for (Service item: serviceList) {
            DtoService dtoService = new DtoService(String.valueOf(item.getId()),item.getName(),item.getDescription());
            dtoServiceList.add(dtoService);
        }
        session.setAttribute("servicesList", dtoServiceList);
        session.setAttribute("contentPage", getPathName("content.addTariff"));
        if (user!=null) return user.getMainPage();

        return getPathName("page.login");
    }
}