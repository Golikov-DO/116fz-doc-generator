package ru.ecospas.web.dto.request.object;

import java.time.LocalDate;

public record ObjectOrderMinimumBalanceRequest(

        String number,
        LocalDate date

) {
}