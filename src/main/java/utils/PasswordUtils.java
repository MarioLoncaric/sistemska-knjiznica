package utils;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordUtils {

    private static final int ITERATIONS = 210_000;
    private static final int KEY_LENGTH = 256;
    private static final SecureRandom RANDOM = new SecureRandom();

    private PasswordUtils(){
        throw new UnsupportedOperationException("Utility klasa");
    }

    public static String hash(String password) {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(salt) + ":" + encoder.encodeToString(pbkdf2(password, salt));
    }

    public static boolean matches(String password, String stored) {
        if (stored == null) {
            return false;
        }
        String[] parts = stored.split(":");
        if (parts.length != 2) {
            return false;
        }
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] expected = decoder.decode(parts[1]);
            return MessageDigest.isEqual(expected, pbkdf2(password, decoder.decode(parts[0])));
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private static byte[] pbkdf2(String password, byte[] salt) {
        try {
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512").generateSecret(spec).getEncoded();
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("PBKDF2 nije dostupan", e);
        }
    }
}
