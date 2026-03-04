package com.caseo.word.factory;

import com.caseo.domain.model.*;
import com.caseo.domain.service.*;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ImageBlockFactory {
    private final AsfDocumentImageService asfDocumentImageService;
    private final AsfService asfService;
    private final ObjectImageService objectImageService;
    private final ObjectService objectService;

    public ImageBlockFactory(ObjectImageService objectImageService, ObjectService objectService,
                             AsfDocumentImageService asfDocumentImageService, AsfService asfService) {
        this.objectImageService = objectImageService;
        this.objectService = objectService;
        this.asfDocumentImageService = asfDocumentImageService;
        this.asfService = asfService;
    }

    public Map<String, Object> build(int objectId) throws SQLException {
        Map<String, Object> data = new HashMap<>();
        Asf asf = asfService.getById(objectId);
        List<AsfDocumentImage> asfImage = asfDocumentImageService.getByAsfId(asf.id());
        List<ObjectImage> objectImages = objectImageService.getByObjectId(objectId);

        Map<String, List<byte[]>> imagesByGroup = objectImages.stream()
                .collect(Collectors.groupingBy(
                        ObjectImage::groupKey,
                        Collectors.mapping(ObjectImage::imageBlob, Collectors.toList())
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
                        AsfDocumentImage::groupKey,
                        Collectors.mapping(AsfDocumentImage::imageBlob, Collectors.toList())
                ));
        data.put("ASF_IMAGE_1_APPENDIX", asfByGroup.get(String.valueOf(1)));
        data.put("ASF_IMAGE_2_APPENDIX", asfByGroup.get(String.valueOf(2)));

        return data;
    }
}