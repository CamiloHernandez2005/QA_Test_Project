package com.example.AutomatizationQA.Controllers;

import com.example.AutomatizationQA.DTOs.RegionDTO;
import com.example.AutomatizationQA.Models.Region;
import com.example.AutomatizationQA.Services.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

        import java.util.List;

@RestController
@RequestMapping("/regions")
@RequiredArgsConstructor
public class RegionController {

    private final RegionService regionService;

    @GetMapping
    public List<Region> getAllRegions() {
        return regionService.getAllRegions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Region> getRegionById(@PathVariable Long id) {
        return regionService.getRegionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<RegionDTO> createRegion(@RequestBody  Region dto) {
        Region region = regionService.createRegion(dto);
        return ResponseEntity.ok(regionService.toDTO(region));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegionDTO> updateRegion(
            @PathVariable Long id,
            @RequestBody Region dto) {
        Region region = regionService.updateRegion(id, dto);
        return ResponseEntity.ok(regionService.toDTO(region));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRegion(@PathVariable Long id) {
        regionService.deleteRegion(id);
        return ResponseEntity.noContent().build();
    }
}
