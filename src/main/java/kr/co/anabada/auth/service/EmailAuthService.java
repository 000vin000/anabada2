package kr.co.anabada.auth.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import kr.co.anabada.auth.entity.EmailAuthToken;
import kr.co.anabada.auth.repository.EmailAuthTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailAuthService {

    private final JavaMailSender mailSender;
    private final EmailAuthTokenRepository tokenRepository;

    @Value("${spring.mail.username}")
    private String fromAddress;

    //인증번호 생성 메일 전송
    public void sendVerificationCode(String email) throws MessagingException {
        String code = generateRandomCode(); // 인증번호 생성
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(5); // 유효기간 5분

        // DB 저장
        EmailAuthToken token = EmailAuthToken.builder()
                .email(email)
                .code(code)
                .expiresAt(expiresAt)
                .isUsed(false)
                .build();
        tokenRepository.save(token);

        // 이메일 전송
        String subject = "[Anabada] 이메일 인증번호 안내";
        String htmlContent = "<p>아래 인증번호를 입력해주세요:</p>"
                           + "<h2>" + code + "</h2>"
                           + "<p>유효시간: 5분</p>";
        sendHtmlMail(email, subject, htmlContent);
    }

    //인증번호 검증
    public boolean verifyCode(String email, String code) {
        Optional<EmailAuthToken> optional = tokenRepository.findTopByEmailAndCodeOrderByIdDesc(email, code);
        if (optional.isEmpty()) return false;

        EmailAuthToken token = optional.get();

        if (token.isUsed()) return false;
        if (token.getExpiresAt().isBefore(LocalDateTime.now())) return false;

        token.setUsed(true);
        tokenRepository.save(token);
        return true;
    }

    //내부 메일
    private void sendHtmlMail(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        helper.setFrom(fromAddress);

        mailSender.send(message);
    }

    //6자리 인증번호 생성
    private String generateRandomCode() {
        Random random = new Random();
        int number = random.nextInt(900000) + 100000; // 100000 ~ 999999
        return String.valueOf(number);
    }
}
