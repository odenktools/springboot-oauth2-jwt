package com.odenktools.authserver.security;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Encryption {

  private static String KEY = "770A8A74DA156D24";

  private static String IV = "9mss4ws3dlsugane";

  /**
   * Password Encryption.
   *
   * @param plain data password yang akan di enkrip.
   * @return Nilai Enkripsi.
   * @throws InvalidKeyException                Invalid key.
   * @throws InvalidAlgorithmParameterException Invalid Algorithm.
   * @throws NoSuchAlgorithmException           We didnt find Algorithm.
   * @throws NoSuchPaddingException             We didnt find Padding.
   * @throws IllegalBlockSizeException          Block padding Error.
   * @throws BadPaddingException                Padding Error.
   */
  public static String EncryptAESCBCPCKS5Padding(String plain)
      throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException,
      NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {

    byte[] key = KEY.getBytes();
    SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

    byte[] iv = IV.getBytes();
    IvParameterSpec ivspec = new IvParameterSpec(iv);

    // initialize the cipher for encrypt mode
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivspec);

    // encrypt the message
    byte[] encrypted = cipher.doFinal(plain.getBytes());
    return hexEncode(encrypted);
  }

  /**
   * Decrypt password.
   *
   * @param encryptText data password yang akan di dekrip.
   * @return Nilai Enkripsi.
   * @throws InvalidKeyException                Invalid key.
   * @throws InvalidAlgorithmParameterException Invalid Algorima.
   * @throws NoSuchAlgorithmException           Algorima tidak ada.
   * @throws NoSuchPaddingException             Padding tidak ada.
   * @throws IllegalBlockSizeException          Block padding tidak sesuai.
   * @throws BadPaddingException                Padding tidak sesuai.
   */
  public static String DecryptAESCBCPCKS5Padding(String encryptText)
      throws InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException,
      NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {

    byte[] key = KEY.getBytes();
    SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");

    byte[] iv = IV.getBytes();
    IvParameterSpec ivspec = new IvParameterSpec(iv);

    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.DECRYPT_MODE, skeySpec, ivspec);

    byte[] original = cipher.doFinal(hexDecode(encryptText));

    return new String(original);
  }

  private static byte[] hexDecode(String input) {

    try {
      return Hex.decodeHex(input.toCharArray());
    }
    catch (DecoderException e) {
      throw new IllegalStateException("Hex Decoder exception", e);
    }

  }

  /**
   * Hex encodes a byte array.
   * Returns an empty string if the input array is null or empty.
   *
   * @param input bytes to encode
   * @return string containing hex representation of input byte array
   */
  private static String hexEncode(byte[] input) {

    if (input == null || input.length == 0) {
      return "";
    }

    int inputLength = input.length;
    StringBuilder output = new StringBuilder(inputLength * 2);

    for (byte anInput : input) {
      int next = anInput & 0xff;
      if (next < 0x10) {
        output.append("0");
      }

      output.append(Integer.toHexString(next));
    }

    return output.toString();
  }

  /**
   * Generate MD5.
   *
   * @param plain Data yang akan digenerate MD5 nya.
   * @return MD5.
   * @throws NoSuchAlgorithmException Error jika data input tidak sesuai.
   */
  public static String MD5(String plain) throws NoSuchAlgorithmException {

    String enc;

    MessageDigest md = MessageDigest.getInstance("MD5");
    md.reset();

    md.update(plain.getBytes());
    byte[] encPasswordByte = md.digest();

    StringBuilder hexString = new StringBuilder();
    for (byte anEncPasswordByte : encPasswordByte) {
      hexString.append(Integer.toHexString(0xFF & anEncPasswordByte));
    }

    enc = hexString.toString();

    return enc;
  }

  public static String SHA1(String plain) throws NoSuchAlgorithmException {

    String enc;

    MessageDigest md = MessageDigest.getInstance("SHA1");
    md.reset();

    md.update(plain.getBytes());
    byte[] encPasswordByte = md.digest();

    StringBuilder sb = new StringBuilder();
    for (byte anEncPasswordByte : encPasswordByte) {
      sb.append(Integer.toString((anEncPasswordByte & 0xff) + 0x100, 16).substring(1));
    }
    enc = sb.toString();

    return enc;
  }

}
