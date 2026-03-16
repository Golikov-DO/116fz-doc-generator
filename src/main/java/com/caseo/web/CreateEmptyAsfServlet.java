package com.caseo.web;

import com.caseo.app.ApplicationContext;
import com.caseo.app.InternalServices;
import com.caseo.domain.model.Asf;
import com.caseo.domain.service.ParentService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static com.caseo.web.util.RequestUtils.param;

@WebServlet("/createEmptyAsf")
public class CreateEmptyAsfServlet extends HttpServlet {

    private ParentService<Asf> asfService;

    @Override
    public void init() {
        ApplicationContext context = (ApplicationContext) getServletContext()
                .getAttribute("appContext");
        InternalServices services = context.internalServices();
        asfService = services.getParentService(Asf.class);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException {

        try {
            String returnOrgId = param(req, "returnOrgId");

            // Создаем пустую запись АСФ
            Asf asf = new Asf();
            asfService.save(asf); // теперь у asf есть ID

            // Сразу открываем форму редактирования с этим ID
            resp.sendRedirect("asf?mode=edit&asfId=" + asf.getId() + "&returnOrgId=" + returnOrgId);

        } catch (Exception e) {
            getServletContext().log("Ошибка при создании пустой записи АСФ", e);
            throw new ServletException("Ошибка при создании пустой записи АСФ", e);
        }
    }
}