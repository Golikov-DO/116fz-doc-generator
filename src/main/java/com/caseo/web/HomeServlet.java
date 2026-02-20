package com.caseo.web;

import com.caseo.app.ApplicationContext;
import com.caseo.domain.model.DocumentSet;
import com.caseo.domain.model.Organization;
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

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            ApplicationContext context = (ApplicationContext) getServletContext().getAttribute("appContext");

            var docs = context.documentSetService().getAll();
            var organizations = context.organizationService().getAll();

            var orgMap = new java.util.HashMap<Integer, Organization>();
            for (var org : organizations) {
                orgMap.put(org.organizationId(), org);
            }

            var grouped = new LinkedHashMap<Organization, List<DocumentSet>>();

            for (var doc : docs) {
                Organization org = orgMap.get(doc.orgId());
                if (org != null) {
                    grouped.computeIfAbsent(org, k -> new ArrayList<>()).add(doc);
                }
            }

            var existingFiles = scanExistingFiles();
            req.setAttribute("existingFiles", existingFiles);
            req.setAttribute("groupedDocuments", grouped);

            req.getRequestDispatcher("WEB-INF/index.jsp").forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
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