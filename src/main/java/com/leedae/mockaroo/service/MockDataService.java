package com.leedae.mockaroo.service;

import com.leedae.mockaroo.doamin.MockData;
import com.leedae.mockaroo.doamin.User;
import com.leedae.mockaroo.dto.request.MockDataCreateRequest;
import com.leedae.mockaroo.dto.request.MockDataUpdateRequest;
import com.leedae.mockaroo.dto.response.MockDataGeneratedResponse;
import com.leedae.mockaroo.dto.response.MockDataPageResponse;
import com.leedae.mockaroo.dto.response.MockDataResponse;
import com.leedae.mockaroo.repository.MockDataRepository;
import com.leedae.mockaroo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class MockDataService {
    private final MockDataRepository mockDataRepository;
    private final UserRepository userRepository;
    private final MockDataGenerator mockDataGenerator;

    public MockDataResponse createMockData(String email, MockDataCreateRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        MockData mockData = MockData.builder()
                .name(request.getName())
                .rowCount(request.getRowCount())
                .outputFormat(request.getOutputFormat())
                .shareUrl(generateShareUrl())
                .user(user)
                .build();

        mockData.addFields(request.getFields());
        return MockDataResponse.from(mockDataRepository.save(mockData));
    }

    @Transactional(readOnly = true)
    public MockDataPageResponse getMockDataList(String email, Pageable pageable) {
        Page<MockData> mockDataPage = mockDataRepository.findByUserEmail(email, pageable);
        return MockDataPageResponse.from(mockDataPage);
    }

    @Transactional(readOnly = true)
    public MockDataResponse getMockData(String email, Long mockDataId) {
        return MockDataResponse.from(findMockDataByIdAndEmail(mockDataId, email));
    }

    public MockDataResponse updateMockData(String email, Long mockDataId, MockDataUpdateRequest request) {
        MockData mockData = findMockDataByIdAndEmail(mockDataId, email);
        mockData.updateName(request.getName());
        return MockDataResponse.from(mockData);
    }

    public void deleteMockData(String email, Long mockDataId) {
        MockData mockData = findMockDataByIdAndEmail(mockDataId, email);
        mockDataRepository.delete(mockData);
    }

    @Transactional(readOnly = true)
    public MockDataGeneratedResponse generateMockData(Long mockDataId) {
        MockData mockData = mockDataRepository.findById(mockDataId)
                .orElseThrow(() -> new RuntimeException("Mock 데이터를 찾을 수 없습니다."));

        String generatedData = mockDataGenerator.generate(mockData);

        return MockDataGeneratedResponse.builder()
                .data(generatedData)
                .format(mockData.getOutputFormat().name())
                .rowCount(mockData.getRowCount())
                .build();
    }

    private MockData findMockDataByIdAndEmail(Long mockDataId, String email) {
        return mockDataRepository.findByIdAndUserEmail(mockDataId, email)
                .orElseThrow(() -> new RuntimeException("Mock 데이터를 찾을 수 없습니다."));
    }

    private String generateShareUrl() {
        return UUID.randomUUID().toString();
    }
}