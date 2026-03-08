package com.caseo.web;

import com.caseo.app.ApplicationContext;
import com.caseo.app.InternalServices;
import com.caseo.domain.model.ObjectModel;
import com.caseo.domain.model.Organization;
import com.caseo.domain.service.ChildService;
import com.caseo.domain.service.ParentService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet(value = "", loadOnStartup = 1)
public class HomeServlet extends HttpServlet {

    private InternalServices services;

    @Override
    public void init() {
        ApplicationContext context = (ApplicationContext) getServletContext()
                .getAttribute("appContext");
        services = context.internalServices();
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            // Получаем сервисы
            ParentService<Organization> orgService = services.getParentService(Organization.class);
            ChildService<ObjectModel> objService = services.getChildService(ObjectModel.class);

            // Загружаем все организации
            List<Organization> organizations = orgService.getMany();

            // Группируем объекты по организациям
            var grouped = new LinkedHashMap<Organization, List<ObjectModel>>();

            for (var org : organizations) {
                List<ObjectModel> objects = objService.getManyByParentId(org.getId());
                if (objects != null && !objects.isEmpty()) {
                    grouped.put(org, objects);
                }
            }

            var existingFiles = scanExistingFiles();
            req.setAttribute("existingFiles", existingFiles);
            req.setAttribute("groupedDocuments", grouped);

            req.getRequestDispatcher("WEB-INF/index.jsp").forward(req, resp);

        } catch (Exception e) {
            getServletContext().log("Ошибка в HomeServlet", e);
        }
    }

    private Map<String, List<String>> scanExistingFiles() {
        String outputDir = System.getProperty("user.home") + File.separator + "documents";
        File root = new File(outputDir);
        Map<String, List<String>> tree = new LinkedHashMap<>();

        if (root.exists() && root.isDirectory()) {
            File[] dirs = root.listFiles(File::isDirectory);
            if (dirs != null) {
                for (File dir : dirs) {
                    File[] files = dir.listFiles((d, name) -> name.endsWith(".docx"));
                    if (files != null && files.length > 0) {
                        List<String> names = new ArrayList<>();
                        for (File f : files) names.add(f.getName());
                        tree.put(dir.getName(), names);
                    }
                }
            }
        }
        return tree;
    }
}