package cn.aijiamuyingfang.client.dnspod.domain;

import java.util.HashMap;
import java.util.Map;

import cn.aijiamuyingfang.client.commons.constant.ClientRestConstants;
import lombok.Data;
import lombok.NoArgsConstructor;
import okhttp3.RequestBody;

/**
 * [描述]:
 * <p>
 * dnspodqin请求的基础类
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2019-06-09 05:52:46
 */
@Data
@NoArgsConstructor
public abstract class BaseRequest {
  /**
   * 用于鉴权的 API Token
   */
  protected String loginToken;

  /**
   * 返回的数据格式，可选 {json,xml}，默认为json
   */
  protected String format = "json";

  /**
   * 返回的错误语言，可选{en,cn}，默认用cn
   */
  protected String lang = "cn";

  /**
   * 没有数据时是否返回错误，可选{yes,no}，默认为no
   */
  protected String errorOnEmpty = "no";

  protected BaseRequest(String tokenId, String tokenValue) {
    this.loginToken = String.format("%s,%s", tokenId, tokenValue);
  }

  public Map<String, RequestBody> toPartMap() {
    Map<String, RequestBody> partMap = new HashMap<>();
    
    partMap.put("login_token", RequestBody.create(ClientRestConstants.MEDIA_TYPE_MULTIPART, loginToken));
    partMap.put("format", RequestBody.create(ClientRestConstants.MEDIA_TYPE_MULTIPART, format));
    partMap.put("lang", RequestBody.create(ClientRestConstants.MEDIA_TYPE_MULTIPART, lang));
    partMap.put("error_on_empty", RequestBody.create(ClientRestConstants.MEDIA_TYPE_MULTIPART, errorOnEmpty));
    return partMap;
  }
}
