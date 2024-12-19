package com.leedae.mockaroo.doamin;

import com.leedae.mockaroo.doamin.constant.mock.OutputFormat;
import com.leedae.mockaroo.dto.request.MockFieldRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "mock_data")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MockData extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "mockData", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MockField> fields = new ArrayList<>();

    private Integer rowCount;

    @Enumerated(EnumType.STRING)
    private OutputFormat outputFormat;

    private String shareUrl;

    @Builder
    public MockData(String name, User user, Integer rowCount, OutputFormat outputFormat, String shareUrl) {
        this.name = name;
        this.user = user;
        this.rowCount = rowCount;
        this.outputFormat = outputFormat;
        this.shareUrl = shareUrl;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    // fields 관련 메서드 추가 필요
    public void addFields(List<MockFieldRequest> fieldRequests) {
        List<MockField> mockFields = fieldRequests.stream()
                .map(request -> MockField.builder()
                        .fieldName(request.getFieldName())
                        .fieldType(request.getFieldType())
                        .format(request.getFormat())
                        .required(request.isRequired())
                        .defaultValue(request.getDefaultValue())
                        .customValues(request.getCustomValues())
                        .mockData(this)
                        .build())
                .collect(Collectors.toList());

        this.fields.addAll(mockFields);
    }
}