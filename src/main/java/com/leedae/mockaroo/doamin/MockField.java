package com.leedae.mockaroo.doamin;

import com.leedae.mockaroo.doamin.constant.mock.FieldType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "mock_fields")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MockField extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fieldName;

    @Enumerated(EnumType.STRING)
    private FieldType fieldType;

    private String format;

    private boolean required;

    private String defaultValue;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mock_data_id")
    private MockData mockData;

    @ElementCollection
    @CollectionTable(name = "custom_values", joinColumns = @JoinColumn(name = "mock_field_id"))
    private List<String> customValues = new ArrayList<>();

    @Builder
    public MockField(String fieldName, FieldType fieldType, String format,
                     boolean required, String defaultValue, MockData mockData,
                     List<String> customValues) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
        this.format = format;
        this.required = required;
        this.defaultValue = defaultValue;
        this.mockData = mockData;
        if (customValues != null) {
            this.customValues = customValues;
        }
    }
}