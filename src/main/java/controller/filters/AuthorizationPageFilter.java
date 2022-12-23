package controller.filters;

import entity.User;
import enums.UserRole;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static controller.manager.PathNameManager.*;


@WebFilter(urlPatterns = "/*")
public class AuthorizationPageFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        UserRole userRole = UserRole.ANONYMOUS;
        HttpSession session = req.getSession();
        String pageName = req.getServletPath().replace("//", "/").substring(1);
        String commandName = req.getParameter("command");
        if (isLoggedIn(session)) {
            userRole = ((User) session.getAttribute("loggedUser")).getRole();
        }
        if (!userRole.isPageAccessAllowed(pageName)) {
            session.setAttribute("errorText", "Page access error.");
            resp.sendRedirect(getPathName("page.error"));
        } else if (commandName!=null && !userRole.isCommandAccessAllowed(commandName)) {
            session.setAttribute("errorText", "Command access error.");
            resp.sendRedirect(getPathName("page.error"));
        } else filterChain.doFilter(servletRequest, servletResponse);
    }
    @Override
    public void destroy() {

    }
    private boolean isLoggedIn(HttpSession session) {
        return session != null && session.getAttribute("loggedUser") != null;
    }
}
