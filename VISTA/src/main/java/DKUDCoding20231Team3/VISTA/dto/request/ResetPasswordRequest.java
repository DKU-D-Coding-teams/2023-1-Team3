package DKUDCoding20231Team3.VISTA.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class ResetPasswordRequest {

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$")
    private String currentPassword;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$")
    private String futurePassword;

}
