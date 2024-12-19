package com.leedae.mockaroo.dto.response;

import com.leedae.mockaroo.doamin.MockField;
import com.leedae.mockaroo.doamin.constant.mock.FieldType;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MockFieldResponse {
    private Long id;
    private String fieldName;
    private FieldType fieldType;
    private String format;
    private boolean required;
    private String defaultValue;
    private List<String> customValues;

    public static MockFieldResponse from(MockField mockField) {
        return MockFieldResponse.builder()
                .id(mockField.getId())
                .fieldName(mockField.getFieldName())
                .fieldType(mockField.getFieldType())
                .format(mockField.getFormat())
                .required(mockField.isRequired())
                .defaultValue(mockField.getDefaultValue())
                .customValues(mockField.getCustomValues())
                .build();
    }
}
