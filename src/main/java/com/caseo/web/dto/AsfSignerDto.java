package com.caseo.web.dto;

import com.caseo.domain.model.AsfSigner;
import lombok.Getter;

@Getter
public class AsfSignerDto {

    private final Integer id;
    private final String name;
    private final String position;

    public AsfSignerDto(AsfSigner signer) {
        this.id = signer.getId();
        this.name = signer.getName();
        this.position = signer.getPosition();
    }
}