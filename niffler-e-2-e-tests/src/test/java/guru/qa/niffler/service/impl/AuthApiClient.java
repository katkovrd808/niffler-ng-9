package guru.qa.niffler.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.niffler.api.AuthApi;
import guru.qa.niffler.api.core.ThreadSafeCookieStore;
import guru.qa.niffler.api.core.interceptor.QueryLoggingInterceptor;
import guru.qa.niffler.api.core.interceptor.ThreadSafeQueryStore;
import retrofit2.Response;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static guru.qa.niffler.utils.oauth.OAuthUtils.generateCodeChallenge;
import static guru.qa.niffler.utils.oauth.OAuthUtils.generateCodeVerifier;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ParametersAreNonnullByDefault
public class AuthApiClient extends RestClient {

  private final AuthApi authApi;

  private final static String RESPONSE_TYPE = "code";
  private final static String CLIENT_ID = "client";
  private final static String REDIRECT_URI = CFG.frontUrl() + "authorized";

  public AuthApiClient() {
    super(CFG.authUrl(), true, new QueryLoggingInterceptor());
    this.authApi = create(AuthApi.class);
  }

  @Nonnull
  public String login(String username, String password) throws UnsupportedEncodingException {
    final String codeVerifier = generateCodeVerifier();
    final String codeChallenge = generateCodeChallenge(codeVerifier);
    final String token;

    try {
      authorizeOAuth(codeChallenge);
      loginOAuth(username, password);
      token = requestTokenOAuth(codeVerifier);
    } catch (IOException e) {
      throw new AssertionError(e);
    }
    return token;
  }

  private void authorizeOAuth(String codeChallenge) throws IOException {
    final Response<Void> response;
    response = authApi.authorizeOAuth(
      RESPONSE_TYPE,
      CLIENT_ID,
      "openid",
      REDIRECT_URI,
      codeChallenge,
      "S256"
    ).execute();
    assertEquals(200, response.code());
  }

  private void loginOAuth(String username, String password) throws IOException {
    final Response<Void> response;
    response = authApi.loginOAuth(
      ThreadSafeCookieStore.INSTANCE.cookieValue("XSRF-TOKEN"),
      username,
      password
    ).execute();
    assertEquals(200, response.code());
  }

  private String requestTokenOAuth(String codeVerifier) throws IOException {
    final Response<JsonNode> response;
    response = authApi.tokenOAuth(
      ThreadSafeQueryStore.INSTANCE.get("code"),
      REDIRECT_URI,
      codeVerifier,
      "authorization_code",
      CLIENT_ID
    ).execute();
    assertEquals(200, response.code());
    return response.body().get("id_token").asText();
  }
}
