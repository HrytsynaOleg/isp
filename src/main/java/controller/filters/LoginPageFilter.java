package controller.filters;

import enums.UserRole;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@WebFilter(urlPatterns = "/*")
public class LoginPageFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        HttpSession session = req.getSession();

        if (isLoggedIn(session)) {
            if (isLoginPage(req) || isMainPage(req) ) {
                UserRole userRole = (UserRole) session.getAttribute("role");
                resp.sendRedirect(userRole.getMainPage());
            } else filterChain.doFilter(servletRequest, servletResponse);
        } else filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }

    private boolean isLoginPage(HttpServletRequest req) {
        return req.getRequestURI().endsWith("login.jsp");
    }


    private boolean isMainPage(HttpServletRequest req) {
        String mainURI = req.getContextPath() + "/";
        return req.getRequestURI().equals(mainURI);
    }

    private boolean isLoggedIn(HttpSession session) {
        return session != null && session.getAttribute("loggedUser") != null;
    }
}
