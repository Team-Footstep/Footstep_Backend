package com.example.demo.src.Login;

import com.example.demo.src.Login.model.GetLoginReq;
import com.example.demo.src.Login.model.GetLoginRes;
import com.example.demo.src.User.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class LoginEmailSenderService {
    private final JavaMailSender javaMailSender;

    public GetLoginRes loginMail(GetLoginReq getLoginReq) throws MessagingException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        mimeMessageHelper.setFrom("footstep2022@naver.com");
        mimeMessageHelper.setTo(getLoginReq.getEmail());
        mimeMessageHelper.setSubject("[Footstep] 로그인 메일");

        StringBuilder body = new StringBuilder();
        body.append("로그인 하시려면 다음 링크를 클릭해주세요. \n");
        body.append("http://localhost:3000/users/confirmlogin?email=" + getLoginReq.getEmail() +
                "&token=" + getLoginReq.getToken());
        mimeMessageHelper.setText(body.toString()) ;
        javaMailSender.send(mimeMessage);
        System.out.println("이메일 보내는거 완료");
        return new GetLoginRes(getLoginReq.getEmail(), getLoginReq.getToken());

    }
}
