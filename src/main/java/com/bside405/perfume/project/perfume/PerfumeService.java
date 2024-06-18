package com.bside405.perfume.project.perfume;

import com.bside405.perfume.project.exception.ResourceNotFoundException;
import com.bside405.perfume.project.perfume.dto.PerfumeRequestDTO;
import com.bside405.perfume.project.perfume.dto.PerfumeResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PerfumeService {

    private final PerfumeRepository perfumeRepository;

    public PerfumeService(PerfumeRepository perfumeRepository) {
        this.perfumeRepository = perfumeRepository;
    }

    public List<PerfumeResponseDTO> getAllPerfumes() {
        List<Perfume> allPerfumes = perfumeRepository.findAll();
        return allPerfumes.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public PerfumeResponseDTO getPerfumeByID(Long id) {
        Perfume perfume = perfumeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id + "의 id를 가진 리소스를 찾을 수 없습니다."));
        return this.convertToDTO(perfume);
    }

    public PerfumeResponseDTO savePerfume(Long id, PerfumeRequestDTO perfumeRequest) {
        Perfume perfume;

        if (id == null) {
            perfume = new Perfume();
        } else {
            perfume = perfumeRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(id + "의 id를 가진 리소스를 찾을 수 없습니다."));
        }

        perfume.setName(perfumeRequest.getName());
        perfume.setEnglishName(perfumeRequest.getEName());
        perfume.setBrand(perfumeRequest.getBrand());
        perfume.setImageUrl(perfumeRequest.getImageURL());
        Perfume savedPerfume = perfumeRepository.save(perfume);
        return convertToDTO(savedPerfume);
    }

    public void deletePerfume(Long id) {
        Perfume perfume = perfumeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id + "의 id를 가진 리소스를 찾을 수 없습니다."));
        perfumeRepository.delete(perfume);
    }

    private PerfumeResponseDTO convertToDTO(Perfume perfume) {
        PerfumeResponseDTO perfumeResponseDTO = new PerfumeResponseDTO();
        perfumeResponseDTO.setId(perfume.getId());
        perfumeResponseDTO.setName(perfume.getName());
        perfumeResponseDTO.setEName(perfume.getEnglishName());
        perfumeResponseDTO.setBrand(perfume.getBrand());
        perfumeResponseDTO.setImageURL(perfume.getImageUrl());
        return perfumeResponseDTO;
    }
}
