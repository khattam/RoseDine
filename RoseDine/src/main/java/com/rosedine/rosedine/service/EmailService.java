package com.rosedine.rosedine.service;

import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.sendgrid.*;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

@Service
public class EmailService {

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${spring.sendgrid.api-key}")
    private String sendgridApiKey;

    public String sendVerificationEmail(String email) throws IOException {
        String code = String.valueOf((int)(Math.random() * 9000) + 1000); // Generate 4-digit code
        sendEmail(email, "Verify Your Email", "Your verification code is: " + code);

        return Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject(email)
                .claim("code", code)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 10 * 60 * 1000)) // 10 minutes expiration
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    public boolean validateVerificationToken(String token, String code) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token)
                    .getBody();

            String email = claims.getSubject();
            return email != null && claims.get("code").equals(code);
        } catch (Exception e) {
            return false;
        }
    }

    private void sendEmail(String to, String subject, String body) throws IOException {
        Email from = new Email("noreplyrosedine@gmail.com"); // Ensure this is verified in SendGrid
        Email toEmail = new Email(to);
        Content content = new Content("text/plain", body);
        Mail mail = new Mail(from, subject, toEmail, content);

        SendGrid sg = new SendGrid(sendgridApiKey);

        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            throw new IOException("Error sending email", ex);
        }
    }
}
