package com.harrys.hyppo.client.v1;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * Created by jpetty on 12/18/15.
 */
public final class HyppoSigning {

    public static final String SigningAlgorithm = "HmacSHA1";

    public static final String KeyNameHeaderName    = "X-HYPPO-KEY";
    public static final String SignatureHeaderName  = "X-HYPPO-SIGNATURE";
    public static final String TimestampHeaderName  = "X-TIMESTAMP";


    public static Header signingKeyNameHeader(final String keyName){
        return new BasicHeader(KeyNameHeaderName, keyName);
    }

    public static Header timestampHeader(final long timestamp){
        return new BasicHeader(TimestampHeaderName, Long.toString(timestamp));
    }

    public static Header signatureHeader(final byte[] signature){
        return new BasicHeader(SignatureHeaderName, encodeSignature(signature));
    }

    public static String encodeSignature(final byte[] signature){
        return encodeBase64(signature);
    }

    public static byte[] decodeSignature(final String encodedSignature){
        return decodeBase64(encodedSignature);
    }

    public static String encodeBase64(final byte[] signature){
        return Base64.getEncoder().encodeToString(signature);
    }

    public static byte[] decodeBase64(final String base64){
        return Base64.getDecoder().decode(base64);
    }


    public static SecretKeySpec decodeSecretKey(final String encodedKey){
        final byte[] bytes = decodeBase64(encodedKey);
        return new SecretKeySpec(bytes, SigningAlgorithm);
    }

    public static String encodeSecretKey(final SecretKeySpec key){
        return encodeBase64(key.getEncoded());
    }

    public static SecretKeySpec generateRandomKey(){
        try {
            final SecretKey key = KeyGenerator.getInstance(SigningAlgorithm).generateKey();
            return new SecretKeySpec(key.getEncoded(), key.getAlgorithm());
        } catch (NoSuchAlgorithmException nsae){
            throw new RuntimeException("Failed generate random key for algorithm: " + SigningAlgorithm, nsae);
        }
    }

}
