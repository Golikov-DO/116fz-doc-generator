package ru.ecospas.word.pipeline;

import ru.ecospas.domain.model.ObjectModel;

public interface OpenStrategy {

    OpenResult open(byte[] templateBytes, ObjectModel object);

}