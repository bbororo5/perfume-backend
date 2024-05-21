package com.bside405.perfume.project.mypage;

import com.bside405.perfume.project.perfume.Perfume;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class MypageController {

    private final MyPageService myPageService;

    @PostMapping("/myPage/perfumes/{id}")
    public ResponseEntity<Void> saveMyPerfume(@AuthenticationPrincipal OAuth2User principal, @PathVariable Long id) {
        myPageService.saveMyPerfume(principal, id);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/myPage/perfumes")
    public ResponseEntity<List<Perfume>> getUserPerfumes(@AuthenticationPrincipal OAuth2User principal) {
        List<Perfume> perfumes = myPageService.getUserPerfumes(principal);
        return ResponseEntity.ok(perfumes);
    }

    @DeleteMapping("/myPage/{id}")
    public ResponseEntity<Void> removeMyPerfume(@AuthenticationPrincipal OAuth2User principal, @PathVariable Long id) {
        myPageService.removeMyPerfume(principal, id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/mypage/account")
    public ResponseEntity<Void> removeAccount(@AuthenticationPrincipal OAuth2User principal) {
        myPageService.removeAccount(principal);
        return ResponseEntity.noContent().build();
    }
}
