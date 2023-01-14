package controller.impl.tariff;

import controller.ICommand;
import dto.DtoService;
import dto.DtoTariff;
import entity.Service;
import entity.Tariff;
import entity.User;
import enums.TariffStatus;
import exceptions.DbConnectionException;
import service.IServicesService;
import service.ITariffsService;
import service.impl.ServicesService;
import service.impl.TariffsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

import static controller.manager.PathNameManager.getPathName;

public class EditTariffPageCommand implements ICommand {
    private static final ITariffsService service = new TariffsService();
    private static final IServicesService servicesService = new ServicesService();

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) throws DbConnectionException {
        HttpSession session = request.getSession();
        User loggedUser = (User) session.getAttribute("loggedUser");
        String tariffId = request.getParameter("tariffId");
        List<DtoService> dtoServiceList=new ArrayList<>();
        List<Service> serviceList =  servicesService.getAllServicesList();
        for (Service item: serviceList) {
            DtoService dtoService = new DtoService(String.valueOf(item.getId()),item.getName(),item.getDescription());
            dtoServiceList.add(dtoService);
        }
        List<String> statusList = TariffStatus.getStatusList();

        try {
            Tariff tariffEdit = service.getTariff(Integer.parseInt(tariffId));
            DtoTariff dtoTariff = new DtoTariff(String.valueOf(tariffEdit.getId()),tariffEdit.getName(),tariffEdit.getDescription(),
                    tariffEdit.getService().getName(),tariffEdit.getStatus().toString(),tariffEdit.getPrice().toString(),tariffEdit.getPeriod().toString());
            session.setAttribute("editTariff",dtoTariff);

        } catch (DbConnectionException e) {
            session.setAttribute("contentPage", getPathName("content.tariffsList"));
            session.setAttribute("alert", e.getMessage());
            return loggedUser.getRole().getMainPage();
        }
        session.setAttribute("statusList", statusList);
        session.setAttribute("servicesList", dtoServiceList);
        session.setAttribute("contentPage", getPathName("content.editTariff"));

        return loggedUser.getRole().getMainPage();
    }

}
