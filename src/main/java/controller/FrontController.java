package controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import static controller.impl.CommandsMap.*;

@WebServlet(value = "", loadOnStartup = 1)
public class FrontController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String commandName = request.getParameter("command");
        ICommand command = COMMANDS_MAP.get(commandName);
        String commandAdress = command.process(request, response);
//        response.sendRedirect(commandAdress);
        request.getRequestDispatcher(commandAdress).forward(request, response);
    }
}
