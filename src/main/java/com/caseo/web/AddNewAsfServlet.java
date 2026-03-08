package com.caseo.web;

import com.caseo.app.ApplicationContext;
import com.caseo.app.InternalServices;
import com.caseo.domain.model.Asf;
import com.caseo.domain.model.AsfSigner;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/addNewAsf")
public class AddNewAsfServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String asfIdParam = req.getParameter("asfId");
        String mode = req.getParameter("mode"); // Добавляем параметр mode

        // Проверяем, AJAX запрос или обычный
        String requestedWith = req.getHeader("X-Requested-With");
        boolean isAjax = "XMLHttpRequest".equals(requestedWith);

        try {
            ApplicationContext context =
                    (ApplicationContext) getServletContext().getAttribute("appContext");
            InternalServices services = context.internalServices();

            if (asfIdParam != null && !asfIdParam.isEmpty()) {
                int asfId = Integer.parseInt(asfIdParam);

                Asf asf = services.getParentService(Asf.class).getOneById(asfId);
                List<AsfSigner> signers = services.getChildService(AsfSigner.class).getManyByParentId(asfId);

                req.setAttribute("editAsf", asf);
                req.setAttribute("asfSigners", signers);
                req.setAttribute("mode", "edit"); // Устанавливаем режим редактирования
            } else {
                req.setAttribute("mode", "create"); // Режим создания
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Ошибка загрузки ASF", e);
        }

        // Для AJAX запросов отдаем только фрагмент
        if (isAjax) {
            req.getRequestDispatcher("/WEB-INF/fragments/asf/asf.jsp").forward(req, resp);
        } else {
            // Для обычных запросов - полную страницу
            req.getRequestDispatcher("/WEB-INF/pages/asf-page.jsp").forward(req, resp);
        }
    }
}