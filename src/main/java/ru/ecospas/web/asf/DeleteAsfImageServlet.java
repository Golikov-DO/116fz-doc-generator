package ru.ecospas.web.asf;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import ru.ecospas.domain.repository.OrganizationRepository;
import ru.ecospas.domain.service.AsfDocumentImageService;
import ru.ecospas.domain.service.SecurityService;
import ru.ecospas.web.BaseServlet;

@Component
public class DeleteAsfImageServlet extends BaseServlet {

    private final AsfDocumentImageService imageService;

    public DeleteAsfImageServlet(
            SecurityService securityService,
            OrganizationRepository organizationRepository,
            AsfDocumentImageService imageService) {

        super(securityService, organizationRepository);
        this.imageService = imageService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {

        int id = Integer.parseInt(req.getParameter("id"));

        imageService.delete(id);
    }
}