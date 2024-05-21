package com.bside405.perfume.project.mypage;

import com.bside405.perfume.project.exception.PerfumeNotFoundException;
import com.bside405.perfume.project.perfume.Perfume;
import com.bside405.perfume.project.perfume.PerfumeRepository;
import com.bside405.perfume.project.user.User;
import com.bside405.perfume.project.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MyPageService {
    private final UserRepository userRepository;
    private final PerfumeRepository perfumeRepository;


    public void saveMyPerfume(OAuth2User principal, Long perfumeId) {
        String providerId = getProviderID(principal);

        User user = userRepository.findByProviderId(providerId)
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다."));

        Perfume perfume = perfumeRepository.findById(perfumeId)
                .orElseThrow(() -> new PerfumeNotFoundException("향수를 찾을 수 없습니다."));

        user.addPerfume(perfume);
        userRepository.save(user);
    }

    public List<Perfume> getUserPerfumes(OAuth2User principal) {
        String providerId = getProviderID(principal);

        User user = userRepository.findByProviderId(providerId)
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다."));

        return user.getUserPerfumes();
    }

    public void removeMyPerfume(OAuth2User principal, Long perfumeId) {
        String providerId = getProviderID(principal);

        User user = userRepository.findByProviderId(providerId)
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다."));

        Perfume perfume = perfumeRepository.findById(perfumeId)
                .orElseThrow(() -> new PerfumeNotFoundException("향수를 찾을 수 없습니다."));

        user.removePerfume(perfume);
        userRepository.save(user);
    }

    public void removeAccount(OAuth2User principal) {
        String providerId = getProviderID(principal);
        User user = userRepository.findByProviderId(providerId)
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾지 못하였습니다."));
        userRepository.delete(user);
    }

    private String getProviderID(OAuth2User principal) {
        Map<String, String> response = principal.getAttribute("response");
        if (response != null ) {
            return response.get("id");
        } else {
            throw new UsernameNotFoundException("유저를 찾지 못하였습니다.");
        }
    }
}
