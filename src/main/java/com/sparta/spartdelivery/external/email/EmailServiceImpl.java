package com.sparta.spartdelivery.external.email;

import java.util.Optional;
import java.util.Random;

import jakarta.mail.Message.RecipientType;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Autowired
    JavaMailSender emailSender;

    private final EmailCodeRepository emailCodeRepository;

    private String ePw;

    @Override
    @Transactional
    public String sendSimpleMessage(String to)throws Exception {

        MimeMessage message = createMessage(to);

        try{ // 예외처리
            emailSender.send(message);
        }catch(MailException es){
            es.printStackTrace();
            throw new IllegalArgumentException();
        }

        return ePw;
    }

    // 인증메일 생성
    private MimeMessage createMessage(String to)throws Exception{
        ePw = createKey();

        // 이미 인증코드가 발급되었었는지 확인
        Optional<EmailCode> existingRecord = emailCodeRepository.findByEmail(to);

        if (existingRecord.isPresent()) {
            // 이미 발급된 경우, 기존 레코드를 수정
            EmailCode emailCode = existingRecord.get();
            emailCode.setEmailCode(ePw);
            emailCodeRepository.save(emailCode);
        } else {
            // 새로운 레코드 추가
            emailCodeRepository.save(new EmailCode(to, ePw));
        }

        // 인증메일 작성
        System.out.println("보내는 대상 : "+ to);
        System.out.println("인증 번호 : " + ePw);
        MimeMessage message = emailSender.createMimeMessage();

        message.addRecipients(RecipientType.TO, to);//보내는 대상
        message.setSubject("이메일 인증 테스트");//제목

        String msgg="";
        msgg+= "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg+= "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>";
        msgg+= "<div style='font-size:130%'>";
        msgg+= "CODE : <strong>";
        msgg+= ePw+"</strong><div><br/> ";
        msgg+= "</div>";
        message.setText(msgg, "utf-8", "html");//내용
        message.setFrom(new InternetAddress("applebanana0319@gmail.com","항해99"));//보내는 사람

        return message;
    }

    // 인증코드 랜덤 생성
    public static String createKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 8; i++) { // 인증코드 8자리
            int index = rnd.nextInt(3); // 0~2 까지 랜덤

            switch (index) {
                case 0:
                    key.append((char) (rnd.nextInt(26) + 97));
                    //  a~z  (ex. 1+97=98 => (char)98 = 'b')
                    break;
                case 1:
                    key.append((char) (rnd.nextInt(26) + 65));
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
}