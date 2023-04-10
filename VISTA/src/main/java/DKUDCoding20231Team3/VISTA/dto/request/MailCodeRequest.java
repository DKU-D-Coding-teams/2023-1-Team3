package DKUDCoding20231Team3.VISTA.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MailCodeRequest {

    @Pattern(regexp = "^[a-zA-Z0-9]+@dankook\\\\.ac\\\\.kr$")
    private String mail;

    @Pattern(regexp = "[0-9]{6}")
    private String code;

}
