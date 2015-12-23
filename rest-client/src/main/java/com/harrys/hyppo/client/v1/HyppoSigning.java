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

    public static Header timestampHeader(long timestamp){
        return new BasicHeader(TimestampHeaderName, Long.toString(timestamp));
    }

    public static Header signatureHeader(byte[] signature){
        return new BasicHeader(SignatureHeaderName, Base64.getEncoder().encodeToString(signature));
    }


    public static SecretKeySpec decodeSecretKey(final String encodedKey){
        final byte[] bytes = Base64.getDecoder().decode(encodedKey);
        return new SecretKeySpec(bytes, SigningAlgorithm);
    }

    public static String encodeSecretKey(final SecretKeySpec key){
        return Base64.getEncoder().encodeToString(key.getEncoded());
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
