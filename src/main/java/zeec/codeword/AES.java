package zeec.codeword;

import org.spongycastle.jce.provider.BouncyCastleProvider;


import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.util.Arrays;
import java.util.HashMap;


/**
 * Created by Cesar-Melchor on 12/7/16.
 */

public class AES {

    private Cipher aesCipher;
    private SecretKey secretKey;
    private byte[] iv;
    private HashMap<String,SecretKey> contacts;

    public AES() {
        contacts = new HashMap<String, SecretKey>();


        try {
            aesCipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }

    }

    public void createIv() {

    }

    public void generateKey(String name) {
        Security.addProvider(new BouncyCastleProvider());

        KeyGenerator keyGen = null;
        try {
            keyGen = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        keyGen.init(256); // for example
        secretKey = keyGen.generateKey();
        addToContacts(name,secretKey);
//        iv = new byte[16];
//        SecureRandom random = new SecureRandom();
//        random.nextBytes(iv);
//        ivParameterSpec = new IvParameterSpec(iv);
        //addToVectors(name,ivParameterSpec.getIV());
    }

    public void recoverKey(byte[] decryptedKey) {
        secretKey = new SecretKeySpec(decryptedKey, 0, decryptedKey.length, "AES");
    }

    public byte[] encrypt(byte[] plaintext, String contactName) {
        byte[] byteCipherText = new byte[1];
        byte[] c = new byte[1];
        try {

            byte [] iv = new byte[16];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

            aesCipher.init(Cipher.ENCRYPT_MODE, contacts.get(contactName),ivParameterSpec);
            byteCipherText = aesCipher.doFinal(plaintext);
            Mac hmac =  Mac.getInstance("HmacSHA256");
            hmac.init(contacts.get(contactName));
            byte[] hMACtag = hmac.doFinal(byteCipherText);
            int aLen = ivParameterSpec.getIV().length;
            int bLen = byteCipherText.length;
            int tLen = hMACtag.length;
            c = new byte[aLen+bLen+tLen];
            System.arraycopy(ivParameterSpec.getIV(), 0, c, 0, aLen);
            System.arraycopy(byteCipherText, 0, c, aLen, bLen);
            System.arraycopy(hMACtag, 0, c, aLen+bLen, tLen);

        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return c;
    }

    public byte[] decrypt(byte[] cipherText, String contactName) {
        byte[] plainText = new byte[1];
        try {

            byte [] iv = new byte[16];
            byte[] text = new byte[cipherText.length - iv.length-32];
            byte[] hash = new byte[32];
            System.arraycopy(cipherText, 0, iv, 0, iv.length);
            System.arraycopy(cipherText, iv.length, text, 0, text.length);
            System.arraycopy(cipherText, iv.length+text.length, hash,0, 32);
            Mac hmac =  Mac.getInstance("HmacSHA256");
            hmac.init(contacts.get(contactName));


            if (Arrays.equals(hash, hmac.doFinal(text))){
                aesCipher.init(Cipher.DECRYPT_MODE, contacts.get(contactName),new IvParameterSpec(iv));
                plainText = aesCipher.doFinal(text);
            }
            else {
                System.out.println("HMAC Failed");
            }


        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return plainText;
    }

    public SecretKey getSecretKey() { return secretKey; }

    public void addToContacts(String name, SecretKey key) {
        contacts.put(name,key);
    }


}
