package com.caseo.word.pipeline;

public interface OpenStrategy {

    OpenResult open(byte[] templateBytes, int objectId);

}