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
    private final EmailCertDao emailCertDao;
    public String sendAuthEmail;

    public void sendSignupMail(String token, String email) throws MessagingException {
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

    public String sendAuthEmail(String email) throws MessagingException {
        int authNum;
        Random r = new Random();
        int checkNum = r.nextInt(888888) +111111;
        System.out.println("인증번호는 " + checkNum);
        authNum = checkNum;

        System.out.println("인증번호 보내는데 까지 왔어요. ");
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        mimeMessageHelper.setFrom("footstep2022@naver.com");
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("[Footstep] 정보 수정 인증 번호");
이
        StringBuilder body = new StringBuilder();
        body.append("정보수정을 하시려면, 다음 인증번호를 입력해주세요.  \n");
        body.append(authNum);
        mimeMessageHelper.setText(body.toString()) ;
        javaMailSender.send(mimeMessage);
        System.out.println("인증번호 이메일 보내는거 완료");

        //TODO : 프론트 화면 전환해주어야 함
        //Todo : 이메일이랑 유저 이름이 일치하지 않는데 로그인됨 (문제)
        //TOdo : 이메일 변경할때만 6자리 인증번호 해야함.
        //TOdo : auth 이름 바꾸기
        //회원가입을 했음
        //디비에 해당 이메일이 있는지 체크해주고
        //있으면, 토큰있는 링크 보내서 클릭해서 인증받으면
        //
        return email;
    }
}
