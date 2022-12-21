package controller;

import controller.manager.PathNameManager;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static controller.impl.CommandsMap.*;

@WebServlet(name = "controller", urlPatterns = "/controller", loadOnStartup = 1)
public class FrontController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        process(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        process(req, resp);
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String commandName = request.getParameter("command");
//        String commandAdress = request.getRequestURI();
        String commandAdress = PathNameManager.getPathName("page.login");
        if (commandName != null) {
            ICommand command = COMMANDS_MAP.get(commandName);
            commandAdress = command.process(request, response);
        }
        response.sendRedirect(commandAdress);
    }
}
