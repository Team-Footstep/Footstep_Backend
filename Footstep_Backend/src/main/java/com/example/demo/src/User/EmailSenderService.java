package com.example.demo.src.User;

import com.example.demo.src.User.model.GetEmailCertReq;
import com.example.demo.src.User.model.GetEmailCertRes;
import com.example.demo.src.User.model.PostUserRes;
import com.example.demo.src.User.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class EmailSenderService {
    private final JavaMailSender javaMailSender;
    private final EmailCertDao emailCertDao;

    public void sendMail(String token, String email) throws MessagingException {
        System.out.println("이메일 보내는데까지 왔어여");
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        mimeMessageHelper.setFrom("footstep2022@naver.com");
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("[Footstep] 회원가입 이메일 인증");

        StringBuilder body = new StringBuilder();
        body.append("회원가입을 하시려면 다음 링크를 클릭해주세요. \n");
        body.append("http://localhost:8080/users/signup/confirm?email=" + email +
                "&token=" + token);
        mimeMessageHelper.setText(body.toString()) ;
        javaMailSender.send(mimeMessage);
        System.out.println("이메일 보내는거 완료");

        //아마 프론트쪽에서 화면을 전환해주어야 할듯?
    }

}
