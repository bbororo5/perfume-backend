package com.bside405.perfume.project.oauth2;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class TempController {

    @GetMapping("/")
    public RedirectView home() {
        return new RedirectView("https://perfume-client.vercel.app/");
    }

    @GetMapping("/error")
    public ResponseEntity<String> error() {
        return ResponseEntity.ok("테스트용 커스텀 에러 페이지");
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Healthy");
    }
}
