package com.earth_pointer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class MailService {

    private final JavaMailSender mailSender;

    @Autowired
    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmailVerification(String email) {
        int verificationCode = generateRandomCode();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("이메일 인증 요청");
        message.setText("인증 코드: " + verificationCode);

        mailSender.send(message);
        System.out.println("이메일을 성공적으로 보냈습니다.");
    }

    private int generateRandomCode() {
        Random rand = new Random();
        return rand.nextInt(900000) + 100000;
    }
}