package utils;

import java.security.Key;
import java.util.Date;

import javax.ws.rs.NotAuthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;

public class AuthenticationFilter  {
    public static void filter(String token) throws Exception {
        // Check if the HTTP Authorization header is present and formatted correctly 
        if (token == null) {
            throw new NotAuthorizedException("Authorization header must be provided");
        }
        else validateToken(token);   
    }

    private static void validateToken(String token) throws Exception {
    	Key key = utils.KeyHolder.key;
    	try {
            Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
            long nowMillis = System.currentTimeMillis();
            Date now = new Date(nowMillis);
            if (now.after(claims.getExpiration())) throw new Exception();
    	} catch (SignatureException e) {
            throw e;
    	}
    }
}