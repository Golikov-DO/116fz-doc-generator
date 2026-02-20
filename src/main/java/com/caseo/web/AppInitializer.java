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
            // Принудительно регистрируем драйвер PostgreSQL в Tomcat
            Class.forName("org.postgresql.Driver");

            String catalinaBase = System.getProperty("catalina.base");
            if (catalinaBase != null) {
                String tomcatTemp = catalinaBase + File.separator + "temp";
                File tempDir = new File(tomcatTemp);
                if (!tempDir.exists()) {
                    tempDir.mkdirs();
                }
                System.setProperty("java.io.tmpdir", tomcatTemp);
                System.out.println(">>> Custom tmpdir set to: " + tomcatTemp);
            }

            // Теперь инициализируем контекст
            ApplicationContext context = Bootstrap.init();
            sce.getServletContext().setAttribute("appContext", context);

            System.out.println(">>> CASEO Bootstrap initialized");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}