package com.bside405.perfume.project.mypage;

import com.bside405.perfume.project.exception.MyPerfumeConflictException;
import com.bside405.perfume.project.exception.MyPerfumeNotFound;
import com.bside405.perfume.project.exception.PerfumeNotFoundException;
import com.bside405.perfume.project.exception.UserNotFoundException;
import com.bside405.perfume.project.mypage.dto.*;
import com.bside405.perfume.project.perfume.Perfume;
import com.bside405.perfume.project.perfume.PerfumeRepository;
import com.bside405.perfume.project.oauth2.User;
import com.bside405.perfume.project.oauth2.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyPageService {
    private final UserRepository userRepository;
    private final PerfumeRepository perfumeRepository;
    private final MyPerfumeRepository myPerfumeRepository;
    private final HttpSession httpSession;

    public MyPerfume saveMyPerfume(OAuth2User principal, Long perfumeId) {
        log.debug("향수 저장 시작");
        User user = getCurrentUserFromOAuth2User(principal);
        Perfume perfume = getPerfumeById(perfumeId);
        log.debug("User : {}, Perfume : {}",user, perfume);

        Optional<MyPerfume>  myPerfumeOptional= myPerfumeRepository.findByUserIdAndPerfumeId(user.getId(), perfumeId);
        log.debug("myPerfumeOptional: {}, user.getId : {}, perfumeId : {}",myPerfumeOptional ,user.getId(), perfumeId);

        if (myPerfumeOptional.isPresent()) {
            throw new MyPerfumeConflictException("앨범에 향수가 이미 저장되어 있습니다.");
        } else {
            MyPerfume myPerfume = new MyPerfume();
            myPerfume.setUser(user);
            myPerfume.setPerfume(perfume);
            myPerfume.setAddedDate(LocalDateTime.now());
            return myPerfumeRepository.save(myPerfume);
        }
    }

    public MyPerfumeResponseDTO getMyPerfumeOnlyOne(OAuth2User principal, Long MyPerfumeId) {
        User user = getCurrentUserFromOAuth2User(principal);
        Long userId = user.getId();

        MyPerfume myPerfume = myPerfumeRepository.findById(MyPerfumeId)
                .orElseThrow(() -> new MyPerfumeNotFound("앨범에 저장된 향수를 찾을 수 없습니다."));

        if (!myPerfume.getUser().getId().equals(userId)) {
            throw new MyPerfumeNotFound("앨범에 저장된 향수를 찾을 수 없습니다.");
        }

        return convertToMyPerfumeResponseDTO(myPerfume);
    }

    public MyPerfumePaginationResponseDTO getUserPerfumes(OAuth2User principal, Pageable pageable) {
        log.debug("마이페이지 향수 읽기 시작");
        User user = getCurrentUserFromOAuth2User(principal);
        log.debug("user : {}", user);
        Page<MyPerfume> myPerfumePage = myPerfumeRepository.findAllByUserId(user.getId(), pageable);
        log.debug("myPerfumePage : {}", myPerfumePage);

        List<MyPerfumeResponseDTO> responseDTOs = new ArrayList<>();
        for (MyPerfume myPerfume : myPerfumePage.getContent()) {
            MyPerfumeResponseDTO responseDTO = convertToMyPerfumeResponseDTO(myPerfume);
            responseDTOs.add(responseDTO);
        }
        log.debug("마이페이지 향수 읽기 종료");
        return new MyPerfumePaginationResponseDTO(responseDTOs,
                                            myPerfumePage.getTotalPages(),
                                            myPerfumePage.getTotalElements());
    }

    public void removeMyPerfume(OAuth2User principal, List<Long> MyPerfumeIds) {
        User user = getCurrentUserFromOAuth2User(principal);
        Long userId = user.getId();

        for (Long myPerfumeId : MyPerfumeIds) {
            MyPerfume myPerfume = myPerfumeRepository.findById(myPerfumeId)
                    .orElseThrow(() -> new MyPerfumeNotFound("앨범에 저장된 향수를 찾을 수 없습니다."));

            if (myPerfume.getUser().getId().equals(userId)) {
                myPerfumeRepository.delete(myPerfume);
            }
        }
    }

    public void removeAccount(OAuth2User principal) {
        User user = getCurrentUserFromOAuth2User(principal);
        userRepository.delete(user);
        httpSession.invalidate();
        SecurityContextHolder.clearContext();
    }

    private Perfume getPerfumeById(Long perfumeId) {
        return perfumeRepository.findById(perfumeId)
                .orElseThrow(() -> new PerfumeNotFoundException("향수를 찾을 수 없습니다."));
    }

    private User getCurrentUserFromOAuth2User(OAuth2User principal) {
        log.debug("OAuth2User 객체로부터 현재 유저 찾기 시작, OAuth2User: {}", principal);
        String providerId = getProviderID(principal);
        log.debug("providerId: {}", providerId);
        return userRepository.findByProviderId(providerId)
                .orElseThrow(() -> new UserNotFoundException("유저를 찾을 수 없습니다."));
    }

    private String getProviderID(OAuth2User principal) {
        Map<String, Object> attributes = principal.getAttributes();
        log.debug("OAuth2User attributes: {}", attributes);
        if (attributes != null ) {
            String providerId = (String) attributes.get("id");
            log.debug("providerId : {}", providerId);
            return providerId;
        } else {
            throw new UserNotFoundException("유저를 찾을 수 없습니다.");
        }
    }

    private MyPerfumeResponseDTO convertToMyPerfumeResponseDTO(MyPerfume myPerfume) {
        MyPerfumeResponseDTO myPerfumeResponseDTO = new MyPerfumeResponseDTO();
        myPerfumeResponseDTO.setMyPerfumeId(myPerfume.getId());
        myPerfumeResponseDTO.setName(myPerfume.getPerfume().getName());
        myPerfumeResponseDTO.setEName(myPerfume.getPerfume().getEnglishName());
        myPerfumeResponseDTO.setBrand(myPerfume.getPerfume().getBrand());
        myPerfumeResponseDTO.setImageURL(myPerfume.getPerfume().getImageUrl());
        return myPerfumeResponseDTO;
    }

    public MyAccountInfoResponseDTO getMyAccountInfo(OAuth2User principal) {
        User user = getCurrentUserFromOAuth2User(principal);
        MyAccountInfoResponseDTO myAccount = new MyAccountInfoResponseDTO();
        myAccount.setName(user.getName());
        myAccount.setEmail(user.getEmail());
        return myAccount;
    }

    public List<ExistsCheckResponseDTO> checkIfAllRecommendedPerfumesExists(OAuth2User principal, ExistsCheckRequestDTO requestDTO) {
        User user = getCurrentUserFromOAuth2User(principal);
        Long userId = user.getId();

        List<Long> existingPerfumeIds = myPerfumeRepository.findExistingPerfumeIds(userId, requestDTO.getIds());
        Set<Long> existingPerfumeIdSet = new HashSet<>(existingPerfumeIds);

        List<ExistsCheckResponseDTO> existsCheckResponseDTOList = new ArrayList<>();
        for (Long perfumeId : requestDTO.getIds()) {
            boolean exists = existingPerfumeIdSet.contains(perfumeId);
            ExistsCheckResponseDTO existsCheckResponseDTO = new ExistsCheckResponseDTO(perfumeId, exists);
            existsCheckResponseDTOList.add(existsCheckResponseDTO);
        }

        return existsCheckResponseDTOList;
    }
}
