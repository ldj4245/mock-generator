package com.leedae.mockaroo.dto.request;

import com.leedae.mockaroo.doamin.constant.mock.OutputFormat;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MockDataCreateRequest {

    @NotBlank(message = "Mock 데이터 이름은 필수입니다.")
    private String name;

    @NotNull(message = "생성할 데이터 개수는 필수입니다.")
    @Min(value = 1, message = "데이터 개수는 1개 이상이어야 합니다.")
    @Max(value = 10000, message = "데이터 개수는 10000개를 초과할 수 없습니다.")
    private Integer rowCount;

    @NotNull(message = "출력 형식은 필수입니다.")
    private OutputFormat outputFormat;

    @NotEmpty(message = "필드 정보는 필수입니다.")
    private List<MockFieldRequest> fields;
}