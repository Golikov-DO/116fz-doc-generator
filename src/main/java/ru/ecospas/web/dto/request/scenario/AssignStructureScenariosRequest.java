package ru.ecospas.web.dto.request.scenario;

import java.util.List;

public record AssignStructureScenariosRequest(

        List<Integer> likely,
        List<Integer> dangerous

) {
}