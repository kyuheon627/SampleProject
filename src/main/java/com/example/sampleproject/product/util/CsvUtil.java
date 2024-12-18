package com.example.sampleproject.product.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CsvUtil {

    public List<Map<String, String>> loadCsv(String filename) {
        try {
            // src/main/resources/static/csv 경로에서 파일 로드
            ClassPathResource resource = new ClassPathResource("static/csv/" + filename);

            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), "UTF-8"))) {

                // 첫 줄은 헤더로 처리
                List<String> headers = List.of(br.readLine().split(","));
                return br.lines().map(line -> {
                    String[] values = line.split(",");
                    return headers.stream()
                            .collect(Collectors.toMap(h -> h, h -> values[headers.indexOf(h)]));
                }).collect(Collectors.toList());

            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}