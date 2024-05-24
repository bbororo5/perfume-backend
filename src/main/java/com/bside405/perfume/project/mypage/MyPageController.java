package com.bside405.perfume.project.mypage;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;

@RestController
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService myPageService;

    @PostMapping("/myPage/perfumes/{id}")
    public ResponseEntity<Void> saveMyPerfume(@AuthenticationPrincipal OAuth2User principal, @PathVariable Long id) {
        MyPerfume myPerfume = myPageService.saveMyPerfume(principal, id);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .buildAndExpand(myPerfume.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/myPage/perfumes/{id}")
    public ResponseEntity<MyPerfumeResponseDTO> getOneMyPerfume(@AuthenticationPrincipal OAuth2User principal, @PathVariable Long id) {
        MyPerfumeResponseDTO myPerfumeResponseDTO = myPageService.getMyPerfumeOnlyOne(principal, id);
        return ResponseEntity.ok(myPerfumeResponseDTO);
    }

    @GetMapping("/myPage/perfumes")
    public ResponseEntity<MyPerfumePaginationResponseDTO> getAllMyPerfumes(@AuthenticationPrincipal OAuth2User principal, @PageableDefault(size = 6) Pageable pageable) {
        MyPerfumePaginationResponseDTO myPerfumePageResponseDTO = myPageService.getUserPerfumes(principal, pageable);
        return ResponseEntity.ok(myPerfumePageResponseDTO);
    }

    @DeleteMapping("/myPage/perfumes")
    public ResponseEntity<Void> removeMyPerfume(@AuthenticationPrincipal OAuth2User principal, @RequestBody IdsDeleteRequestDTO idsDeleteRequestDTO) {
        myPageService.removeMyPerfume(principal, idsDeleteRequestDTO.getIds());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/myPage/account")
    public ResponseEntity<MyAccountInfoResponseDTO> getMyaccount(@AuthenticationPrincipal OAuth2User principal) {
        MyAccountInfoResponseDTO myAccountInfo = myPageService.getMyAccountInfo(principal);
        return ResponseEntity.ok(myAccountInfo);
    }

    @DeleteMapping("/myPage/account")
    public ResponseEntity<Void> removeAccount(@AuthenticationPrincipal OAuth2User principal) {
        myPageService.removeAccount(principal);
        return ResponseEntity.noContent().build();
    }
}
