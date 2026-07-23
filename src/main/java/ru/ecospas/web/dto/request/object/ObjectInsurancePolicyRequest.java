package ru.ecospas.web.dto.request.object;

import java.time.LocalDate;

public record ObjectInsurancePolicyRequest(

        String number,
        LocalDate validUntil

) {
}