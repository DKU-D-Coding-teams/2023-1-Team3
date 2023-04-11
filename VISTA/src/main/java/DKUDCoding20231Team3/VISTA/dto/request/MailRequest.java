package DKUDCoding20231Team3.VISTA.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MailRequest {

    @Pattern(regexp = "^[a-zA-Z0-9]+@dankook\\.ac\\.kr$")
    private String mail;

}
