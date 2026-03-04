package com.caseo.web;

import com.caseo.app.ApplicationContext;
import com.caseo.app.InternalServices;
import com.caseo.domain.model.*;
import com.caseo.domain.service.*;
import com.caseo.web.helper.AsfSaveHelper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/updateAsf")
@MultipartConfig(
        maxFileSize = 1024 * 1024 * 8,
        maxRequestSize = 1024 * 1024 * 20
)
public class UpdateAsfServlet extends HttpServlet {

    private AsfService asfService;
    private AsfCertificateService asfCertificateService;
    private AsfCompositionDeploymentFundsService asfCompositionDeploymentFundsService;
    private AsfPersonnelService asfPersonnelService;
    private AsfSpecialistsService asfSpecialistsService;
    private AsfSignerService asfSignerService;
    private AsfWorkTypeService asfWorkTypeService;
    private AsfDocumentImageService asfDocumentImageService;
    private AsfSaveHelper asfSaveHelper;

    @Override
    public void init() {
        ApplicationContext context = (ApplicationContext) getServletContext()
                .getAttribute("appContext");
        InternalServices services = context.internalServices();

        asfService = services.asfService();
        asfCertificateService = services.asfCertificateService();
        asfCompositionDeploymentFundsService = services.asfCompositionDeploymentFundsService();
        asfPersonnelService = services.asfPersonnelService();
        asfSpecialistsService = services.asfSpecialistsService();
        asfSignerService = services.asfSignerService();
        asfWorkTypeService = services.asfWorkTypeService();
        asfDocumentImageService = services.asfDocumentImageService();

        asfSaveHelper = new AsfSaveHelper(
                asfCertificateService,
                asfCompositionDeploymentFundsService,
                asfPersonnelService,
                asfSpecialistsService,
                asfSignerService,
                asfWorkTypeService,
                asfDocumentImageService
        );
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String mode = req.getParameter("mode");
        String asfId = req.getParameter("asfId");
        String returnMode = req.getParameter("returnMode");
        String returnDocId = req.getParameter("returnDocId");

        try {
            int savedAsfId = updateAsf(req, Integer.parseInt(asfId));

            if (returnMode != null && returnDocId != null) {
                resp.sendRedirect("portal?mode=" + returnMode + "&docId=" + returnDocId + "&asfId=" + savedAsfId);
            } else {
                resp.sendRedirect("asf?mode=edit&asfId=" + savedAsfId);
            }

        } catch (Exception e) {
            getServletContext().log("Ошибка при обновлении АСФ", e);
            req.setAttribute("error", "Ошибка: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/pages/asf-page.jsp").forward(req, resp);
        }
    }

    private int updateAsf(HttpServletRequest req, int asfId) throws Exception {
        String hours = req.getParameter("arrival_hours");
        String minutes = req.getParameter("arrival_minutes");
        String arrivalTime = null;
        if (hours != null && !hours.isEmpty() && minutes != null && !minutes.isEmpty()) {
            arrivalTime = String.format("%s:%s:00", hours, minutes);
        } else if (hours != null && !hours.isEmpty()) {
            arrivalTime = String.format("%s:00:00", hours);
        }

        Asf asf = new Asf(
                asfId,
                req.getParameter("full_name"),
                req.getParameter("full_name_gen"),
                req.getParameter("short_name"),
                req.getParameter("email"),
                req.getParameter("status_short"),
                arrivalTime
        );

        Asf savedAsf = asfService.save(asf);
        int savedAsfId = savedAsf.id();

        // Удаляем старые связанные записи
        asfCertificateService.deleteByAsfId(savedAsfId);
        asfCompositionDeploymentFundsService.deleteByAsfId(savedAsfId);
        asfPersonnelService.deleteByAsfId(savedAsfId);
        asfSpecialistsService.deleteByAsfId(savedAsfId);
        asfSignerService.deleteByAsfId(savedAsfId);
        asfWorkTypeService.deleteByAsfId(savedAsfId);

        // Сохраняем новые
        asfSaveHelper.saveRelatedEntities(req, savedAsfId);

        return savedAsfId;
    }
}