package ru.ecospas.web;

import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpServlet;
import ru.ecospas.web.plan.GeneratePlanServlet;

import java.io.File;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

@WebListener
public class ServletAutoRegistration implements ServletContextListener {

    // Special cases
    private static final Map<Class<? extends HttpServlet>, String> CUSTOM_PATHS = Map.of(
            WelcomeServlet.class, "",
            GeneratePlanServlet.class, "/generate-plan"
    );

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();

        // List of packages containing servlets
        List<String> packagesToScan = List.of(
                "ru.ecospas.web",
                "ru.ecospas.web.asf",
                "ru.ecospas.web.auth",
                "ru.ecospas.web.object",
                "ru.ecospas.web.organization",
                "ru.ecospas.web.plan",
                "ru.ecospas.web.substance",
                "ru.ecospas.web.user",
                "ru.ecospas.web.type",
                "ru.ecospas.web.region"
        );

        try {
            for (String pkg : packagesToScan) {
                scanAndRegister(pkg, ctx);
            }
            ctx.log("[Auto-Scan] All servlets have been registered successfully.");
        } catch (Exception e) {
            ctx.log("Servlet auto-scan error", e);
            throw new RuntimeException(e);
        }
    }

    private void scanAndRegister(String packageName, ServletContext ctx) throws Exception {
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(path);

        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            File directory = new File(resource.getFile());

            if (directory.exists() && directory.isDirectory()) {
                String[] files = directory.list();
                if (files == null) continue;

                for (String fileName : files) {
                    if (fileName.endsWith(".class")) {
                        String className = packageName + "." + fileName.substring(0, fileName.length() - 6);
                        Class<?> clazz = Class.forName(className);

                        // Check it's a servlet, it's not abstract
                        if (HttpServlet.class.isAssignableFrom(clazz) && !Modifier.isAbstract(clazz.getModifiers())) {
                            registerServlet(ctx, clazz);
                        }
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked") // Safe cast after Runtime type check for HttpServlet assignment
    private void registerServlet(ServletContext ctx, Class<?> clazz) {
        // First, a tough check
        if (!HttpServlet.class.isAssignableFrom(clazz) || Modifier.isAbstract(clazz.getModifiers())) {
            return;
        }

        // Now the cast is safe, since we passed the check above
        Class<? extends HttpServlet> servletClass = (Class<? extends HttpServlet>) clazz;

        String urlPattern;
        if (CUSTOM_PATHS.containsKey(servletClass)) {
            urlPattern = CUSTOM_PATHS.get(servletClass);
        } else {
            urlPattern = "/" + convertToKebabCase(servletClass.getSimpleName());
        }

        var dynamic = ctx.addServlet(servletClass.getSimpleName(), servletClass);
        dynamic.addMapping(urlPattern);
        ctx.log("REGISTER: " + urlPattern + " -> " + servletClass.getName());
        if (servletClass.isAnnotationPresent(MultipartConfig.class)) {
            dynamic.setMultipartConfig(new MultipartConfigElement(""));
        }
    }

    private String convertToKebabCase(String className) {
        String name = className.replace("Servlet", "");
        // Insert a hyphen before capital letters and make everything lowercase.
        return name.replaceAll("([a-z])([A-Z])", "$1-$2").toLowerCase();
    }
}