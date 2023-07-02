package DKUDCoding20231Team3.VISTA.dto.message;

import DKUDCoding20231Team3.VISTA.domain.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {

    private Member sendMember;
//    private Long sendMemberId;

    private Member recvMember;
//    private Long recvMemberId;

    private LocalDateTime timeStamp;

    private String message;

}
