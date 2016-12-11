package com.example.alexhan.codeword;


import org.spongycastle.jce.provider.BouncyCastleProvider;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.spongycastle.jce.provider.BouncyCastleProvider;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * Created by Cesar-Melchor on 12/7/16.
 */

public class RSA implements Runnable {

    private Cipher cipher;
    private KeyPairGenerator generator;
    private KeyPair pair;
    private Key pubKey;
    private Key privKey;
    private SecureRandom random;
    private String publicKeyFile;
    private String privateKeyFile;

    public RSA(String username) {
        Security.addProvider(new BouncyCastleProvider());
        this.publicKeyFile = username + "_pub.pem";
        this.privateKeyFile = username + ".pem";
        cipher = null;
        try {
            cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA256AndMGF1Padding","BC");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }

        random = new SecureRandom();
        generator = null;
        try {
            generator = KeyPairGenerator.getInstance("RSA","BC");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
    }

    private void generateKeys() {



        generator.initialize(2048, random);

        pair = generator.generateKeyPair();
        pubKey = pair.getPublic();
        privKey = pair.getPrivate();
    }


    public void run() {
        if (!checkForKeys()) {
            generateKeys();
            writeKeys();
        }

    }

    public byte[] encrypt(byte[] message) {
        byte[] input = message;

        byte[] cipherText = new byte[0];
        try {
            cipher.init(Cipher.ENCRYPT_MODE, pubKey, random);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        try {
            cipherText = cipher.doFinal(input);

        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return cipherText;
    }

    public byte[] decrypt(byte[] cipherText,String username) {
        Key k  = checkForPrivKey(username);
        try {
            cipher.init(Cipher.DECRYPT_MODE, k);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        byte[] plainText = new byte[0];
        try {
            plainText = cipher.doFinal(cipherText);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return plainText;
    }

    private void writeKeys() {
        RSAPrivateKey priv = (RSAPrivateKey) pair.getPrivate();
        RSAPublicKey pub = (RSAPublicKey) pair.getPublic();

        PemFileWriter pemFile = new PemFileWriter(priv, "RSA PRIVATE KEY");
        try {
            pemFile.write(privateKeyFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        PemFileWriter pubFile = new PemFileWriter(pub, "RSA PUBLIC KEY");
        try {
            pubFile.write(publicKeyFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private boolean checkForKeys(){
        try {
            PemFileReader pubFile = new PemFileReader(publicKeyFile);
            byte[] content = pubFile.getPemObject().getContent();

            X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(content);

            Security.addProvider( new BouncyCastleProvider() );
            KeyFactory kf = KeyFactory.getInstance("RSA","BC");


            pubKey = kf.generatePublic(pubKeySpec);

            PemFileReader privFile = new PemFileReader(privateKeyFile);
            byte[] content2 = privFile.getPemObject().getContent();
            PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(content2);
            privKey = kf.generatePrivate(privKeySpec);
            return true;

        } catch (IOException e) {
            return false;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
            return false;
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            return false;
        }

    }


    private Key checkForPrivKey(String username){
        String fileName = username+".pem";
        try {

            Security.addProvider( new BouncyCastleProvider() );
            KeyFactory kf = KeyFactory.getInstance("RSA","BC");
            PemFileReader privFile = new PemFileReader(fileName);
            byte[] content2 = privFile.getPemObject().getContent();
            PKCS8EncodedKeySpec privKeySpec = new PKCS8EncodedKeySpec(content2);
            Key k = kf.generatePrivate(privKeySpec);

            return k;

        } catch (IOException e) {
            return null;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
            return null;
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            return null;
        }

    }

}
