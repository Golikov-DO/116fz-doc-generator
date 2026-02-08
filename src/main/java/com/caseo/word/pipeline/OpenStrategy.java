package com.caseo.word.pipeline;

import com.caseo.domain.model.DocumentSet;

public interface OpenStrategy {

    OpenResult open(byte[] templateBytes, DocumentSet documentSet);

}