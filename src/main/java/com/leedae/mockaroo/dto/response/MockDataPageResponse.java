package com.leedae.mockaroo.dto.response;

import com.leedae.mockaroo.doamin.MockData;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class MockDataPageResponse {
    private List<MockDataResponse> content;
    private int totalPages;
    private long totalElements;
    private boolean last;
    private boolean first;
    private int size;
    private int number;

    public static MockDataPageResponse from(Page<MockData> page) {
        return MockDataPageResponse.builder()
                .content(page.getContent().stream()
                        .map(MockDataResponse::from)
                        .collect(Collectors.toList()))
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .last(page.isLast())
                .first(page.isFirst())
                .size(page.getSize())
                .number(page.getNumber())
                .build();
    }
}