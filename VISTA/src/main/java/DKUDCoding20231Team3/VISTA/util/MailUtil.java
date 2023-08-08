package DKUDCoding20231Team3.VISTA.util;


import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailUtil {

    private final JavaMailSender javaMailSender;

    public String codeSend(String mail) {
        SimpleMailMessage message = new SimpleMailMessage();
        String code = Integer.toString((int)(Math.random() * (999999 - 100000 + 1) + 100000));

        message.setTo(mail);
        message.setSubject("[Dlink] 메일 인증코드 전송");
        message.setText(
                "Dlink에서 회원 인증을 위한 코드를 전송했습니다.\n" +
                        "하단의 코드를 입력해주세요\n\n" +
                        "--\n" +
                        code +
                        "\n"
        );
        javaMailSender.send(message);

        return code;
    }

}
