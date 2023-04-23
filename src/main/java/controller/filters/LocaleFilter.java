package controller.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(servletNames = "controller",urlPatterns = "*.jsp")
public class LocaleFilter implements Filter {
    private String defaultLocale;

    @Override
    public void init(FilterConfig filterConfig) {
        defaultLocale="ua";
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        String locale = req.getParameter("locale");
        if (!isBlank(locale)) {
            req.getSession().setAttribute("locale", locale);
        } else {
            String sessionLocale = (String) req.getSession().getAttribute("locale");
            if (isBlank(sessionLocale)) {
                req.getSession().setAttribute("locale", defaultLocale);
            }
        }
        filterChain.doFilter(req, resp);
    }

    @Override
    public void destroy() {

    }

    private boolean isBlank(String locale) {
        return locale == null || locale.isEmpty();
    }
}
