package com.caseo.web;

import com.caseo.app.ApplicationContext;
import com.caseo.app.InternalServices;
import com.caseo.domain.model.AsfDocumentImage;
import com.caseo.web.helper.DataLoader;
import com.caseo.web.helper.DataLoader.AsfData;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

import static com.caseo.web.util.RequestUtils.param;
import static com.caseo.web.util.RequestUtils.paramInt;

@WebServlet("/asf")
public class AsfServlet extends HttpServlet {

    private DataLoader dataLoader;

    @Override
    public void init() {
        ApplicationContext context = (ApplicationContext) getServletContext()
                .getAttribute("appContext");
        InternalServices services = context.internalServices();
        dataLoader = new DataLoader(services);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException {

        try {
            String mode = param(req, "mode");
            String returnOrgId = param(req, "returnOrgId");

            // 1. Получаем ID из запроса
            int asfId = paramInt(req, "asfId");

            // 2. Если в запросе ID нет, пробуем достать из сессии
            if (asfId == 0) {
                Object sessionAsfId = req.getSession().getAttribute("asfId");
                if (sessionAsfId != null) {
                    asfId = (Integer) sessionAsfId;
                }
            }

            // 3. Определяем режим работы
            if (asfId > 0) {
                // Если ID есть — сохраняем/обновляем в сессии и ставим режим просмотра, если не задан
                req.getSession().setAttribute("asfId", asfId);
                req.setAttribute("asfId", String.valueOf(asfId));
                if (mode == null || mode.isEmpty()) {
                    mode = "view";
                }
            } else {
                // Если ID всё еще 0 — это создание нового
                mode = "create";
                // Очищаем сессию, чтобы при "Создать" не всплывал старый ID
                //req.getSession().removeAttribute("asfId");
            }

            req.setAttribute("mode", mode);
            req.setAttribute("returnOrgId", returnOrgId);

            // Загружаем данные только для режимов просмотра/редактирования и если есть ID
            if (("view".equals(mode) || "edit".equals(mode))) {

                AsfData data = dataLoader.loadAsf(asfId);

                List<AsfDocumentImage> appendix1Images = data.images().stream()
                        .filter(img -> "1".equals(img.getGroupKey()))
                        .toList();
                List<AsfDocumentImage> appendix2Images = data.images().stream()
                        .filter(img -> "2".equals(img.getGroupKey()))
                        .toList();

                req.setAttribute("asf", data.asf());
                req.setAttribute("certificate", data.certificate());
                req.setAttribute("deployments", data.deployment());
                req.setAttribute("personnel", data.personnel());
                req.setAttribute("specialists", data.specialists());
                req.setAttribute("asfSigners", data.signers());
                req.setAttribute("asfWorkTypes", data.workTypes());
                req.setAttribute("appendix1Images", appendix1Images);
                req.setAttribute("appendix2Images", appendix2Images);
            }

            String requestedWith = req.getHeader("X-Requested-With");

            if ("XMLHttpRequest".equals(requestedWith)) {
                req.getRequestDispatcher("/WEB-INF/fragments/asf/asf.jsp")
                        .forward(req, resp);
            } else {
                req.setAttribute("contentPage", "/WEB-INF/pages/asf-page.jsp");
                req.getRequestDispatcher("/WEB-INF/layout.jsp").forward(req, resp);
            }

        } catch (Exception e) {
            getServletContext().log("Ошибка при загрузке АСФ", e);
            throw new ServletException("Ошибка при загрузке АСФ", e);
        }
    }
}