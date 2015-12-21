package com.harrys.hyppo.client.v1;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicHeader;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

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


    public static byte[] computeSignature(
            final SecretKeySpec key,
            final String keyName,
            final long timestamp,
            final String method,
            final String path,
            final List<NameValuePair> params,
            final byte[] body) {

        final Mac mac = initializeMac(key);
        mac.update(keyName.getBytes(StandardCharsets.UTF_8));
        mac.update(Long.toString(timestamp).getBytes(StandardCharsets.UTF_8));

        mac.update(method.toUpperCase().getBytes(StandardCharsets.UTF_8));
        mac.update(path.getBytes(StandardCharsets.UTF_8));

        final List<NameValuePair> paramList = new ArrayList<>(params);
        paramList.sort((a, b) -> a.getName().compareTo(b.getName()));

        for (final NameValuePair param : paramList){
            mac.update(param.getName().getBytes(StandardCharsets.UTF_8));
            mac.update(param.getValue().getBytes(StandardCharsets.UTF_8));
        }

        return mac.doFinal(body);
    }

    public static Mac initializeMac(final SecretKeySpec key){
        try {
            final Mac mac = Mac.getInstance(SigningAlgorithm);
            mac.init(key);
            return mac;
        } catch (NoSuchAlgorithmException nse) {
            throw new RuntimeException("Failed initialize with algorithm: " + SigningAlgorithm, nse);
        } catch (InvalidKeyException ike){
            throw new RuntimeException("Failed to initialize the MAC instance with key algorithm: " + key.getAlgorithm(), ike);
        }
    }

    public static Mac initializeMac(final HyppoClientConfig config) {
        return initializeMac(config.getKeySecret());
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
