package com.caseo.domain.util;

import com.caseo.domain.model.Asf;

public class AsfHeaderBuilder {

    public static String build(Asf asf) {

        if (asf == null) return "";

        return asf.getStatusGen() + " (" + asf.getStatusShort() + ") "
                + asf.getFullName();
    }
}