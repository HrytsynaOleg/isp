package controller;

import controller.manager.PathNameManager;
import exceptions.DbConnectionException;

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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        process(req, resp);
//        req.getRequestDispatcher(procerocess(req,resp)).forward(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        process(req, resp);
//        resp.sendRedirect(process(req,resp));
    }

    private void process(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String commandName = request.getParameter("command");
        String commandAdress = PathNameManager.getPathName("page.login");
        if (commandName != null) {
            ICommand command = COMMANDS_MAP.get(commandName);
            try {
                commandAdress = command.process(request, response);
            } catch (DbConnectionException e) {
                commandAdress = PathNameManager.getPathName("page.error");
            }
        }
//        return commandAdress;
        response.sendRedirect(commandAdress);
    }
}
