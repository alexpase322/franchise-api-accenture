package com.accenture.franchise.api.dto;

public record ErrorResponse(int status, String error, String message) {}