package cn.aijiamuyingfang.client.oauth2;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.aijiamuyingfang.client.oauth2.utils.Assert;
import lombok.Data;
import okhttp3.OkHttpClient;

@Data
public class OAuth2Client {
  private String clientId;

  private String clientSecret;

  private String site;

  private OkHttpClient okHttpClient;

  private String scope;

  private String grantType;

  private String username;

  private String password;

  private Map<String, String> parameters;

  private OAuth2Client(Builder builder) {
    this.username = builder.username;
    this.password = builder.password;
    this.clientId = builder.clientId;
    this.clientSecret = builder.clientSecret;
    this.site = builder.site;
    this.scope = builder.scope;
    this.grantType = builder.grantType;
    this.okHttpClient = builder.okHttpClient;
    this.parameters = builder.parameters;
  }

  public OAuthResponse refreshAccessToken(String refreshToken) throws IOException {
    if (this.grantType == null)
      this.grantType = Constants.GRANT_TYPE_REFRESH;
    return Access.refreshAccessToken(this, refreshToken);

  }

  public void refreshAccessToken(final String refreshToken, final OAuthResponseCallback callback) {
    new Thread(() -> {
      OAuthResponse response;
      try {
        response = refreshAccessToken(refreshToken);
        callback.onResponse(response);
      } catch (Exception e) {
        response = new OAuthResponse(e);
        callback.onResponse(response);
      }
    }).start();

  }

  public OAuthResponse requestAccessToken() throws IOException {
    if (this.grantType == null)
      this.grantType = Constants.GRANT_TYPE_PASSWORD;
    return Access.getToken(this);
  }

  public void requestAccessToken(final OAuthResponseCallback callback) {
    new Thread(() -> {
      OAuthResponse response;
      try {
        response = requestAccessToken();
        callback.onResponse(response);
      } catch (Exception e) {
        response = new OAuthResponse(e);
        callback.onResponse(response);
      }
    }).start();
  }

  protected OkHttpClient getOkHttpClient() {
    if (this.okHttpClient == null) {
      return new OkHttpClient();
    } else {
      return this.okHttpClient;
    }
  }

  protected Map<String, String> getParameters() {
    if (parameters == null) {
      return new HashMap<>();
    } else {
      return parameters;
    }
  }

  protected Map<String, String> getFieldsAsMap() {
    Map<String, String> oAuthParams = new HashMap<>();
    oAuthParams.put(Constants.POST_CLIENT_ID, getClientId());
    oAuthParams.put(Constants.POST_CLIENT_SECRET, getClientSecret());
    oAuthParams.put(Constants.POST_GRANT_TYPE, getGrantType());
    oAuthParams.put(Constants.POST_SCOPE, getScope());
    oAuthParams.put(Constants.POST_USERNAME, getUsername());
    oAuthParams.put(Constants.POST_PASSWORD, getPassword());
    return oAuthParams;
  }

  public static class Builder {
    private String clientId;

    private String clientSecret;

    private String site;

    private String scope;

    private String grantType;

    private String username;

    private String password;

    private OkHttpClient okHttpClient;

    private Map<String, String> parameters;

    public Builder(String site) {
      Assert.hasLength(site, "access token request site is required");
      this.site = site;
    }

    public Builder(String clientId, String clientSecret, String site) {
      this(site);
      Assert.hasLength(clientId, "client id is required");
      this.clientId = clientId;
      Assert.hasLength(clientSecret, "client secret is required");
      this.clientSecret = clientSecret;
    }

    public Builder(String username, String password, String clientId, String clientSecret, String site) {
      this(clientId, clientSecret, site);
      Assert.hasLength(username, "username is required");
      this.username = username;
      Assert.hasLength(password, "password is required");
      this.password = password;
    }

    public Builder grantType(String grantType) {
      this.grantType = grantType;
      return this;
    }

    public Builder scope(String scope) {
      this.scope = scope;
      return this;
    }

    public Builder username(String username) {
      this.username = username;
      return this;
    }

    public Builder password(String password) {
      this.password = password;
      return this;
    }

    public Builder okHttpClient(OkHttpClient client) {
      this.okHttpClient = client;
      return this;
    }

    public Builder parameters(Map<String, String> parameters) {
      this.parameters = parameters;
      return this;
    }

    public OAuth2Client build() {
      return new OAuth2Client(this);
    }

  }

}
