package gr.di.ecommerce.airbnb.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import io.jsonwebtoken.impl.crypto.MacProvider;
import java.util.Date;
import java.util.logging.Logger;

public class KeyHolder 
{
    public static final Key key = MacProvider.generateKey();
    
    public static String issueToken(String keyname) {
        String jws;
        if (keyname != null && keyname != "") {
            Key key = KeyHolder.key;
            long nowMillis = System.currentTimeMillis();
            Date now = new Date(nowMillis);
            long expMillis = nowMillis + 300000L;
            Date exp = new Date(expMillis);
            jws = Jwts.builder()
                        .setSubject(keyname)
                        .setIssuedAt(now)
                        .signWith(SignatureAlgorithm.HS512, key)
                        .setExpiration(exp)
                        .compact();
            System.out.println("User [" + keyname + "] , encrypted with key: " + key.toString());
            System.out.println("Now: " + now + " issued new token ["+jws+"] with expiration: " + exp);
        } else {
            jws = null;
        }
        return jws;
    }
    
    public static Boolean checkToken(String token, String entityName) 
    {
        Logger.getAnonymousLogger().info("Checktoken entname : " + entityName);
        Logger.getAnonymousLogger().info("Validating token: [" + token.toString() + "]");
        try 
        {
            AuthenticationFilter.filter(token);
            return true;
        } 
        catch(Exception ex) 
        {
//            Logger.getLogger(entityName).log(Level.SEVERE, null, ex);
            Logger.getAnonymousLogger().info("Token [" + token.toString() + "] failed to validate");
            return false;
        }
        
    }
}