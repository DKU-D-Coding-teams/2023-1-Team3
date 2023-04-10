package DKUDCoding20231Team3.VISTA.dto.request;

import DKUDCoding20231Team3.VISTA.domain.enumerations.Gender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

    @Pattern(regexp = "^[a-zA-Z0-9]+@dankook\\.ac\\.kr$")
    private String mail;

    @Setter
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$")
    private String password;

    @NotBlank
    private String name;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @NonNull
    private LocalDate birth;

}
