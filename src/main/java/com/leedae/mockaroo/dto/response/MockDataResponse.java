package com.leedae.mockaroo.dto.response;

import com.leedae.mockaroo.doamin.MockData;
import com.leedae.mockaroo.doamin.constant.mock.OutputFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MockDataResponse {
    private Long id;
    private String name;
    private Integer rowCount;
    private OutputFormat outputFormat;
    private String shareUrl;
    private LocalDateTime createdAt;
    private List<MockFieldResponse> fields;

    public static MockDataResponse from(MockData mockData) {
        return MockDataResponse.builder()
                .id(mockData.getId())
                .name(mockData.getName())
                .rowCount(mockData.getRowCount())
                .outputFormat(mockData.getOutputFormat())
                .shareUrl(mockData.getShareUrl())
                .createdAt(mockData.getCreatedAt())
                .fields(mockData.getFields().stream()
                        .map(MockFieldResponse::from)
                        .collect(Collectors.toList()))
                .build();
    }
}