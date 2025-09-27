package guru.qa.niffler.utils.oauth;

import lombok.SneakyThrows;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

@ParametersAreNonnullByDefault
public class OAuthUtils {
  @Nonnull
  public static String generateCodeVerifier() {
    SecureRandom secureRandom = new SecureRandom();
    byte[] codeVerifierBytes = new byte[32];
    secureRandom.nextBytes(codeVerifierBytes);
    return Base64.getUrlEncoder().withoutPadding().encodeToString(codeVerifierBytes);
  }

  @SneakyThrows
  @Nonnull
  public static String generateCodeChallenge(String codeVerifier) throws UnsupportedEncodingException {
    byte[] bytes = codeVerifier.getBytes("US-ASCII");
    MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
    messageDigest.update(bytes, 0, bytes.length);
    byte[] digest = messageDigest.digest();
    return Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
  }
}
