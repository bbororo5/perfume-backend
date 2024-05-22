package com.bside405.perfume.project.perfume;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
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
    public ResponseEntity<PerfumeResponseDTO> getPerfumeById(@PathVariable Long id) {
        PerfumeResponseDTO perfumeResponseDTO = perfumeService.getPerfumeByID(id);
        return ResponseEntity.ok(perfumeResponseDTO);
    }

    //201 상태코드가 담긴 응답의 헤더에는 location 헤더가 들어가며
    // 이 헤더에는 클라이언트가 입력한 정보의 리소스를 확인할 수 있는 URL이 담겨있음. 혹은 리다이렉션(상태코드: 301,302)
    @PostMapping("/perfumes")
    public ResponseEntity<Void> createPerfume(@Valid @RequestBody PerfumeRequestDTO perfumeRequest) {
        PerfumeResponseDTO createdPerfume = perfumeService.savePerfume(perfumeRequest);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdPerfume.getId())//아무나 자원에 접근할 수 없게 보안처리도 고민하면 좋음.
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
