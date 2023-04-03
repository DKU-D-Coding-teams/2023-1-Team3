package DKUDCoding20231Team3.VISTA.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MailRequest {

    // 단국대학교 이메일 형식: 도메인 앞에 영문자도 가능하도록

    @Pattern(regexp = "\\w{8,}@dankook\\.ac\\.kr")
    private String mail;

}
