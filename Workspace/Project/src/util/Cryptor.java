//작성자 : 윤정도

package util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Cryptor {
	private static char key = 0x21;
	
	public static String xorEncrypt(String source) {
		StringBuilder builder = new StringBuilder(source);
		
		for (int i = 0; i < builder.length(); i++) {
			builder.setCharAt(i, (char)(source.charAt(i) ^ key));
		}
		
		return builder.toString();
	}
	
	public static String xorDecrypt(String source) {
		StringBuilder builder = new StringBuilder(source);
		
		for (int i = 0; i < builder.length(); i++) {
			builder.setCharAt(i, (char)(source.charAt(i) ^ key));
		}
		
		return builder.toString();
	}
	
	private static String bytesToHex(byte[] hash) {
	    StringBuilder hexString = new StringBuilder(2 * hash.length);
	    for (int i = 0; i < hash.length; i++) {
	        String hex = Integer.toHexString(0xff & hash[i]);
	        if(hex.length() == 1) {
	            hexString.append('0');
	        }
	        hexString.append(hex);
	    }
	    return hexString.toString();
	}
	
	// Java SHA256
	// https://www.baeldung.com/sha-256-hashing-java
	public static String sha256(String source) throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hash = digest.digest(source.getBytes(StandardCharsets.UTF_8));
		String sha256hex = new String(bytesToHex(hash));
		return sha256hex;
	}
}
