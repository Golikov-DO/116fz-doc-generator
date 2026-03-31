package com.caseo.web;

import com.caseo.app.ApplicationContext;
import com.caseo.app.InternalServices;
import com.caseo.domain.model.AsfSigner;
import com.caseo.domain.service.ChildService;
import com.caseo.web.dto.AsfSignerDto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalTime;
import java.util.List;


@WebServlet("/getAsfSigners")
public class GetAsfSignersServlet extends HttpServlet {

    private ChildService<AsfSigner> signerService;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(java.time.LocalTime.class,
                    (JsonSerializer<LocalTime>) (src, typeOfSrc, context) ->
                            new JsonPrimitive(src.toString()))
            .create();

    @Override
    public void init() {
        ApplicationContext context = (ApplicationContext) getServletContext()
                .getAttribute("appContext");
        InternalServices services = context.internalServices();
        signerService = services.getChildService(AsfSigner.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String asfIdParam = req.getParameter("asfId");
        if (asfIdParam == null || asfIdParam.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            int asfId = Integer.parseInt(asfIdParam);
            List<AsfSigner> signers = signerService.getManyByParentId(asfId);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            List<AsfSignerDto> dtoList = signers.stream()
                    .map(AsfSignerDto::new)
                    .toList();

            resp.getWriter().write(gson.toJson(dtoList));

        } catch (Exception e) {
            getServletContext().log("Ошибка при получении подписантов ASF", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}