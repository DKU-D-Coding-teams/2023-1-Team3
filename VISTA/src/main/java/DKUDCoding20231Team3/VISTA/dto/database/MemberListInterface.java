package DKUDCoding20231Team3.VISTA.dto.database;

import DKUDCoding20231Team3.VISTA.domain.enumerations.Gender;

import java.time.LocalDate;

public interface MemberListInterface {

    Long getMemberId();

    String getName();

    Gender getGender();

    LocalDate getBirth();

}