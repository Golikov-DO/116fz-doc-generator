package ru.ecospas.web;

import jakarta.servlet.http.HttpServlet;
import ru.ecospas.app.ApplicationContext;
import ru.ecospas.app.InternalServices;

public abstract class BaseServlet extends HttpServlet {
    protected InternalServices services;

    @Override
    public void init() {
        ApplicationContext context = (ApplicationContext) getServletContext()
                .getAttribute("appContext");
        this.services = context.internalServices();
    }
}
