package com.example.AutomatizationQA.DTOs;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class CheckListDTO {
    private Long id;
    private String name;
    private String lastResult;
    private LocalDateTime lastUpdated;
    private Long regionId;
}
