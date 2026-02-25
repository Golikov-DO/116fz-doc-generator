package com.caseo.web;

import com.caseo.app.ApplicationContext;
import com.caseo.app.Bootstrap;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class AppInitializer implements ServletContextListener {

    public void contextInitialized(ServletContextEvent sce) {
        try {
            // Принудительно регистрируем драйвер PostgreSQL в Tomcat
            Class.forName("org.postgresql.Driver");

            // Теперь инициализируем контекст
            ApplicationContext context = Bootstrap.init();
            sce.getServletContext().setAttribute("appContext", context);

            System.out.println(">>> CASEO Bootstrap initialized");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}