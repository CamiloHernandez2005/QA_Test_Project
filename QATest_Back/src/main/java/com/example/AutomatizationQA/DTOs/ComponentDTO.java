package com.example.AutomatizationQA.DTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ComponentDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime lastUpdated;
    private List<RegionDTO> regions;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RegionDTO {
        private Long id;
        private String regionCode;
        private String ip;
        private String port;
        private LocalDateTime lastUpdated;
    }
}
