package com.example.AutomatizationQA.Services;

import com.example.AutomatizationQA.DTOs.ComponentDTO;
import com.example.AutomatizationQA.Models.Component;
import com.example.AutomatizationQA.Repositorys.ComponentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ComponentService {

    private final ComponentRepository componentRepository;

    public List<Component> getAllComponents() {
        return componentRepository.findAll();
    }

    public Optional<Component> getComponentById(Long id) {
        return componentRepository.findById(id);
    }

    public ComponentDTO getComponentWithRegions(Long id) {
        return componentRepository.findById(id)
                .map(app -> ComponentDTO.builder()
                        .id(app.getId())
                        .name(app.getName())
                        .description(app.getDescription())
                        .lastUpdated(app.getLastUpdated())
                        .regions(app.getRegions().stream()
                                .map(r -> ComponentDTO.RegionDTO.builder()
                                        .id(r.getId())
                                        .regionCode(r.getRegionCode())
                                        .ip(r.getIp())
                                        .port(r.getPort())
                                        .lastUpdated(r.getLastUpdated())
                                        .build())
                                .collect(Collectors.toList()))
                        .build()
                ).orElseThrow(() -> new RuntimeException("Application not found with id " + id));
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
}

