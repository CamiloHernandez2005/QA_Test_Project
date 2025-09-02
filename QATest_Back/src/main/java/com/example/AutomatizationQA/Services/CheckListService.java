package com.example.AutomatizationQA.Services;

import com.example.AutomatizationQA.DTOs.CheckListDTO;
import com.example.AutomatizationQA.Models.CheckList;
import com.example.AutomatizationQA.Models.Region;
import com.example.AutomatizationQA.Repositorys.CheckListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CheckListService {

    private final CheckListRepository checkListRepository;

    public List<CheckListDTO> getAllCheckLists() {
        return checkListRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    public CheckListDTO getCheckListById(Long id) {
        return checkListRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Region not found with id " + id));
    }

    public CheckList createCheckList(CheckList checkList) {
        return checkListRepository.save(checkList);
    }

    public CheckList updateCheckList(Long id, CheckList checkListDetails) {
        return checkListRepository.findById(id).map(checkList -> {
            if (checkListDetails.getName() != null) {
                checkList.setName(checkListDetails.getName());
            }
            if (checkListDetails.getLastResult() != null) {
                checkList.setLastResult(checkListDetails.getLastResult());
            }
            Region region = checkList.getRegion();
            if (region != null) {
                region.setLastUpdated(LocalDateTime.now());
            }
            return checkListRepository.save(checkList);
        }).orElseThrow(() -> new RuntimeException("Check list not found with id " + id));
    }


    public void deleteCheckList(Long id) {
        checkListRepository.deleteById(id);
    }

    public CheckListDTO toDTO(CheckList checkList) {
        return CheckListDTO.builder()
                .id(checkList.getId())
                .name(checkList.getName())
                .lastResult(checkList.getLastResult())
                .lastUpdated(checkList.getLastUpdated())
                .regionId(checkList.getRegion() != null ? checkList.getRegion().getId() : null)
                .build();
    }
}