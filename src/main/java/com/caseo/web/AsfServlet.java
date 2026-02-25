package com.caseo.web;

import com.caseo.app.ApplicationContext;
import com.caseo.app.InternalServices;
import com.caseo.domain.model.*;
import com.caseo.domain.service.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/asf")
public class AsfServlet extends HttpServlet {

    private AsfService asfService;
    private AsfCertificateService asfCertificateService;
    private AsfCompositionDeploymentFundsService asfCompositionDeploymentFundsService;
    private AsfDocumentImageService asfDocumentImageService;
    private AsfPersonnelService asfPersonnelService;
    private AsfSpecialistsService asfSpecialistsService;
    private AsfSignerService asfSignerService;
    private AsfWorkTypeService asfWorkTypeService;

    @Override
    public void init() {
        ApplicationContext context = (ApplicationContext) getServletContext()
                .getAttribute("appContext");
        InternalServices services = context.internalServices();

        asfService = services.asfService();
        asfCertificateService = services.asfCertificateService();
        asfCompositionDeploymentFundsService = services.asfCompositionDeploymentFundsService();
        asfDocumentImageService = services.asfDocumentImageService();
        asfPersonnelService = services.asfPersonnelService();
        asfSpecialistsService = services.asfSpecialistsService();
        asfSignerService = services.asfSignerService();
        asfWorkTypeService = services.asfWorkTypeService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String mode = req.getParameter("mode"); // view, edit, create
        String asfId = req.getParameter("asfId");

        req.setAttribute("mode", mode);

        try {
            ApplicationContext context = (ApplicationContext) getServletContext()
                    .getAttribute("appContext");

            // Для view и edit загружаем данные
            if (("view".equals(mode) || "edit".equals(mode)) && asfId != null && !asfId.isEmpty()) {
                int id = Integer.parseInt(asfId);

                // Загружаем основную информацию об АСФ
                Asf asf = asfService.getById(id);

                // Загружаем связанные данные
                AsfCertificate certificate = asfCertificateService.getByAsfId(id);
                AsfCompositionDeploymentFunds deployments = asfCompositionDeploymentFundsService.getByAsfId(id);
                List<AsfDocumentImage> images = asfDocumentImageService.getByAsfId(id);
                AsfPersonnel personnel = asfPersonnelService.getByAsfId(id);
                AsfSpecialists specialists = asfSpecialistsService.getByAsfId(id);
                List<AsfSigner> signers = asfSignerService.getAllByAsfId(id);
                List<AsfWorkType> workTypes = asfWorkTypeService.getByAsfId(id);

                // Раскладываем изображения по группам
                List<AsfDocumentImage> appendix1Images = images.stream()
                        .filter(img -> "1".equals(img.groupKey()))
                        .toList();
                List<AsfDocumentImage> appendix2Images = images.stream()
                        .filter(img -> "2".equals(img.groupKey()))
                        .toList();

                // Устанавливаем атрибуты
                req.setAttribute("asf", asf);
                req.setAttribute("certificate", certificate);
                req.setAttribute("deployments", deployments);
                req.setAttribute("personnel", personnel);
                req.setAttribute("specialists", specialists);
                req.setAttribute("asfSigners", signers);
                req.setAttribute("asfWorkTypes", workTypes);
                req.setAttribute("appendix1Images", appendix1Images);
                req.setAttribute("appendix2Images", appendix2Images);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Определяем, откуда пришел запрос
        String requestedWith = req.getHeader("X-Requested-With");

        if ("XMLHttpRequest".equals(requestedWith)) {
            // AJAX запрос из агрегатора - отдаем только фрагмент
            req.getRequestDispatcher("/WEB-INF/fragments/asf/asf.jsp")
                    .forward(req, resp);
        } else {
            // Прямой запрос - отдаем полную страницу
            req.getRequestDispatcher("/WEB-INF/pages/asf-page.jsp")
                    .forward(req, resp);
        }
    }
}