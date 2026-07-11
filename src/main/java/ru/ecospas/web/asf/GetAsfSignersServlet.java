package ru.ecospas.web.asf;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import ru.ecospas.app.InternalServices;
import ru.ecospas.domain.model.AsfSigner;
import ru.ecospas.domain.service.ChildService;
import ru.ecospas.domain.service.SecurityService;
import ru.ecospas.web.BaseServlet;
import ru.ecospas.web.dto.AsfSignerDto;

import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

@Component
public class GetAsfSignersServlet extends BaseServlet {

    private final ChildService<AsfSigner> signerService;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(java.time.LocalTime.class,
                    (JsonSerializer<LocalTime>) (src, typeOfSrc, context) ->
                            new JsonPrimitive(src.toString()))
            .create();

    public GetAsfSignersServlet(InternalServices services, SecurityService securityService) {
        super(services, securityService);
        this.signerService = services.getChildService(AsfSigner.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

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
            getServletContext().log("Error retrieving ASF signatories", e);
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}