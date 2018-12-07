package gr.di.ecommerce.airbnb.utils;

import java.security.Key;
import java.util.Date;

import javax.ws.rs.NotAuthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import java.util.logging.Logger;

public class AuthenticationFilter  {
    public static void filter(String token) throws Exception {
        // Check if the HTTP Authorization header is present and formatted correctly 
        if (token == null) {
            throw new NotAuthorizedException("Authorization header must be provided");
        }
        else validateToken(token);   
    }

    private static void validateToken(String token) throws Exception {
        
        Logger.getAnonymousLogger().info(String.format("Validating token :[%s]",token));
    	Key key = gr.di.ecommerce.airbnb.utils.KeyHolder.key;
        System.out.println("decrypting with key: " + key.toString());

    	try {
            if (!Jwts.parser().isSigned(token))
            {
                Logger.getAnonymousLogger().info(String.format("Unsigned token"));

                throw new Exception();
            }
            System.out.println("Fetching claim: " + key.toString());

            Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
            
            long nowMillis = System.currentTimeMillis();
            Date now = new Date(nowMillis);
            if (now.after(claims.getExpiration()))
            {
                Logger.getAnonymousLogger().info(String.format("Expired,token signed at %s, expired at %s, now : %s",
                        claims.getIssuedAt().toString(),claims.getExpiration().toString(),now.toString()));
                throw new Exception();
            }
    	} catch (SignatureException e) {
            Logger.getAnonymousLogger().info(String.format("Signature exception."));
            throw e;
    	}
        catch (Exception e)
        {
            Logger.getAnonymousLogger().info(String.format("Exception : "));
            Logger.getAnonymousLogger().info(String.format(e.getMessage()));
            throw e;
        }
    }
}