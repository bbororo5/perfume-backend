package com.bside405.perfume.project.perfume;

import com.bside405.perfume.project.perfume.dto.PerfumeRequestDTO;
import com.bside405.perfume.project.perfume.dto.PerfumeResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PerfumeController {

    private final PerfumeService perfumeService;

    public PerfumeController(PerfumeService perfumeService) {
        this.perfumeService = perfumeService;
    }

    @GetMapping("/perfumes")
    public ResponseEntity<List<PerfumeResponseDTO>> getAllPerfumes() {
        List<PerfumeResponseDTO> listPerfume = perfumeService.getAllPerfumes();
        return ResponseEntity.ok(listPerfume);
    }

    @GetMapping("/perfumes/{id}")
    public ResponseEntity<PerfumeResponseDTO> getOnePerfume(@PathVariable Long id) {
        PerfumeResponseDTO perfumeResponseDTO = perfumeService.getPerfumeByID(id);
        return ResponseEntity.ok(perfumeResponseDTO);
    }

    @PostMapping("/perfumes")
    public ResponseEntity<Void> createPerfume(@Valid @RequestBody PerfumeRequestDTO perfumeRequest) {
        PerfumeResponseDTO createdPerfume = perfumeService.savePerfume(perfumeRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdPerfume.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/perfumes/{id}")
    public ResponseEntity<PerfumeResponseDTO> updatePerfume(@PathVariable Long id, @Valid @RequestBody PerfumeRequestDTO perfumeRequest) {
        PerfumeResponseDTO perfumeResponseDTO = perfumeService.updatePerfume(id, perfumeRequest);
        return ResponseEntity.ok(perfumeResponseDTO);
    }

    @DeleteMapping("/perfumes/{id}")
    public ResponseEntity<Void> deletePerfume(@PathVariable Long id) {
        perfumeService.deletePerfume(id);
        return ResponseEntity.noContent().build();
    }
}
