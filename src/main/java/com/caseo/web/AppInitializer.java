package com.caseo.web;

import com.caseo.app.ApplicationContext;
import com.caseo.app.Bootstrap;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.io.File;

@WebListener
public class AppInitializer implements ServletContextListener {

    public void contextInitialized(ServletContextEvent sce) {
        try {
            Class.forName("org.postgresql.Driver");

            // Настройка путей для docx4j
            String tomcatTemp = System.getProperty("catalina.base") + File.separator + "temp";
            System.setProperty("docx4j.tmpdir", tomcatTemp);
            System.setProperty("java.io.tmpdir", tomcatTemp);

            ApplicationContext context = Bootstrap.init();
            sce.getServletContext().setAttribute("appContext", context);
        } catch (Exception e) {
            sce.getServletContext().log("Ошибка инициализации", e);
        }
    }

}