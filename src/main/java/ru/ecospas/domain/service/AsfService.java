package ru.ecospas.domain.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ecospas.domain.model.Asf;
import ru.ecospas.domain.model.AsfCertificate;
import ru.ecospas.domain.model.AsfCompositionDeploymentFunds;
import ru.ecospas.domain.model.AsfPersonnel;
import ru.ecospas.domain.model.AsfSpecialists;
import ru.ecospas.domain.model.AsfSigner;
import ru.ecospas.domain.model.AsfWorkType;
import ru.ecospas.domain.repository.AsfCertificateRepository;
import ru.ecospas.domain.repository.AsfCompositionDeploymentFundsRepository;
import ru.ecospas.domain.repository.AsfPersonnelRepository;
import ru.ecospas.domain.repository.AsfRepository;
import ru.ecospas.domain.repository.AsfSignerRepository;
import ru.ecospas.domain.repository.AsfSpecialistsRepository;
import ru.ecospas.domain.repository.AsfWorkTypeRepository;
import ru.ecospas.web.helper.AsfSaveHelper;
import ru.ecospas.web.util.SyncListUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AsfService {

    private final AsfRepository asfRepository;

    private final AsfCertificateRepository certificateRepository;
    private final AsfPersonnelRepository personnelRepository;
    private final AsfSpecialistsRepository specialistsRepository;
    private final AsfCompositionDeploymentFundsRepository deploymentRepository;

    private final AsfSignerRepository signerRepository;
    private final AsfWorkTypeRepository workTypeRepository;

    private final AsfSaveHelper helper;

    public Asf load(Integer id) {
        return asfRepository.findById(id).orElse(null);
    }

    public Asf create() {
        return new Asf();
    }

    public Asf saveAsf(
            HttpServletRequest req,
            Asf asf
    ) {

        helper.mapAsf(req, asf);

        return asfRepository.save(asf);
    }

    private void saveCertificate(
            HttpServletRequest req,
            Asf asf
    ) {

        AsfCertificate certificate = certificateRepository
                .findByAsfId(asf.getId())
                .orElseGet(AsfCertificate::new);

        helper.mapCertificate(req, certificate);

        certificate.setAsf(asf);

        certificateRepository.save(certificate);
    }

    private void savePersonnel(
            HttpServletRequest req,
            Asf asf
    ) {

        AsfPersonnel personnel = personnelRepository
                .findByAsfId(asf.getId())
                .orElseGet(AsfPersonnel::new);

        helper.mapPersonnel(req, personnel);

        personnel.setAsf(asf);

        personnelRepository.save(personnel);
    }

    private void saveSpecialists(
            HttpServletRequest req,
            Asf asf
    ) {

        AsfSpecialists specialists = specialistsRepository
                .findByAsfId(asf.getId())
                .orElseGet(AsfSpecialists::new);

        helper.mapSpecialists(req, specialists);

        specialists.setAsf(asf);

        specialistsRepository.save(specialists);
    }

    private void saveDeployment(
            HttpServletRequest req,
            Asf asf
    ) {

        AsfCompositionDeploymentFunds deployment = deploymentRepository
                .findByAsfId(asf.getId())
                .orElseGet(AsfCompositionDeploymentFunds::new);

        helper.mapDeployment(req, deployment);

        deployment.setAsf(asf);

        deploymentRepository.save(deployment);
    }

    private void saveSigners(
            HttpServletRequest req,
            Asf asf
    ) {

        List<AsfSigner> newSigners =
                helper.mapSigners(req);

        List<AsfSigner> oldSigners =
                signerRepository.findAllByAsfId(asf.getId());

        SyncListUtils.syncList(
                newSigners,
                oldSigners,
                AsfSigner::getId,
                signerRepository::deleteById
        );

        for (AsfSigner signer : newSigners) {
            signer.setAsf(asf);
            signerRepository.save(signer);
        }
    }

    private void saveWorkTypes(
            HttpServletRequest req,
            Asf asf
    ) {

        List<AsfWorkType> newWorkTypes =
                helper.mapWorkTypes(req);

        List<AsfWorkType> oldWorkTypes =
                workTypeRepository.findAllByAsfId(asf.getId());

        SyncListUtils.syncList(
                newWorkTypes,
                oldWorkTypes,
                AsfWorkType::getId,
                workTypeRepository::deleteById
        );

        for (AsfWorkType workType : newWorkTypes) {
            workType.setAsf(asf);
            workTypeRepository.save(workType);
        }
    }

    public Asf save(
            HttpServletRequest req,
            Asf asf
    ) {

        asf = saveAsf(req, asf);

        saveCertificate(req, asf);

        savePersonnel(req, asf);

        saveSpecialists(req, asf);

        saveDeployment(req, asf);

        saveSigners(req, asf);

        saveWorkTypes(req, asf);

        return asf;
    }
}