package ru.ecospas.domain.service;

import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ecospas.domain.model.*;
import ru.ecospas.domain.repository.AsfRepository;
import ru.ecospas.domain.repository.AsfSignerRepository;
import ru.ecospas.web.dto.request.asf.SaveAsfRequest;
import ru.ecospas.web.mapper.asf.AsfRequestMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AsfService {

    private final AsfRepository asfRepository;
    private final AsfSignerRepository signerRepository;
    private final AsfRequestMapper asfRequestMapper;

    public Asf load(Integer id) {
        return asfRepository.findById(id).orElse(null);
    }

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