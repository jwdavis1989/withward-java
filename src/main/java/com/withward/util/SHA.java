package com.withward.util;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class SHA {
	
	  /**
	   * Returns a salted and hashed password using SHA-512.<br>
	   *
	   * @param String password - the password to be hashed
	   * @param byte[] salt     - a 16 bytes salt, ideally obtained with the getSalt method
	   *
	   * @return String hashed password
	   */
	public String hashingMethod(String password, byte[] salt) {
		
		String generatedPassword = null;
		
		try {			
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			md.update(salt);
			byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));
		
			StringBuilder sb = new StringBuilder();
			for(int i=0; i< hashedPassword.length; i++) {
				sb.append(Integer.toString((hashedPassword[i] & 0xff) + 0x100, 16).substring(1));
			}
			generatedPassword = sb.toString();
		
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return generatedPassword;
		
	}
	
	  /**
	   * Returns a random salt to be used to hash a password.
	   *
	   * @return a 16 bytes random salt
	   */
	public byte[] getSalt() {
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[16];
		random.nextBytes(salt);
		return salt;
	}
}
