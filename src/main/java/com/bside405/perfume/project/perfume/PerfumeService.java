package com.bside405.perfume.project.perfume;

import com.bside405.perfume.project.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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


    public PerfumeResponseDTO savePerfume(PerfumeRequestDTO perfumeRequest) {
        Perfume perfume = new Perfume();
        perfume.setName(perfumeRequest.getName());
        perfume.setEName(perfumeRequest.getEName());
        perfume.setBrand(perfumeRequest.getBrand());
        perfume.setImageURL(perfumeRequest.getImageURL());
        Perfume savedPerfume = perfumeRepository.save(perfume);
        return convertToDTO(savedPerfume);
    }

    public PerfumeResponseDTO getPerfumeByID(Long id) {
        Optional<Perfume> optionalPerfume = perfumeRepository.findById(id);

        if (optionalPerfume.isPresent()) {
            Perfume perfume = optionalPerfume.get();
            PerfumeResponseDTO perfumeResponseDTO = new PerfumeResponseDTO();
            perfumeResponseDTO.setId(perfume.getId());
            perfumeResponseDTO.setName(perfume.getName());
            perfumeResponseDTO.setEName(perfume.getEName());
            perfumeResponseDTO.setBrand(perfume.getBrand());
            perfumeResponseDTO.setImageURL(perfume.getImageURL());
            return perfumeResponseDTO;
        } else {
            throw new ResourceNotFoundException(id + "의 id를 가진 리소스를 찾을 수 없습니다.");
        }
    }

    public PerfumeResponseDTO updatePerfume(Long id, PerfumeRequestDTO perfumeRequest) {
        Perfume perfume = perfumeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id + "의 id를 가진 리소스를 찾을 수 없습니다."));
        perfume.setName(perfumeRequest.getName());
        perfume.setEName(perfumeRequest.getEName());
        perfume.setBrand(perfumeRequest.getBrand());
        perfume.setImageURL(perfumeRequest.getImageURL());
        Perfume updatedPerfume = perfumeRepository.save(perfume);
        return convertToDTO(updatedPerfume);
    }

    public PerfumeResponseDTO convertToDTO(Perfume perfume) {
        PerfumeResponseDTO perfumeResponseDTO = new PerfumeResponseDTO();
        perfumeResponseDTO.setId(perfume.getId());
        perfumeResponseDTO.setName(perfume.getName());
        perfumeResponseDTO.setEName(perfume.getEName());
        perfumeResponseDTO.setBrand(perfume.getBrand());
        perfumeResponseDTO.setImageURL(perfume.getImageURL());
        return perfumeResponseDTO;
    }

    public void deletePerfume(Long id) {
        Perfume perfume = perfumeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id + "의 id를 가진 리소스를 찾을 수 없습니다."));
        perfumeRepository.delete(perfume);
    }
}
