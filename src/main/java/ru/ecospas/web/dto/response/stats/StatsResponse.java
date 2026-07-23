package ru.ecospas.web.dto.response.stats;

public record StatsResponse(
    long organizations,
    long objects
) {
}