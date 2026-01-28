package com.caseo.domain.util;

import com.caseo.domain.model.ObjectAddress;
import com.caseo.domain.model.ObjectCity;

public class ObjectAddressFormatter {

    private ObjectAddressFormatter() {}

    public static String format(ObjectAddress address, ObjectCity city) {

        StringBuilder sb = new StringBuilder();

        // индекс
        sb.append(address.getIndex()).append(", ");

        // субъект
        sb.append(address.getConstituentEntity()).append(", ");

        // город
        sb.append("г. ").append(city.getName()).append(", ");

        // улица
        sb.append("ул. ").append(address.getStreet()).append(", ");

        // дом
        sb.append(address.getHouse());

        return sb.toString();
    }
}
