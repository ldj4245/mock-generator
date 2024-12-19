package com.leedae.mockaroo.dto.response;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MockDataGeneratedResponse {
    private String data;  // JSON, CSV, XML 형식의 생성된 데이터
    private String format;
    private int rowCount;
}