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
@RequestMapping("/api/myPage")
public class MyPageController {

    private final MyPageService myPageService;

    @GetMapping("/perfumes/check/{id}")
    public ResponseEntity<Boolean> checkIfRecommendedPerfumeExists( @AuthenticationPrincipal OAuth2User principal, @PathVariable Long id) {
        boolean exists = myPageService.checkIfRecommendedPerfumeExists(principal, id);
        return ResponseEntity.ok().body(exists);
    }

    @PostMapping("/perfumes/{id}")
    public ResponseEntity<Void> saveRecommendedPerfumeInAlbum(@AuthenticationPrincipal OAuth2User principal, @PathVariable Long id) {
        MyPerfume myPerfume = myPageService.saveMyPerfume(principal, id);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .buildAndExpand(myPerfume.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/perfumes/{id}")
    public ResponseEntity<MyPerfumeResponseDTO> getOneMyPerfume(@AuthenticationPrincipal OAuth2User principal, @PathVariable Long id) {
        MyPerfumeResponseDTO myPerfumeResponseDTO = myPageService.getMyPerfumeOnlyOne(principal, id);
        return ResponseEntity.ok(myPerfumeResponseDTO);
    }

    @GetMapping("/perfumes")
    public ResponseEntity<MyPerfumePaginationResponseDTO> getAllMyPerfumeList(@AuthenticationPrincipal OAuth2User principal, @PageableDefault(size = 6) Pageable pageable) {
        MyPerfumePaginationResponseDTO myPerfumePageResponseDTO = myPageService.getUserPerfumes(principal, pageable);
        return ResponseEntity.ok(myPerfumePageResponseDTO);
    }

    @DeleteMapping("/perfumes")
    public ResponseEntity<Void> removeMyPerfume(@AuthenticationPrincipal OAuth2User principal, @RequestBody IdsDeleteRequestDTO idsDeleteRequestDTO) {
        myPageService.removeMyPerfume(principal, idsDeleteRequestDTO.getIds());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/account")
    public ResponseEntity<MyAccountInfoResponseDTO> getMyAccountInfo(@AuthenticationPrincipal OAuth2User principal) {
        MyAccountInfoResponseDTO myAccountInfo = myPageService.getMyAccountInfo(principal);
        return ResponseEntity.ok(myAccountInfo);
    }

    @DeleteMapping("/account")
    public ResponseEntity<Void> withdrawAccount(@AuthenticationPrincipal OAuth2User principal) {
        myPageService.removeAccount(principal);
        return ResponseEntity.noContent().build();
    }
}
