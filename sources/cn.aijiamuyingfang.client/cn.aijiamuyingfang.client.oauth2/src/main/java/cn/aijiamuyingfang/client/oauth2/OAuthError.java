package cn.aijiamuyingfang.client.oauth2;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class OAuthError {
  @SerializedName("error")
  private String error;

  @SerializedName("error_description")
  private String descirption;

  @SerializedName("error_uri")
  private String uri;

  private Exception cause;

  public OAuthError(Exception e) {
    cause = e;
  }
}
