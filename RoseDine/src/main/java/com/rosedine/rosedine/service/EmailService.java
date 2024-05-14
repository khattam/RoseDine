package com.rosedine.rosedine.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;

@Service
public class EmailService {

    private static final String SECRET_KEY = "your_secret_key";

    @Autowired
    private JavaMailSender mailSender;

    public String send2FACode(String email) {
        String code = String.valueOf((int)(Math.random() * 9000) + 1000); // Generate 4-digit code
        sendEmail(email, "Your 2FA Code", "Your code is: " + code);

        return Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject(email)
                .claim("code", code)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 10 * 60 * 1000)) // 10 minutes expiration
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public boolean validate2FAToken(String token, String code) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject() != null && claims.get("code").equals(code);
        } catch (Exception e) {
            return false;
        }
    }

    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("no-reply@rosedine.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}
