package com.leedae.mockaroo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leedae.mockaroo.doamin.MockData;
import com.leedae.mockaroo.doamin.MockField;
import com.leedae.mockaroo.doamin.constant.mock.OutputFormat;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MockDataGenerator {
    private final ObjectMapper objectMapper;
    private final Faker faker = new Faker(new Locale("ko"));

    public String generate(MockData mockData) {
        List<Map<String, Object>> generatedData = new ArrayList<>();

        for (int i = 0; i < mockData.getRowCount(); i++) {
            Map<String, Object> row = new HashMap<>();
            for (MockField field : mockData.getFields()) {
                row.put(field.getFieldName(), generateFieldValue(field));
            }
            generatedData.add(row);
        }

        return convertToFormat(generatedData, mockData.getOutputFormat());
    }

    private Object generateFieldValue(MockField field) {
        return switch (field.getFieldType()) {
            case STRING -> generateString(field);
            case NUMBER -> generateNumber(field);
            case DATE -> generateDate(field);
            case BOOLEAN -> faker.bool().bool();
            case EMAIL -> faker.internet().emailAddress();
            case PHONE -> faker.phoneNumber().phoneNumber();
            case ADDRESS -> generateAddress();
            case NAME -> faker.name().fullName();
            case CUSTOM_LIST -> generateCustomList(field);
            case UUID -> UUID.randomUUID().toString();
            case IP_ADDRESS -> faker.internet().ipV4Address();
        };
    }

    private Object generateNumber(MockField field) {
        return faker.number().numberBetween(0, 1000);
    }

    private String generateString(MockField field) {
        return faker.lorem().word();
    }

    private String generateDate(MockField field) {
        return LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
    }

    private String generateAddress() {
        return faker.address().fullAddress();
    }

    private String generateCustomList(MockField field) {
        List<String> customValues = field.getCustomValues();
        if (customValues == null || customValues.isEmpty()) {
            return null;
        }
        return customValues.get(faker.number().numberBetween(0, customValues.size()));
    }

    private String convertToFormat(List<Map<String, Object>> data, OutputFormat format) {
        try {
            return switch (format) {
                case JSON -> objectMapper.writeValueAsString(data);
                case CSV -> convertToCsv(data);
                case XML -> convertToXml(data);
            };
        } catch (Exception e) {
            throw new RuntimeException("데이터 변환 중 오류가 발생했습니다.", e);
        }
    }

    private String convertToCsv(List<Map<String, Object>> data) {
        if (data.isEmpty()) return "";

        StringBuilder csv = new StringBuilder();

        // 헤더 추가
        csv.append(String.join(",", data.get(0).keySet())).append("\n");

        // 데이터 추가
        for (Map<String, Object> row : data) {
            csv.append(String.join(",", row.values().stream()
                            .map(String::valueOf)
                            .toList()))
                    .append("\n");
        }

        return csv.toString();
    }

    private String convertToXml(List<Map<String, Object>> data) {
        StringBuilder xml = new StringBuilder();
        xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        xml.append("<data>\n");

        for (Map<String, Object> row : data) {
            xml.append("  <row>\n");
            for (Map.Entry<String, Object> entry : row.entrySet()) {
                xml.append("    <")
                        .append(entry.getKey())
                        .append(">")
                        .append(entry.getValue())
                        .append("</")
                        .append(entry.getKey())
                        .append(">\n");
            }
            xml.append("  </row>\n");
        }

        xml.append("</data>");
        return xml.toString();
    }
}