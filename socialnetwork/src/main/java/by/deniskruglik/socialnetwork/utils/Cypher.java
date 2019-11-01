package by.deniskruglik.socialnetwork.utils;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Cypher {
    private final static String HASHING_ALGORITHM = "MD5";

    public static String encode(String input) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(HASHING_ALGORITHM);
            messageDigest.update(input.getBytes());
            byte[] digest = messageDigest.digest();
            return DatatypeConverter.printHexBinary(digest).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Incorrect hashing algorithm name", e);
        }
    }
}
