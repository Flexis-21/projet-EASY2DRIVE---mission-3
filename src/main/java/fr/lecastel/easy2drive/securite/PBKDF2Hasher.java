package fr.lecastel.easy2drive.securite;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HexFormat;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PBKDF2Hasher {
    private static final int ITERATIONS = 210_000;
    private static final int TAILLE_CLE = 256;
    private static final int TAILLE_SEL = 16;
    private static final SecureRandom ALEATOIRE = new SecureRandom();

    private PBKDF2Hasher() {
    }

    public static String nouveauSel() {
        byte[] sel = new byte[TAILLE_SEL];
        ALEATOIRE.nextBytes(sel);
        return Base64.getEncoder().encodeToString(sel);
    }

    public static String hacher(char[] motDePasse, String sel) {
        return HexFormat.of().formatHex(calculerCle(motDePasse, Base64.getDecoder().decode(sel)));
    }

    public static boolean verifier(char[] motDePasse, String hash, String sel) {
        try {
            byte[] attendu = HexFormat.of().parseHex(hash);
            byte[] selDecode = Base64.getDecoder().decode(sel);
            if (attendu.length != TAILLE_CLE / 8 || selDecode.length != TAILLE_SEL) {
                return false;
            }
            return MessageDigest.isEqual(attendu, calculerCle(motDePasse, selDecode));
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }

    private static byte[] calculerCle(char[] motDePasse, byte[] sel) {
        PBEKeySpec parametres = new PBEKeySpec(motDePasse, sel, ITERATIONS, TAILLE_CLE);
        try {
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                    .generateSecret(parametres).getEncoded();
        } catch (GeneralSecurityException exception) {
            throw new IllegalStateException("Le calcul PBKDF2 est indisponible.", exception);
        } finally {
            parametres.clearPassword();
        }
    }
}
