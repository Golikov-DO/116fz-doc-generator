package ru.ecospas.web.dto.response.object;

import java.time.LocalDate;

public record ObjectInsurancePolicyResponse(

        String number,
        LocalDate validUntil

) {
}