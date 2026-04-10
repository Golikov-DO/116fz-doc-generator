package ru.ecospas.web.dto;

import lombok.Getter;
import ru.ecospas.domain.model.AsfSigner;

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