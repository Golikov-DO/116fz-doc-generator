package ru.ecospas.web.asf;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import ru.ecospas.app.InternalServices;
import ru.ecospas.domain.model.AsfDocumentImage;
import ru.ecospas.domain.service.ChildService;
import ru.ecospas.domain.service.SecurityService;
import ru.ecospas.web.BaseServlet;

@Component
public class DeleteAsfImageServlet extends BaseServlet {

    private ChildService<AsfDocumentImage> imageService;

    public DeleteAsfImageServlet(InternalServices services, SecurityService securityService) {
        super(services, securityService);
        this.imageService = services.getChildService(AsfDocumentImage.class);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {

        int id = Integer.parseInt(req.getParameter("id"));
        imageService.deleteById(id);
    }
}