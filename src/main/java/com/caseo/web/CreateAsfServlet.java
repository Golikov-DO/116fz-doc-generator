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

@WebServlet("/createAsf")
@MultipartConfig(
        maxFileSize = 1024 * 1024 * 8,
        maxRequestSize = 1024 * 1024 * 20
)
public class CreateAsfServlet extends HttpServlet {

    private ParentService<Asf> asfService;
    private AsfSaveHelper asfSaveHelper;  // helper

    @Override
    public void init() {
        ApplicationContext context = (ApplicationContext) getServletContext()
                .getAttribute("appContext");
        InternalServices services = context.internalServices();
        asfService = services.getParentService(Asf.class);
        asfSaveHelper = new AsfSaveHelper();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String returnMode = req.getParameter("returnMode");
        String returnOrgId = req.getParameter("returnOrgId");

        try {
            int savedAsfId = saveAsf(req);

            if (returnMode != null && returnOrgId != null) {
                resp.sendRedirect("portal?mode=" + returnMode + "&orgId=" + returnOrgId + "&asfId=" + savedAsfId);
            } else {
                resp.sendRedirect("asf?mode=edit&asfId=" + savedAsfId);
            }

        } catch (Exception e) {
            getServletContext().log("Ошибка при создании АСФ", e);
            req.setAttribute("error", "Ошибка: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/pages/asf-page.jsp").forward(req, resp);
        }
    }

    private int saveAsf(HttpServletRequest req) {
        // 1. Создаем ASF через helper
        Asf asf = asfSaveHelper.createAsf(req);

        // 2. Добавляем все связанные сущности через helper
        asfSaveHelper.addCertificate(req, asf);
        asfSaveHelper.addPersonnel(req, asf);
        asfSaveHelper.addSpecialists(req, asf);
        asfSaveHelper.addDeployment(req, asf);
        asfSaveHelper.addSigners(req, asf);
        asfSaveHelper.addWorkTypes(req, asf);
        asfSaveHelper.addImages(req, asf);

        // 3. ОДИН save!
        asfService.save(asf);

        return asf.getId();
    }
}