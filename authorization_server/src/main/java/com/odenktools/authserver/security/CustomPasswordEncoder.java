package com.odenktools.authserver.security;

import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Custom Password encoder.
 *
 * @author Odenktools.
 */
public class CustomPasswordEncoder implements PasswordEncoder {

  @Override
  public String encode(CharSequence rawPassword) {

    String hashed = null;
    try {
      hashed = Encryption.EncryptAESCBCPCKS5Padding(rawPassword.toString());
    }
    catch (InvalidKeyException | NoSuchAlgorithmException |
        NoSuchPaddingException | BadPaddingException |
        IllegalBlockSizeException | InvalidAlgorithmParameterException e) {
      e.printStackTrace();
    }
    return hashed;
  }

  @Override
  public boolean matches(CharSequence rawPassword, String encodedPassword) {

    try {
      String hashed = Encryption.EncryptAESCBCPCKS5Padding(rawPassword.toString());
      if (hashed.matches(encodedPassword)) {
        return true;
      }
    }
    catch (InvalidKeyException | NoSuchAlgorithmException |
        NoSuchPaddingException | IllegalBlockSizeException |
        BadPaddingException | InvalidAlgorithmParameterException e) {
      return false;
    }
    return false;
  }

}
