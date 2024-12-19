package com.leedae.mockaroo.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // Common
    INVALID_INPUT_VALUE(400, "C001", "Invalid Input Value"),
    METHOD_NOT_ALLOWED(405, "C002", "Invalid Method"),
    ENTITY_NOT_FOUND(400, "C003", "Entity Not Found"),
    INTERNAL_SERVER_ERROR(500, "C004", "Server Error"),
    INVALID_TYPE_VALUE(400, "C005", "Invalid Type Value"),

    // User
    EMAIL_DUPLICATION(400, "U001", "Email is Duplicated"),
    LOGIN_INPUT_INVALID(400, "U002", "Login input is invalid"),

    // Mock Data
    MOCK_DATA_NOT_FOUND(400, "M001", "Mock Data Not Found"),
    INVALID_MOCK_DATA_FORMAT(400, "M002", "Invalid Mock Data Format");

    private final int status;
    private final String code;
    private final String message;
}