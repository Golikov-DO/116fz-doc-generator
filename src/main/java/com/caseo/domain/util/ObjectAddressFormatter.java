package com.caseo.domain.util;

import com.caseo.domain.model.ObjectAddress;
import com.caseo.domain.model.ObjectCity;

public class ObjectAddressFormatter {

    private ObjectAddressFormatter() {}

    public static String format(ObjectAddress address, ObjectCity city) {


                // индекс
        return address.index() + ", " +
                // субъект
                address.constituentEntity() + ", " +
                // город
                "г. " + city.name() + ", " +
                // улица
                "ул. " + address.street() + ", " +
                // дом
                address.house();
    }
}
