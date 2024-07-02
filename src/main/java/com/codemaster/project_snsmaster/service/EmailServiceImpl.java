package com.codemaster.project_snsmaster.service;

import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements IF_EmailService {
    @Autowired
    JavaMailSender emailSender;

    public static final String ePw = createKey();



    public static String createKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 8; i++) { // 인증코드 8자리
            int index = rnd.nextInt(3); // 0~2 까지 랜덤

            switch (index) {
                case 0:
                    key.append((char) ((int) (rnd.nextInt(26)) + 97));
                    //  a~z  (ex. 1+97=98 => (char)98 = 'b')
                    break;
                case 1:
                    key.append((char) ((int) (rnd.nextInt(26)) + 65));
                    //  A~Z
                    break;
                case 2:
                    key.append((rnd.nextInt(10)));
                    // 0~9
                    break;
            }
        }
        return key.toString();
    }

    private MimeMessage createMessage(String to) throws Exception {
        System.out.println("보내는 대상 : " + to);
        System.out.println("인증 번호 : " + ePw);
        MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, to);//보내는 대상
        message.setSubject("회원가입 이메일 인증 테스트");//제목

        String msgg = "";
        msgg += "<div style='margin:20px;'>";
        msgg += "<h1> 안녕하세요 snsMaster입니다. </h1>";
        msgg += "<br>";
        msgg += "<p>아래 코드를 복사해 입력해주세요<p>";
        msgg += "<br>";
        msgg += "<p>감사합니다.<p>";
        msgg += "<br>";
        msgg += "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg += "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>";
        msgg += "<div style='font-size:130%'>";
        msgg += "CODE : <strong>";
        msgg += ePw + "</strong><div><br/> ";
        msgg += "</div>";
        message.setText(msgg, "utf-8", "html");//내용
        message.setFrom(new InternetAddress("ekfrif0914@gmail.com", "snsMaster"));//보내는 사람

        return message;
    }

    private MimeMessage createPwMessage(String to,String userPw) throws Exception {
        System.out.println("보내는 대상 : " + to);
        System.out.println("비밀 번호 : " + userPw);
        MimeMessage message = emailSender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, to);//보내는 대상
        message.setSubject("회원님의 비밀번호입니다");//제목

        String msgg = "";
        msgg += "<div style='margin:20px;'>";
        msgg += "<h1> 안녕하세요 snsMaster입니다. </h1>";
        msgg += "<br>";
        msgg += "<p>회원님의 비밀번호를 안내드립니다.<p>";
        msgg += "<br>";
        msgg += "<p>감사합니다.<p>";
        msgg += "<br>";
        msgg += "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg += "<h3 style='color:blue;'>회원님의 비밀번호</h3>";
        msgg += "<div style='font-size:130%'>";
        msgg += "CODE : <strong>";
        msgg += userPw + "</strong><div><br/> ";
        msgg += "</div>";
        message.setText(msgg, "utf-8", "html");//내용
        message.setFrom(new InternetAddress("ekfrif0914@gmail.com", "snsMaster"));//보내는 사람

        return message;
    }


    @Override
    public String sendSimpleMessage(String to) throws Exception {
        // TODO Auto-generated method stub
        MimeMessage message = createMessage(to);
        try {//예외처리
            emailSender.send(message);

        } catch (MailException es) {
            es.printStackTrace();
            throw new IllegalArgumentException();

        }
        return ePw;
    }

    @Override
    public String sendPwMessage(String to, String userPW) throws Exception {
        MimeMessage message = createPwMessage(to,userPW);
        try {//예외처리
            emailSender.send(message);

        } catch (MailException es) {
            es.printStackTrace();
            throw new IllegalArgumentException();

        }
        return userPW;
    }
}
