package ru.ecospas.domain.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ecospas.domain.model.*;
import ru.ecospas.domain.repository.*;
import ru.ecospas.web.dto.request.asf.SaveAsfRequest;
import ru.ecospas.web.helper.AsfSaveHelper;
import ru.ecospas.web.mapper.asf.AsfRequestMapper;
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

    private final AsfRequestMapper asfRequestMapper;

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

    private void saveCertificate(HttpServletRequest req, Asf asf) {

        AsfCertificate certificate = certificateRepository
                .findByAsfId(asf.getId())
                .orElseGet(AsfCertificate::new);
        helper.mapCertificate(req, certificate);
        certificate.setAsf(asf);
        certificateRepository.save(certificate);
    }

    private void savePersonnel(HttpServletRequest req, Asf asf) {
        AsfPersonnel personnel = personnelRepository
                .findByAsfId(asf.getId())
                .orElseGet(AsfPersonnel::new);
        helper.mapPersonnel(req, personnel);
        personnel.setAsf(asf);
        personnelRepository.save(personnel);
    }

    private void saveSpecialists(HttpServletRequest req, Asf asf) {
        AsfSpecialists specialists = specialistsRepository
                .findByAsfId(asf.getId())
                .orElseGet(AsfSpecialists::new);
        helper.mapSpecialists(req, specialists);
        specialists.setAsf(asf);
        specialistsRepository.save(specialists);
    }

    private void saveDeployment(HttpServletRequest req, Asf asf) {
        AsfCompositionDeploymentFunds deployment = deploymentRepository
                .findByAsfId(asf.getId())
                .orElseGet(AsfCompositionDeploymentFunds::new);
        helper.mapDeployment(req, deployment);
        deployment.setAsf(asf);
        deploymentRepository.save(deployment);
    }

    private void saveSigners(HttpServletRequest req, Asf asf) {
        List<AsfSigner> newSigners = helper.mapSigners(req);
        List<AsfSigner> oldSigners = signerRepository.findAllByAsfId(asf.getId());
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

    private void saveWorkTypes(HttpServletRequest req, Asf asf) {
        List<AsfWorkType> newWorkTypes =helper.mapWorkTypes(req);
        List<AsfWorkType> oldWorkTypes = workTypeRepository.findAllByAsfId(asf.getId());
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

    public Asf save(HttpServletRequest req, Asf asf) {
        asf = saveAsf(req, asf);
        saveCertificate(req, asf);
        savePersonnel(req, asf);
        saveSpecialists(req, asf);
        saveDeployment(req, asf);
        saveSigners(req, asf);
        saveWorkTypes(req, asf);
        return asf;
    }

    //REST
    @Transactional(readOnly = true)
    public Asf loadRest(Integer id) {
        Asf asf = asfRepository.findById(id).orElse(null);
        if (asf == null) {
            return null;
        }
        Hibernate.initialize(asf.getSigners());
        Hibernate.initialize(asf.getWorkTypes());
        Hibernate.initialize(asf.getImages());
        return asf;
    }

    public Asf create(SaveAsfRequest request) {
        Asf asf = new Asf();
        return save(request, asf);
    }

    public Asf save(SaveAsfRequest request, Asf asf) {
        asfRequestMapper.toAsf(request, asf);
        saveCertificate(request, asf);
        savePersonnel(request, asf);
        saveSpecialists(request, asf);
        saveDeployment(request, asf);
        saveSigners(request, asf);
        saveWorkTypes(request, asf);
        saveImages(request, asf);
        return asfRepository.save(asf);
    }

    public Asf update(Integer id, SaveAsfRequest request) {
        Asf asf = load(id);
        if (asf == null) {
            return null;
        }
        return save(request, asf);
    }
    public void delete(Integer id) {
        Asf asf = load(id);
        if (asf == null) {
            return;
        }
        asfRepository.delete(asf);
    }

    private void saveCertificate(SaveAsfRequest request, Asf asf) {
        AsfCertificate certificate = asf.getCertificate();
        if (certificate == null) {
            certificate = new AsfCertificate();
        }
        asfRequestMapper.toCertificate(request.certificate(), certificate);
        certificate.setAsf(asf);
        asf.setCertificate(certificate);
    }

    private void savePersonnel(SaveAsfRequest request, Asf asf) {
        AsfPersonnel personnel = asf.getPersonnel();
        if (personnel == null) {
            personnel = new AsfPersonnel();
        }
        asfRequestMapper.toPersonnel(request.personnel(), personnel);
        personnel.setAsf(asf);
        asf.setPersonnel(personnel);
    }

    private void saveSpecialists(SaveAsfRequest request, Asf asf) {
        AsfSpecialists specialists = asf.getSpecialists();
        if (specialists == null) {
            specialists = new AsfSpecialists();
        }
        asfRequestMapper.toSpecialists(request.specialists(), specialists);
        specialists.setAsf(asf);
        asf.setSpecialists(specialists);
    }

    private void saveDeployment(SaveAsfRequest request, Asf asf) {
        AsfCompositionDeploymentFunds deployment = asf.getDeployment();
        if (deployment == null) {
            deployment = new AsfCompositionDeploymentFunds();
        }
        asfRequestMapper.toDeployment(request.deployment(), deployment);
        deployment.setAsf(asf);
        asf.setDeployment(deployment);
    }

    private void saveSigners(SaveAsfRequest request, Asf asf) {
        asf.getSigners().clear();
        List<AsfSigner> signers = asfRequestMapper.toSigners(request.signers());
        for (AsfSigner signer : signers) {
            signer.setAsf(asf);
        }
        asf.getSigners().addAll(signers);
    }

    private void saveWorkTypes(SaveAsfRequest request, Asf asf) {
        asf.getWorkTypes().clear();
        List<AsfWorkType> workTypes = asfRequestMapper.toWorkTypes(request.workTypes());
        for (AsfWorkType workType : workTypes) {
            workType.setAsf(asf);
        }
        asf.getWorkTypes().addAll(workTypes);
    }

    private void saveImages(SaveAsfRequest request, Asf asf) {
        asf.getImages().clear();
        List<AsfDocumentImage> images = asfRequestMapper.toImages(request.images());
        for (AsfDocumentImage image : images) {
            image.setAsf(asf);
        }
        asf.getImages().addAll(images);
    }

    public List<Asf> findAll() {
        return asfRepository.findAll();
    }

    @Transactional
    public void setPrimarySigner(Integer asfId, Integer signerId, boolean isPrimary) {
        // Сбросить isPrimary у всех подписантов этой АСФ
        signerRepository.clearPrimaryByAsfId(asfId);
        // Установить isPrimary у выбранного
        AsfSigner signer = signerRepository.findById(signerId)
                .orElseThrow(() -> new IllegalArgumentException("Signer not found"));
        signer.setIsPrimary(isPrimary);
        signerRepository.save(signer);
    }
}