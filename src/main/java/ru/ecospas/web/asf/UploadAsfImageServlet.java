package ru.ecospas.web.asf;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import ru.ecospas.domain.service.AsfDocumentImageService;
import ru.ecospas.domain.service.CurrentUserService;
import ru.ecospas.domain.service.SecurityService;
import ru.ecospas.domain.repository.OrganizationRepository;
import ru.ecospas.web.BaseServlet;

@Component
@MultipartConfig
public class UploadAsfImageServlet extends BaseServlet {

    private final AsfDocumentImageService imageService;

    public UploadAsfImageServlet(
            SecurityService securityService,
            OrganizationRepository organizationRepository,
            AsfDocumentImageService imageService,
            CurrentUserService currentUserService
    ) {

        super(securityService, organizationRepository, currentUserService);
        this.imageService = imageService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException {

        try {

            Integer imageId = imageService.upload(req);

            resp.setContentType("application/json");
            resp.getWriter().write("{\"id\":" + imageId + "}");

        } catch (Exception e) {
            getServletContext().log("Error loading ASF image", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw new ServletException("Error loading ASF image", e);
        }
    }
}