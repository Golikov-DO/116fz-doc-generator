package ru.ecospas.web;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.ecospas.domain.model.User;
import ru.ecospas.infrastructure.config.HibernateConfig;

import java.io.IOException;
import java.util.List;

@WebFilter("/*")
public class AuthFilter implements Filter {

    private static final List<String> ALLOWED_PATHS = List.of(
            "/", "/login", "/guest",
            "/css/", "/js/", "/images/"
    );

    @Override
    public void init(FilterConfig filterConfig) {
        try {
            HibernateConfig.getSessionFactory().openSession().close();
        } catch (Exception e) {
            filterConfig.getServletContext().log("Error initializing Hibernate", e);
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String path = req.getRequestURI();
        String context = req.getContextPath();
        String relativePath = path.substring(context.length());

        // permitted paths
        boolean allowed = ALLOWED_PATHS.stream().anyMatch(relativePath::startsWith);

        if (allowed) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);

        if (session == null) {
            resp.sendRedirect(context + "/");
            return;
        }

        User user = (User) session.getAttribute("user");
        Boolean guest = (Boolean) session.getAttribute("guest");

        if (user == null && (guest == null || !guest)) {
            resp.sendRedirect(context + "/");
            return;
        }

        chain.doFilter(request, response);
    }
}