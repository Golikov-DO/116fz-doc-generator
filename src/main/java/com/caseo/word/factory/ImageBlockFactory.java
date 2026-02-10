package com.caseo.word.factory;

import com.caseo.domain.model.DocumentSet;
import com.caseo.domain.model.ObjectImage;
import com.caseo.domain.model.ObjectModel;
import com.caseo.domain.service.ObjectImageService;
import com.caseo.domain.service.ObjectService;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ImageBlockFactory {
    private final ObjectImageService objectImageService;
    private final ObjectService objectService;

    public ImageBlockFactory(ObjectImageService objectImageService, ObjectService objectService) {
        this.objectImageService = objectImageService;
        this.objectService = objectService;
    }

    public Map<String, Object> build(DocumentSet documentSet) throws SQLException {
        Map<String, Object> data = new HashMap<>();
        ObjectModel obj = objectService.getByOrgId(documentSet.orgId());
        List<ObjectImage> allImages = objectImageService.getByObjectId(obj.id());

        Map<String, List<byte[]>> imagesByGroup = allImages.stream()
                .collect(Collectors.groupingBy(
                        ObjectImage::groupKey,
                        Collectors.mapping(ObjectImage::imageBlob, Collectors.toList())
                ));

        for (int i = 1; i <= 5; i++) {
            String imagePlaceholder = "OBJ_IMAGE_" + i + "_PLACEHOLDER";

            List<byte[]> blobs = imagesByGroup.get(String.valueOf(i));

            if (blobs != null && !blobs.isEmpty()) {
                data.put(imagePlaceholder, blobs);
            } else {
                data.put(imagePlaceholder, null);
            }
        }
        return data;
    }
}