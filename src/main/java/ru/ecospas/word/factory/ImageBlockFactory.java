package ru.ecospas.word.factory;

import ru.ecospas.domain.model.Asf;
import ru.ecospas.domain.model.AsfDocumentImage;
import ru.ecospas.domain.model.ObjectImage;
import ru.ecospas.domain.model.ObjectModel;
import ru.ecospas.domain.service.ChildService;
import ru.ecospas.domain.service.ParentService;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ImageBlockFactory {
    private final ChildService<ObjectImage> objectImageService;
    private final ChildService<AsfDocumentImage> asfDocumentImageService;
    private final ParentService<ObjectModel> objectService;

    public ImageBlockFactory(ChildService<ObjectImage> objectImageService,
                             ChildService<AsfDocumentImage> asfDocumentImageService,
                             ParentService<ObjectModel> objectService) {
        this.objectImageService = objectImageService;
        this.asfDocumentImageService = asfDocumentImageService;
        this.objectService = objectService;
    }

    public Map<String, Object> build(int objectId) throws SQLException {
        Map<String, Object> data = new HashMap<>();
        Asf asf = objectService.getOneById(objectId).getAsf();
        List<AsfDocumentImage> asfImage = asfDocumentImageService.getManyByParentId(asf.getId());
        List<ObjectImage> objectImages = objectImageService.getManyByParentId(objectId);

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