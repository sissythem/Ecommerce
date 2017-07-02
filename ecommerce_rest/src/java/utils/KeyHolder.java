package utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import io.jsonwebtoken.impl.crypto.MacProvider;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KeyHolder 
{
    public static final Key key = MacProvider.generateKey();
    
    public static String issueToken(String keyname) {
        Key key = KeyHolder.key;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        long expMillis = nowMillis + 300000L;
        Date exp = new Date(expMillis);
        String jws = Jwts.builder()
                    .setSubject(keyname)
                    .setIssuedAt(now)
                    .signWith(SignatureAlgorithm.HS512, key)
                    .setExpiration(exp)
                    .compact();
        return jws;
    }
    
    public static Boolean checkToken(String token, String entityName) 
    {
        try 
        {
            AuthenticationFilter.filter(token);
            return true;
        } 
        catch(Exception ex) 
        {
            Logger.getLogger(entityName).log(Level.SEVERE, null, ex);
            return false;
        }
        
    }
}