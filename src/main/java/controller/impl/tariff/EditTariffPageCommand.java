package controller.impl.tariff;

import controller.ICommand;
import dto.DtoService;
import dto.DtoTariff;
import entity.Service;
import entity.Tariff;
import entity.User;
import enums.TariffStatus;
import enums.UserRole;
import exceptions.DbConnectionException;
import service.IServicesService;
import service.ITariffsService;
import service.MapperService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.List;

import static settings.properties.PathNameManager.getPathName;

public class EditTariffPageCommand implements ICommand {
    private final ITariffsService tariffService;
    private final IServicesService servicesService;

    public EditTariffPageCommand(ITariffsService tariffService, IServicesService servicesService) {
        this.tariffService = tariffService;
        this.servicesService = servicesService;
    }

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        UserRole userRole = (UserRole) session.getAttribute("role");
        User loggedUser = (User) session.getAttribute("loggedUser");

        if (userRole == null || loggedUser == null) {
            session.invalidate();
            return getPathName("page.login");
        }
        String tariffId = request.getParameter("tariffId");

        try {
            List<Service> serviceList = servicesService.getAllServicesList();
            List<DtoService> dtoServiceList = MapperService.toDtoServiceList(serviceList);
            List<String> statusList = TariffStatus.getStatusList();
            Tariff tariffEdit = tariffService.getTariff(Integer.parseInt(tariffId));
            DtoTariff dtoTariff = MapperService.toDtoTariff(tariffEdit);
            session.setAttribute("editTariff", dtoTariff);
            session.setAttribute("statusList", statusList);
            session.setAttribute("servicesList", dtoServiceList);
            session.setAttribute("contentPage", getPathName("content.editTariff"));
        } catch (DbConnectionException e) {
            session.setAttribute("contentPage", getPathName("content.tariffsList"));
            session.setAttribute("alert", e.getMessage());
        }

        return loggedUser.getRole().getMainPage();
    }

}
