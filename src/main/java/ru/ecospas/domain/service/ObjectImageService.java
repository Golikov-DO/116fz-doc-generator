package ru.ecospas.domain.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.ecospas.domain.model.ObjectImage;
import ru.ecospas.domain.model.ObjectModel;
import ru.ecospas.domain.repository.ObjectImageRepository;
import ru.ecospas.domain.repository.ObjectModelRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ObjectImageService {

    private final ObjectImageRepository imageRepository;
    private final ObjectModelRepository objectRepository;

    public Integer upload(Integer objectId, String group, byte[] data) {
        ObjectModel object = objectRepository.findById(objectId).orElse(null);
        if (object == null) {
            return null;
        }
        List<ObjectImage> images = imageRepository.findAllByObjectId(objectId);
        ObjectImage image = images.stream()
                .filter(i -> group.equals(i.getGroupKey()))
                .findFirst()
                .orElse(null);
        if (image == null) {
            image = new ObjectImage();
            image.setObject(object);
            image.setGroupKey(group);
        }
        image.setImageBlob(data);
        imageRepository.save(image);
        return image.getId();
    }

    public byte[] getImage(Integer id) {
        return imageRepository.findById(id)
                .map(ObjectImage::getImageBlob)
                .orElse(null);
    }

    public void delete(Integer id) {
        imageRepository.deleteById(id);
    }
}