package com.caseo.web;

import com.caseo.app.ApplicationContext;
import com.caseo.app.InternalServices;
import com.caseo.domain.model.AsfDocumentImage;
import com.caseo.web.helper.DataLoader;
import com.caseo.web.helper.DataLoader.AsfData;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/asf")
public class AsfServlet extends HttpServlet {

    private DataLoader dataLoader;

    @Override
    public void init() {
        ApplicationContext context = (ApplicationContext) getServletContext()
                .getAttribute("appContext");
        InternalServices services = context.internalServices();
        dataLoader = new DataLoader(services);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String mode = req.getParameter("mode");
        String asfId = req.getParameter("asfId");

        req.setAttribute("mode", mode);

        try {
            if (("view".equals(mode) || "edit".equals(mode)) && asfId != null && !asfId.isEmpty()) {
                int id = Integer.parseInt(asfId);

                // Одна строка вместо 30!
                AsfData data = dataLoader.loadAsf(id);

                // Раскладываем изображения по группам
                List<AsfDocumentImage> appendix1Images = data.images().stream()
                        .filter(img -> "1".equals(img.getGroupKey()))
                        .toList();
                List<AsfDocumentImage> appendix2Images = data.images().stream()
                        .filter(img -> "2".equals(img.getGroupKey()))
                        .toList();

                req.setAttribute("asf", data.asf());
                req.setAttribute("certificate", data.certificate());
                req.setAttribute("deployments", data.deployment());
                req.setAttribute("personnel", data.personnel());
                req.setAttribute("specialists", data.specialists());
                req.setAttribute("asfSigners", data.signers());
                req.setAttribute("asfWorkTypes", data.workTypes());
                req.setAttribute("appendix1Images", appendix1Images);
                req.setAttribute("appendix2Images", appendix2Images);
            }

        } catch (Exception e) {
            getServletContext().log("Ошибка при загрузке АСФ", e);
        }

        String requestedWith = req.getHeader("X-Requested-With");

        if ("XMLHttpRequest".equals(requestedWith)) {
            req.getRequestDispatcher("/WEB-INF/fragments/asf/asf.jsp")
                    .forward(req, resp);
        } else {
            req.getRequestDispatcher("/WEB-INF/pages/asf-page.jsp")
                    .forward(req, resp);
        }
    }
}