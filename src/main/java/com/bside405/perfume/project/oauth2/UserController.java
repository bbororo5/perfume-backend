package com.bside405.perfume.project.oauth2;

import com.bside405.perfume.project.mypage.MyPageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(MyPageService.class);

    @GetMapping("/login/check")
    public ResponseEntity<?> check(@AuthenticationPrincipal OAuth2User principal) {
        logger.debug("로그인 체크 시작");
        logger.debug("principal: {}", principal);
        if (principal != null) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
