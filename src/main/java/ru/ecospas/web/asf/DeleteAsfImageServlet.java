package ru.ecospas.web.asf;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.ecospas.domain.model.AsfDocumentImage;
import ru.ecospas.domain.service.ChildService;
import ru.ecospas.web.BaseServlet;

@SuppressWarnings("unused") // Managed via dynamic registration in ServletAutoRegistration
public class DeleteAsfImageServlet extends BaseServlet {

    private ChildService<AsfDocumentImage> imageService;

    @Override
    public void init() {
        super.init();
        imageService = services.getChildService(AsfDocumentImage.class);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {

        int id = Integer.parseInt(req.getParameter("id"));
        imageService.deleteById(id);
    }
}