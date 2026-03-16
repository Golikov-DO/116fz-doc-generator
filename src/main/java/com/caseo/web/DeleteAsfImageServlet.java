package com.caseo.web;

import com.caseo.app.ApplicationContext;
import com.caseo.domain.model.AsfDocumentImage;
import com.caseo.domain.service.ChildService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/deleteAsfImage")
public class DeleteAsfImageServlet extends HttpServlet {

    private ChildService<AsfDocumentImage> imageService;

    @Override
    public void init() {
        ApplicationContext ctx =
            (ApplicationContext) getServletContext().getAttribute("appContext");

        imageService = ctx.internalServices().getChildService(AsfDocumentImage.class);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {

        int id = Integer.parseInt(req.getParameter("id"));
        imageService.deleteById(id);
    }
}