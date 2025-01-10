package mg.itu.cryptomonnaie.utils;

import java.security.SecureRandom;

public class SecureTokenGenerator {

    public static String generateToken(int length) {
	String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
	SecureRandom secureRandom = new SecureRandom();
	StringBuilder stringBuilder = new StringBuilder();

	for (int i = 0; i < length; i++) {
	    int index = secureRandom.nextInt(characters.length());
	    stringBuilder.append(characters.charAt(index));
	}

	return stringBuilder.toString();
    }
}
