package com.leedae.mockaroo.dto.request;

import com.leedae.mockaroo.doamin.constant.mock.FieldType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MockFieldRequest {

    @NotBlank(message = "필드명은 필수입니다.")
    private String fieldName;

    @NotNull(message = "필드 타입은 필수입니다.")
    private FieldType fieldType;

    private String format;
    private boolean required;
    private String defaultValue;
    private List<String> customValues;
}
