package com.example.AutomatizationQA.DTOs;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RegionDTO {
    private Long id;
    private String regionCode;
    private String ip;
    private String port;
    private String path;
    private LocalDateTime lastUpdated;
    private Long componentId;
}
