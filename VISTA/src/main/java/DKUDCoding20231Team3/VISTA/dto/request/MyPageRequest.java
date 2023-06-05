package DKUDCoding20231Team3.VISTA.dto.request;

import DKUDCoding20231Team3.VISTA.domain.enumerations.Gender;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MyPageRequest {

    @NotBlank
    private String name;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @NonNull
    private LocalDate birth;

    @NotBlank
    private String department;

    @Pattern(regexp = "^.{0,100}$")
    private String introduction;

}
