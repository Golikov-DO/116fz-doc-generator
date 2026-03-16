package com.caseo.web;

import com.caseo.app.ApplicationContext;
import com.caseo.app.InternalServices;
import com.caseo.domain.model.*;
import com.caseo.domain.service.*;
import com.caseo.web.helper.AsfSaveHelper;
import com.caseo.web.util.SyncListUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

import static com.caseo.web.util.RequestUtils.param;
import static com.caseo.web.util.RequestUtils.paramInt;

@WebServlet("/createAsf")
public class CreateAsfServlet extends HttpServlet {

    private ParentService<Asf> asfService;
    private ChildService<AsfCertificate> certificateService;
    private ChildService<AsfPersonnel> personnelService;
    private ChildService<AsfSpecialists> specialistsService;
    private ChildService<AsfCompositionDeploymentFunds> deploymentService;
    private ChildService<AsfSigner> signerService;
    private ChildService<AsfWorkType> workTypeService;
    private ChildService<AsfDocumentImage> imageService; // Добавили

    private AsfSaveHelper saveHelper;

    @Override
    public void init() {

        ApplicationContext context = (ApplicationContext) getServletContext()
                .getAttribute("appContext");

        InternalServices services = context.internalServices();

        asfService = services.getParentService(Asf.class);

        certificateService = services.getChildService(AsfCertificate.class);
        personnelService = services.getChildService(AsfPersonnel.class);
        specialistsService = services.getChildService(AsfSpecialists.class);
        deploymentService = services.getChildService(AsfCompositionDeploymentFunds.class);
        signerService = services.getChildService(AsfSigner.class);
        workTypeService = services.getChildService(AsfWorkType.class);
        imageService = services.getChildService(AsfDocumentImage.class); // Инициализируем

        saveHelper = new AsfSaveHelper();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException {

        try {
            Integer asfId = paramInt(req, "asfId");
            String returnOrgId = param(req, "returnOrgId");

            // Получаем ID организации из сессии для редиректа
            Integer orgId =paramInt  (req, "orgId");

            Asf asf;

            // 0 или null означает создание новой АСФ
            if (asfId == null || asfId == 0) {
                asf = new Asf();
            } else {
                asf = asfService.getOneById(asfId);
                if (asf == null) {
                    throw new ServletException("Asf not found with id: " + asfId);
                }
            }

            // 1. Основные данные
            saveHelper.mapAsf(req, asf);
            asfService.save(asf); // После save у asf уже есть ID

            // 2. Сертификат
            AsfCertificate certificate = certificateService.getOneByParentId(asf.getId());
            if (certificate == null) certificate = new AsfCertificate();
            saveHelper.mapCertificate(req, certificate);
            certificate.setAsf(asf);
            certificateService.save(certificate);

            // 3. Персонал
            AsfPersonnel personnel = personnelService.getOneByParentId(asf.getId());
            if (personnel == null) personnel = new AsfPersonnel();
            saveHelper.mapPersonnel(req, personnel);
            personnel.setAsf(asf);
            personnelService.save(personnel);

            // 4. Специалисты
            AsfSpecialists specialists = specialistsService.getOneByParentId(asf.getId());
            if (specialists == null) specialists = new AsfSpecialists();
            saveHelper.mapSpecialists(req, specialists);
            specialists.setAsf(asf);
            specialistsService.save(specialists);

            // 5. Места дислокации
            AsfCompositionDeploymentFunds deployment = deploymentService.getOneByParentId(asf.getId());
            if (deployment == null) deployment = new AsfCompositionDeploymentFunds();
            saveHelper.mapDeployment(req, deployment);
            deployment.setAsf(asf);
            deploymentService.save(deployment);

            // 6. Подписанты
            List<AsfSigner> signers = saveHelper.mapSigners(req);
            List<AsfSigner> oldSigners = signerService.getManyByParentId(asf.getId());

            SyncListUtils.syncList(
                    signers,
                    oldSigners,
                    AsfSigner::getId,
                    signerService::deleteById
            );

            for (AsfSigner signer : signers) {
                signer.setAsf(asf);
                signerService.save(signer);
            }

            // 7. Типы работ
            List<AsfWorkType> workTypes = saveHelper.mapWorkTypes(req);
            List<AsfWorkType> oldWorkTypes = workTypeService.getManyByParentId(asf.getId());

            SyncListUtils.syncList(
                    workTypes,
                    oldWorkTypes,
                    AsfWorkType::getId,
                    workTypeService::deleteById
            );

            for (AsfWorkType workType : workTypes) {
                workType.setAsf(asf);
                workTypeService.save(workType);
            }

            // 8. Изображения - раскомментируем
//            List<AsfDocumentImage> images = saveHelper.mapImages(req);
//            List<AsfDocumentImage> oldImages = imageService.getManyByParentId(asf.getId());
//
//            SyncListUtils.syncList(
//                    images,
//                    oldImages,
//                    AsfDocumentImage::getId,
//                    imageService::deleteById
//            );
//
//            for (AsfDocumentImage image : images) {
//                image.setAsf(asf);
//                imageService.save(image);
//            }

            // Редирект
            if (returnOrgId != null && !returnOrgId.isEmpty()) {
                resp.sendRedirect("objects?mode=edit&orgId=" + returnOrgId);
            } else {
                resp.sendRedirect("asf?mode=view&asfId=" + asf.getId());
            }

        } catch (Exception e) {
            getServletContext().log("Ошибка при сохранении АСФ", e);
            throw new ServletException("Ошибка при сохранении АСФ", e);
        }
    }
}