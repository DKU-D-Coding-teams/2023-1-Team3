package DKUDCoding20231Team3.VISTA.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
@NonNull
@AllArgsConstructor
@NoArgsConstructor
public class SignInRequest {

    @Pattern(regexp = "[0-9]{8}@dankook\\.ac\\.kr")
    private String mail;

    private String password;

}
