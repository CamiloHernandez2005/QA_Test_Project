package com.example.AutomatizationQA.Services;

import com.example.AutomatizationQA.DTOs.RegionDTO;
import com.example.AutomatizationQA.Models.Component;
import com.example.AutomatizationQA.Models.Region;
import com.example.AutomatizationQA.Repositorys.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegionService {

    private final RegionRepository regionRepository;

    public List<RegionDTO> getAllRegions() {
        return regionRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    public RegionDTO getRegionById(Long id) {
        return regionRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Region not found with id " + id));
    }

    public Region createRegion(Region region) {
        return regionRepository.save(region);
    }

    public Region updateRegion(Long id, Region regionDetails) {
        return regionRepository.findById(id).map(region -> {
            if (regionDetails.getRegionCode() != null) {
                region.setRegionCode(regionDetails.getRegionCode());
            }
            if (regionDetails.getIp() != null) {
                region.setIp(regionDetails.getIp());
            }
            if (regionDetails.getPort() != null) {
                region.setPort(regionDetails.getPort());
            }
            Component component = region.getComponent();
            if (component != null) {
                component.setLastUpdated(LocalDateTime.now());
            }
            return regionRepository.save(region);
        }).orElseThrow(() -> new RuntimeException("Region not found with id " + id));
    }


    public void deleteRegion(Long id) {
        regionRepository.deleteById(id);
    }

    public RegionDTO toDTO(Region region) {
        return RegionDTO.builder()
                .id(region.getId())
                .regionCode(region.getRegionCode())
                .ip(region.getIp())
                .port(region.getPort())
                .lastUpdated(region.getLastUpdated())
                .componentId(region.getComponent() != null ? region.getComponent().getId() : null)
                .build();
    }
}

