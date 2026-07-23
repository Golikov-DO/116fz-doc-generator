package ru.ecospas.web.dto.response.object;

import java.time.LocalDate;

public record ObjectOrderMinimumBalanceResponse(

        String number,
        LocalDate date

) {
}