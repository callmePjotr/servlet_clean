import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class encrypt {
    protected String encryptstring(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] messageDigest = md.digest(input.getBytes());
        BigInteger bigInt = new BigInteger(1, messageDigest);
        return bigInt.toString(16);
    }

    protected static String send(String passwd) throws NoSuchAlgorithmException {
        encrypt encryptor = new encrypt();
        String salt = "thisismyverysaltysalt";
        String concatpw = passwd + salt;

        String encryptedpw = encryptor.encryptstring(concatpw);

        return encryptedpw;
    }
}
