package controller.filters;

import enums.UserRole;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@WebFilter(urlPatterns = {"/*"})
public class AuthFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        HttpSession session = req.getSession();
        UserRole userRole = UserRole.UNKNOWN;

        if (session != null & session.getAttribute("login") != null & session.getAttribute("role") != null) {

            if (isCommandExist(req, "logoutUser")) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
            userRole = (UserRole) session.getAttribute("role");
            System.out.println(session.getAttribute("login") + " role: " + session.getAttribute("role").toString());
            moveToPage(req, resp, userRole);
        } else {
            if (req.getParameter("login") == null) {
                moveToPage(req, resp, userRole);
            } else filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }

    private void moveToPage(HttpServletRequest req, HttpServletResponse resp, UserRole role) throws ServletException, IOException {
        if (role.equals(UserRole.USER)) req.getRequestDispatcher("/WEB-INF/view/user_page.jsp").forward(req, resp);
        else if (role.equals(UserRole.ADMIN))
            req.getRequestDispatcher("/WEB-INF/view/admin_page.jsp").forward(req, resp);
        else req.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(req, resp);
    }

    private boolean isCommandExist(HttpServletRequest req, String command) {
        String requestCommand = req.getParameter("command");
        if (requestCommand == null) return false;
        return requestCommand.equals(command);
    }
}
