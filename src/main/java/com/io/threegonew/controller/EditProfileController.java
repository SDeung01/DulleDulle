package com.io.threegonew.controller;

import com.io.threegonew.domain.User;
import com.io.threegonew.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/editprofile") // editprofile 경로로 매핑
@RequiredArgsConstructor
public class EditProfileController {
    private final UserService userService;

    @GetMapping
    public String showEditProfilePage(Model model) {
        // 현재 인증된 사용자의 아이디와 이메일 주소 가져오기
        String userId = userService.getCurrentUserId();
        String email = userService.getCurrentUserEmail();

        // 모델에 사용자 정보 추가
        model.addAttribute("userId", userId);
        model.addAttribute("email", email);

        return "editprofile";
    }
}
