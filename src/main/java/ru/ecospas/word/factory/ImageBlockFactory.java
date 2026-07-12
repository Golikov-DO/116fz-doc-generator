package ru.ecospas.word.factory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.ecospas.domain.model.Asf;
import ru.ecospas.domain.model.AsfDocumentImage;
import ru.ecospas.domain.model.ObjectImage;
import ru.ecospas.domain.model.ObjectModel;
import ru.ecospas.domain.repository.AsfDocumentImageRepository;
import ru.ecospas.domain.repository.ObjectImageRepository;
import ru.ecospas.domain.repository.ObjectModelRepository;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ImageBlockFactory {
    private final ObjectImageRepository objectImageRepository;
    private final AsfDocumentImageRepository asfDocumentImageRepository;
    private final ObjectModelRepository objectRepository;

    public Map<String, Object> build(int objectId) throws SQLException {
        Map<String, Object> data = new HashMap<>();
        ObjectModel object = objectRepository.findById(objectId)
                .orElseThrow();

        Asf asf = object.getAsf();
        List<AsfDocumentImage> asfImage = asfDocumentImageRepository.findAllByAsfId(asf.getId());
        List<ObjectImage> objectImages = objectImageRepository.findAllByObjectId(objectId);

        Map<String, List<byte[]>> imagesByGroup = objectImages.stream()
                .collect(Collectors.groupingBy(
                        ObjectImage::getGroupKey,
                        Collectors.mapping(ObjectImage::getImageBlob, Collectors.toList())
                ));

        for (int i = 1; i <= 4; i++) {
            String imagePlaceholder = "OBJ_IMAGE_" + i + "_PLACEHOLDER";

            List<byte[]> blobs = imagesByGroup.get(String.valueOf(i));

            if (blobs != null && !blobs.isEmpty()) {
                data.put(imagePlaceholder, blobs);
            } else {
                data.put(imagePlaceholder, null);
            }
        }

        Map<String, List<byte[]>> asfByGroup = asfImage.stream()
                .collect(Collectors.groupingBy(
                        AsfDocumentImage::getGroupKey,
                        Collectors.mapping(AsfDocumentImage::getImageBlob, Collectors.toList())
                ));
        data.put("ASF_IMAGE_1_APPENDIX", asfByGroup.get(String.valueOf(1)));
        data.put("ASF_IMAGE_2_APPENDIX", asfByGroup.get(String.valueOf(2)));

        return data;
    }
}