/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author sissy
 */
import java.security.Key;

import io.jsonwebtoken.impl.crypto.MacProvider;

public class KeyHolder {
	
	public static final Key key = MacProvider.generateKey();

}