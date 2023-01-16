package controller.impl.tariff;

import com.itextpdf.text.DocumentException;
import controller.ICommand;
import entity.Tariff;
import entity.User;
import enums.FileFormat;
import exceptions.BuildPriceException;
import exceptions.DbConnectionException;
import exceptions.TariffAlreadySubscribedException;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import service.IPriceService;
import service.ITariffsService;
import service.impl.PriceService;
import service.impl.TariffsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.FileNotFoundException;
import java.util.List;

import static controller.manager.PathNameManager.getPathName;

public class DownloadTariffCommand implements ICommand {
    private static final ITariffsService service = new TariffsService();
    private static final IPriceService priceService = new PriceService();

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        User loggedUser = (User) session.getAttribute("loggedUser");
        FileFormat format = FileFormat.valueOf(request.getParameter("fileFormat"));
        String filePath ;
        try {
            List<Tariff> tariffList = service.getPriceTariffsList();
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
