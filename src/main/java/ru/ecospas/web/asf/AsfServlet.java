package ru.ecospas.web.asf;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import ru.ecospas.domain.model.AsfDocumentImage;
import ru.ecospas.domain.repository.OrganizationRepository;
import ru.ecospas.domain.service.SecurityService;
import ru.ecospas.web.BaseServlet;
import ru.ecospas.web.helper.DataLoader;
import ru.ecospas.web.helper.DataLoader.AsfData;

import java.util.List;

import static ru.ecospas.web.util.RequestUtils.param;
import static ru.ecospas.web.util.RequestUtils.paramInt;

@Component
public class AsfServlet extends BaseServlet {

    private final DataLoader dataLoader;

    public AsfServlet(
            SecurityService securityService,
            OrganizationRepository organizationRepository,
            DataLoader dataLoader) {
        super(securityService, organizationRepository);
        this.dataLoader = dataLoader;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException {

        try {
            String mode = param(req, "mode");
            String returnOrgId = param(req, "returnOrgId");

            // 1. Get ID from the request
            int asfId = paramInt(req, "asfId");

            // 2. If ID is missing in the request, try to retrieve it from the session
            if (asfId == 0) {
                Object sessionAsfId = req.getSession().getAttribute("asfId");
                if (sessionAsfId != null) {
                    asfId = (Integer) sessionAsfId;
                }
            }

            // 3. Determine the operation mode
            if (asfId > 0) {
                // If ID exists: save/update it in the session and set default mode to "view"
                req.getSession().setAttribute("asfId", asfId);
                req.setAttribute("asfId", String.valueOf(asfId));
                if (mode == null || mode.isEmpty()) {
                    mode = "view";
                }
            } else {
                // If ID is still 0: switch to "create" mode
                mode = "create";
                // Clear session to prevent old ID from leaking during creation
                req.getSession().removeAttribute("asfId");
            }

            req.setAttribute("mode", mode);
            req.setAttribute("returnOrgId", returnOrgId);

            // Load data only for view/edit modes and if ID is present
            if (("view".equals(mode) || "edit".equals(mode))) {

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
                req.getRequestDispatcher("/WEB-INF/layout.jsp").forward(req, resp);
            }

        } catch (Exception e) {
            getServletContext().log("Ошибка при загрузке АСФ", e);
            throw new ServletException("Ошибка при загрузке АСФ", e);
        }
    }
}