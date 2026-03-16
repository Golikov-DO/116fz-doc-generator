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

import static com.caseo.web.util.RequestUtils.param;
import static com.caseo.web.util.RequestUtils.paramInt;

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

        try {
            String mode = param(req, "mode");
            String returnOrgId = param(req, "returnOrgId");
            Integer asfId = paramInt(req, "asfId");

            // 0 означает создание новой АСФ
            if (asfId != null && asfId == 0) {
                asfId = null;
                mode = "create";
            }

            if (asfId != null) {
                req.getSession().setAttribute("asfId", asfId);
                req.setAttribute("asfId", asfId.toString());
            }

            if (asfId == null && req.getSession().getAttribute("asfId") != null) {
                asfId = (Integer) req.getSession().getAttribute("asfId");
            }

            req.setAttribute("mode", mode);
            req.setAttribute("returnOrgId", returnOrgId);

            // Загружаем данные только для режимов просмотра/редактирования и если есть ID
            if (("view".equals(mode) || "edit".equals(mode)) && asfId != null) {

                AsfData data = dataLoader.loadAsf(asfId);

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

            String requestedWith = req.getHeader("X-Requested-With");

            if ("XMLHttpRequest".equals(requestedWith)) {
                req.getRequestDispatcher("/WEB-INF/fragments/asf/asf.jsp")
                        .forward(req, resp);
            } else {
                req.setAttribute("contentPage", "/WEB-INF/pages/asf-page.jsp");
                req.getRequestDispatcher("/WEB-INF/template/layout.jsp").forward(req, resp);
            }

        } catch (Exception e) {
            getServletContext().log("Ошибка при загрузке АСФ", e);
            throw new ServletException("Ошибка при загрузке АСФ", e);
        }
    }
}