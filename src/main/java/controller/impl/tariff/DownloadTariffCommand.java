package controller.impl.tariff;

import controller.ICommand;
import entity.Tariff;
import entity.User;
import enums.FileFormat;
import exceptions.BuildPriceException;
import exceptions.DbConnectionException;
import service.ITariffsService;
import service.PriceService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.List;

import static settings.properties.PathNameManager.getPathName;

public class DownloadTariffCommand implements ICommand {
    private final ITariffsService tariffService;
    private final PriceService priceService;

    public DownloadTariffCommand(ITariffsService tariffService, PriceService priceService) {
        this.tariffService = tariffService;
        this.priceService = priceService;
    }

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        User loggedUser = (User) session.getAttribute("loggedUser");
        FileFormat format = FileFormat.valueOf(request.getParameter("fileFormat"));
        String filePath ;
        try {
            List<Tariff> tariffList = tariffService.getPriceTariffsList();
            filePath = priceService.createPrice(tariffList, format);

        } catch (DbConnectionException | BuildPriceException e) {
            session.setAttribute("contentPage", getPathName("content.tariffsUserList"));
            session.setAttribute("alert", e.getMessage());
            return loggedUser.getRole().getMainPage();
        }
        session.setAttribute("contentPage", getPathName("content.tariffsUserList"));

        return "download?path=" + filePath + "&format=" + format;
    }

}
