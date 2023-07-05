package DKUDCoding20231Team3.VISTA.dto.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatLogRequest {

    private Long memberId;

    private Long page;

    private LocalDateTime timeStamp;

}
