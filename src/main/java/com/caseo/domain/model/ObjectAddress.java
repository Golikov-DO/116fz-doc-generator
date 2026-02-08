package com.caseo.domain.model;

public record ObjectAddress(int id, Long objectId, Integer index, String constituentEntity, String city, String street,
                            String house) {

}
