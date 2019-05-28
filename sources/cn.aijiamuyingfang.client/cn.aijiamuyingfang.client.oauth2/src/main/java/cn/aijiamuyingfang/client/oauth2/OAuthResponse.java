package cn.aijiamuyingfang.client.oauth2;

import java.io.IOException;

import cn.aijiamuyingfang.client.oauth2.utils.JsonUtils;
import okhttp3.Response;

public class OAuthResponse {
  private Response response;

  private String responseBody;

  private Token token;

  private OAuthError error;

  private boolean jsonParsed;

  private Long expiresAt;

  protected OAuthResponse(Response response) throws IOException {
    this.response = response;
    if (response != null) {
      responseBody = response.body().string();

      if (Utils.isJsonResponse(response)) {
        if (response.isSuccessful()) {
          token = JsonUtils.fromJson(responseBody, Token.class);
          jsonParsed = true;

          if (token.getExpiresIn() != null)
            expiresAt = (token.getExpiresIn() * 1000) + System.currentTimeMillis();
        } else {
          try {
            error = JsonUtils.fromJson(responseBody, OAuthError.class);
            jsonParsed = true;
          } catch (Exception e) {
            error = new OAuthError(e);
            jsonParsed = false;

          }
        }
      }
    }
  }

  protected OAuthResponse(Exception e) {
    this.response = null;
    this.error = new OAuthError(e);
  }

  public boolean isSuccessful() {
    return response != null && response.isSuccessful() && jsonParsed;
  }

  public boolean isJsonResponse() {
    return jsonParsed;
  }

  public Integer getCode() {
    return response != null ? response.code() : null;
  }

  public Long getExpiresIn() {
    return token != null ? token.getExpiresIn() : null;
  }

  public Long getExpiresAt() {
    return expiresAt;
  }

  public String getTokenType() {
    return token != null ? token.getTokenType() : null;
  }

  public String getRefreshToken() {
    return token != null ? token.getRefreshToken() : null;
  }

  public String getAccessToken() {
    return token != null ? token.getAccessToken() : null;
  }

  public String getScope() {
    return token != null ? token.getScope() : null;
  }

  public String getBody() {
    return responseBody;
  }

  public OAuthError getOAuthError() {
    return error;
  }

  public Response getHttpResponse() {
    return response;
  }

}
