package com.caseo.web;

import com.caseo.app.ApplicationContext;
import com.caseo.app.InternalServices;
import com.caseo.domain.model.AsfSigner;
import com.caseo.domain.service.AsfSignerService;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;


@WebServlet("/getAsfSigners")
public class GetAsfSignersServlet extends HttpServlet {

    private AsfSignerService asfSignerService;
    private Gson gson = new Gson();

    @Override
    public void init() {
        ApplicationContext context = (ApplicationContext) getServletContext()
                .getAttribute("appContext");
        InternalServices services = context.internalServices();
        asfSignerService = services.asfSignerService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String asfIdParam = req.getParameter("asfId");
        if (asfIdParam == null || asfIdParam.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            int asfId = Integer.parseInt(asfIdParam);
            // Получаем ОДНОГО подписанта для этого АСФ
            List<AsfSigner> signers = asfSignerService.getAllByAsfId(asfId);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(gson.toJson(signers));

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}