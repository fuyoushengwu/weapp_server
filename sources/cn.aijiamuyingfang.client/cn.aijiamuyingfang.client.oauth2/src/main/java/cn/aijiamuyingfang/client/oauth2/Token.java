package cn.aijiamuyingfang.client.oauth2;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class Token {
  @SerializedName("expires_in")
  private Long expiresIn;

  @SerializedName("token_type")
  private String tokenType;

  @SerializedName("refresh_token")
  private String refreshToken;

  @SerializedName("access_token")
  private String accessToken;

  @SerializedName("scope")
  private String scope;

  @SerializedName("username")
  private String username;

  @SerializedName("jti")
  private String jti;
}
