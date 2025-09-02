package com.example.AutomatizationQA.Services;

import com.example.AutomatizationQA.DTOs.ComponentDTO;
import com.example.AutomatizationQA.DTOs.RegionDTO;
import com.example.AutomatizationQA.Models.Component;
import com.example.AutomatizationQA.Repositorys.ComponentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ComponentService {

    private final ComponentRepository componentRepository;

    public List<ComponentDTO> getAllComponents() {
        return componentRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    public ComponentDTO getComponentById(Long id) {

        return componentRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Region not found with id " + id));
    }

    public Component createComponent(Component component) {
        return componentRepository.save(component);
    }

    public Component updateComponent(Long id, Component componentDetails) {
        return componentRepository.findById(id).map(app -> {
            if (componentDetails.getName() != null) {
                app.setName(componentDetails.getName());
            }
            if (componentDetails.getDescription() != null) {
                app.setDescription(componentDetails.getDescription());
            }
            if (componentDetails.getRegions() != null) {
                app.getRegions().clear();
                app.getRegions().addAll(componentDetails.getRegions());
            }
            return componentRepository.save(app);
        }).orElseThrow(() -> new RuntimeException("Component not found with id " + id));
    }


    public void deleteComponent(Long id) {
        componentRepository.deleteById(id);
    }

    public ComponentDTO toDTO(Component component) {
        return ComponentDTO.builder()
                .id(component.getId())
                .name(component.getName())
                .description(component.getDescription())
                .createdDate(component.getCreatedDate())
                .lastUpdated(component.getLastUpdated())
                .regions(component.getRegions().stream()
                        .map(r -> RegionDTO.builder()
                                .id(r.getId())
                                .regionCode(r.getRegionCode())
                                .ip(r.getIp())
                                .port(r.getPort())
                                .lastUpdated(r.getLastUpdated())
                                .componentId(r.getComponent().getId())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}

