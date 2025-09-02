package com.example.AutomatizationQA.Controllers;

import com.example.AutomatizationQA.DTOs.ComponentDTO;
import com.example.AutomatizationQA.Models.Component;
import com.example.AutomatizationQA.Services.ComponentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/components")
@RequiredArgsConstructor
public class ComponentController {

    private final ComponentService componentService;

    @GetMapping
    public ResponseEntity<List<ComponentDTO>> getAllComponents() {
        return ResponseEntity.ok(componentService.getAllComponents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComponentDTO> getComponentById(@PathVariable Long id) {
        return ResponseEntity.ok(componentService.getComponentById(id));

    }

    @PostMapping("/create")
    public ResponseEntity<Component> createComponent(@RequestBody Component component) {
        return ResponseEntity.ok(componentService.createComponent(component));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Component> updateComponent(
            @PathVariable Long id,
            @RequestBody Component component) {
        return ResponseEntity.ok(componentService.updateComponent(id, component));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComponent(@PathVariable Long id) {
        componentService.deleteComponent(id);
        return ResponseEntity.noContent().build();
    }
}
