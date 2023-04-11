package DKUDCoding20231Team3.VISTA.util;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

public class JwtUtil {

    public class JwtToken {

        private String grantType;

        private String accessToken;

        private String refreshToken;

    }


}