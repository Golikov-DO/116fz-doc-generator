package com.caseo.word.factory;

import com.caseo.domain.model.DocumentSet;
import com.caseo.domain.model.ObjectModel;
import com.caseo.domain.service.ObjectService;
import com.caseo.word.blocks.image.ImageBlock;
import org.apache.poi.xwpf.usermodel.Document;

import java.sql.SQLException;

public class ImageBlockFactory {

    private final ObjectService objectService;

    public ImageBlockFactory(ObjectService objectService) {
        this.objectService = objectService;
    }

    public ImageBlock build(DocumentSet documentSet) throws SQLException {

        ObjectModel obj =
                objectService.getByOrgId(documentSet.getOrgId());

        byte[] blob = obj.getPlanAndDiagram();

        if (blob == null || blob.length == 0) {
            return null;
        }

        return new ImageBlock(
                "OBJECT_SCHEME",          // placeholder в шаблоне
                blob,
                500,                      // ширина
                350,                      // высота
                Document.PICTURE_TYPE_PNG // или JPEG — зависит от БД
        );
    }
}