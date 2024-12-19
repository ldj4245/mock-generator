package com.leedae.mockaroo.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MockDataUpdateRequest {

    @NotBlank(message = "Mock 데이터 이름은 필수입니다.")
    private String name;
}