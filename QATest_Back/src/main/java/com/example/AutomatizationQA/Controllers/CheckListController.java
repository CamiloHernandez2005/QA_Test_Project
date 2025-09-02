package com.example.AutomatizationQA.Controllers;

import com.example.AutomatizationQA.DTOs.CheckListDTO;
import com.example.AutomatizationQA.Models.CheckList;
import com.example.AutomatizationQA.Services.CheckListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/check-list")
@RequiredArgsConstructor
public class CheckListController {

    private final CheckListService checkListService;

    @GetMapping
    public ResponseEntity<List<CheckListDTO>> getAllCheckLists() {
        return ResponseEntity.ok(checkListService.getAllCheckLists());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CheckListDTO> getCheckListById(@PathVariable Long id) {
        return ResponseEntity.ok(checkListService.getCheckListById(id));

    }

    @PostMapping("/create")
    public ResponseEntity<CheckListDTO> createCheckList(@RequestBody CheckList dto) {
        CheckList checkList = checkListService.createCheckList(dto);
        return ResponseEntity.ok(checkListService.toDTO(checkList));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CheckListDTO> updateCheckList(
            @PathVariable Long id,
            @RequestBody CheckList dto) {
        CheckList checkList = checkListService.updateCheckList(id, dto);
        return ResponseEntity.ok(checkListService.toDTO(checkList));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCheckList(@PathVariable Long id) {
        checkListService.deleteCheckList(id);
        return ResponseEntity.noContent().build();
    }
}
