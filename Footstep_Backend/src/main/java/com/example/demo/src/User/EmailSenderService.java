package com.example.demo.src.User;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailSenderService {
    private final JavaMailSender javaMailSender;
    //회원가입 및 로그인 이메일 링크 보내주기
    public void sendSignupMail(String token, String email) throws MessagingException {
        System.out.println("이메일 보내는데까지 왔어여");
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        mimeMessageHelper.setFrom("footstep2022@naver.com");
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("[Footstep] 회원가입 이메일 인증");

        StringBuilder body = new StringBuilder();
        body.append("회원가입을 하시려면 다음 링크를 클릭해주세요. (인증키는 5분후 만료됩니다.) \n");
        body.append("http://localhost:8080/users/signup/confirm?email=" + email +
                "&token=" + token);
        mimeMessageHelper.setText(body.toString()) ;
        javaMailSender.send(mimeMessage);
        System.out.println("이메일 보내는거 완료");

        //아마 프론트쪽에서 화면을 전환해주어야 할듯?
    }
    //이메일 변경시 인증번호 링크 보내주기
    public String sendAuthEmail(int auth, String email) throws MessagingException {

        System.out.println("인증번호 보내는데 까지 왔어요. ");
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        mimeMessageHelper.setFrom("footstep2022@naver.com");
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("[Footstep] 이메일 수정 인증 번호");
        StringBuilder body = new StringBuilder();
        body.append("정보수정을 하시려면, 다음 인증번호를 입력해주세요. \n");
        body.append(auth);
        mimeMessageHelper.setText(body.toString()) ;
        javaMailSender.send(mimeMessage);
        System.out.println("인증번호 이메일 보내는거 완료");

        //TODO : 프론트 화면 전환해주어야 함
        //Todo : 이메일이랑 유저 이름이 일치하지 않는데 로그인됨 (문제)

        return email;
    }
}
