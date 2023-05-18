package DKUDCoding20231Team3.VISTA.dto.database;

import DKUDCoding20231Team3.VISTA.domain.enumerations.Gender;

import java.time.LocalDate;

public interface MemberInterface {

    Long getMemberId();

    String getName();

    Gender getGender();

    LocalDate getBirth();

    String getImage();

    String getDepartment();

    String getIntroduction();

}
