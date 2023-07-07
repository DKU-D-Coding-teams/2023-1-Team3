package DKUDCoding20231Team3.VISTA.dto.database;

import DKUDCoding20231Team3.VISTA.domain.enumerations.Gender;

import java.time.LocalDateTime;

public interface ChatInterface {

    String getMessage();

    String getName();

    Gender getGender();

    String getImage();

    Long getAns();

    Long getSendMemberId();

    Long getRecvMemberId();

    LocalDateTime getTimeStamp();

}
