package com.earth_pointer.controller;

import com.earth_pointer.domain.User;
import com.earth_pointer.dto.PasswordDTO;
import com.earth_pointer.service.MailService;
import com.earth_pointer.service.UserService;
import com.earth_pointer.dto.UserDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    private final MailService mailService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(MailService mailService, UserService userService, PasswordEncoder passwordEncoder) {
        this.mailService = mailService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    // 메인 페이지
    @GetMapping({"", "/"})
    public String home() {
        return "home";
    }

    // 로그인 페이지
    @GetMapping("/login")
    public String login() {
        return "index/login";
    }

    // 비밀번호 변경
    @GetMapping("/user/change")
    public String passwordChange(Model model) {
        model.addAttribute("password", new PasswordDTO());
        return "index/changePassword";
    }

    @PostMapping("/user/change")
    public String changePassword(@Valid @ModelAttribute("password") PasswordDTO passwordDTO,
                                 BindingResult bindingResult,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("password", passwordDTO);
            return "index/changePassword";
        }
        String session = SecurityContextHolder.getContext().getAuthentication().getName();
        int userId = Integer.parseInt(session);
        String password = passwordEncoder.encode(passwordDTO.getPassword());
        userService.passwordChange(userId, password);
        return "redirect:/";
    }

    // 회원가입
    @GetMapping("/user/join")
    public String join(Model model) {
        model.addAttribute("user", new UserDTO());
        return "index/join";
    }

    @PostMapping("/user/new")
    public String joinSubmit(@Valid @ModelAttribute("user") UserDTO userDTO,
                             BindingResult bindingResult,
                             Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("user", userDTO);
            return "index/join";
        }

        int certificationCode = mailService.sendEmailVerification(userDTO.getEmail());
        model.addAttribute("certificationCode", certificationCode);
        model.addAttribute("user", userDTO);
        return "index/sendVerificationEmail";
    }

    //
    @PostMapping("/user/certification")
    public String handleCertification(
            @ModelAttribute UserDTO userDTO,
            @RequestParam("certificationCode") String certificationCode,
            @RequestParam("inputCode") String inputCode) {

        if(certificationCode.equals(inputCode)) {
            User user = new User();
            user.setUsername(userDTO.getUsername());
            user.setPasswordHash(passwordEncoder.encode(userDTO.getPassword()));
            user.setEmail(userDTO.getEmail());
            try {
                userService.join(user);
            } catch (RuntimeException e) {
                System.out.println(e);
                return "redirect:/user/join";
            }
            System.out.println(0);
            return "redirect:/";
        } else {
            System.out.println(1);
            return "redirect:/user/join";
        }
    }
}