package ru.ecospas.web.asf;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import ru.ecospas.app.InternalServices;
import ru.ecospas.domain.model.*;
import ru.ecospas.domain.service.ChildService;
import ru.ecospas.domain.service.ParentService;
import ru.ecospas.domain.service.SecurityService;
import ru.ecospas.web.BaseServlet;
import ru.ecospas.web.helper.AsfSaveHelper;
import ru.ecospas.web.util.SyncListUtils;

import java.util.List;

import static ru.ecospas.web.util.RequestUtils.param;
import static ru.ecospas.web.util.RequestUtils.paramInt;

@Component
public class SaveAsfServlet extends BaseServlet {

    private final ParentService<Asf> asfService;
    private final ChildService<AsfCertificate> certificateService;
    private final ChildService<AsfPersonnel> personnelService;
    private final ChildService<AsfSpecialists> specialistsService;
    private final ChildService<AsfCompositionDeploymentFunds> deploymentService;
    private final ChildService<AsfSigner> signerService;
    private final ChildService<AsfWorkType> workTypeService;

    private final AsfSaveHelper saveHelper;

    public SaveAsfServlet(InternalServices services, SecurityService securityService) {
        super(services, securityService);
        this.asfService = services.getParentService(Asf.class);
        this.certificateService = services.getChildService(AsfCertificate.class);
        this.personnelService = services.getChildService(AsfPersonnel.class);
        this.specialistsService = services.getChildService(AsfSpecialists.class);
        this.deploymentService = services.getChildService(AsfCompositionDeploymentFunds.class);
        this.signerService = services.getChildService(AsfSigner.class);
        this.workTypeService = services.getChildService(AsfWorkType.class);
        this.saveHelper = new AsfSaveHelper();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException {

        try {
            int asfId = paramInt(req, "asfId");
            String returnOrgId = param(req, "returnOrgId");

            Asf asf;

            if (asfId == 0) {
                asf = new Asf();
            } else {
                asf = asfService.getOneById(asfId);
                if (asf == null) {
                    throw new ServletException("Asf not found with id: " + asfId);
                }
            }

            saveHelper.mapAsf(req, asf);
            asfService.save(asf);

            AsfCertificate certificate = certificateService.getOneByParentId(asf.getId());
            if (certificate == null) certificate = new AsfCertificate();
            saveHelper.mapCertificate(req, certificate);
            certificate.setAsf(asf);
            certificateService.save(certificate);

            AsfPersonnel personnel = personnelService.getOneByParentId(asf.getId());
            if (personnel == null) personnel = new AsfPersonnel();
            saveHelper.mapPersonnel(req, personnel);
            personnel.setAsf(asf);
            personnelService.save(personnel);

            AsfSpecialists specialists = specialistsService.getOneByParentId(asf.getId());
            if (specialists == null) specialists = new AsfSpecialists();
            saveHelper.mapSpecialists(req, specialists);
            specialists.setAsf(asf);
            specialistsService.save(specialists);

            AsfCompositionDeploymentFunds deployment = deploymentService.getOneByParentId(asf.getId());
            if (deployment == null) deployment = new AsfCompositionDeploymentFunds();
            saveHelper.mapDeployment(req, deployment);
            deployment.setAsf(asf);
            deploymentService.save(deployment);

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

            if (returnOrgId != null && !returnOrgId.isEmpty()) {
                resp.sendRedirect("objects?mode=edit&orgId=" + returnOrgId);
            } else {
                resp.sendRedirect("asf?mode=view&asfId=" + asf.getId());
            }

        } catch (Exception e) {
            getServletContext().log("Error saving ASF", e);
            throw new ServletException("Error saving ASF", e);
        }
    }
}