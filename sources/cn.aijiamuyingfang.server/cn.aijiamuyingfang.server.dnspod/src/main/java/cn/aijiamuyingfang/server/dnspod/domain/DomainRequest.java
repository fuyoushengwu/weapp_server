package cn.aijiamuyingfang.server.dnspod.domain;

import java.util.Map;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import okhttp3.MediaType;
import okhttp3.RequestBody;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
public class DomainRequest extends BaseRequest {
  /**
   * 域名分组类型, 默认为’mine’. 包含以下类型：<br>
   * all：所有域名<br>
   * mine：我的域名<br>
   * share：共享给我的域名<br>
   * ismark：星标域名<br>
   * pause：暂停域名<br>
   * vip：VIP域名<br>
   * recent：最近操作过的域名<br>
   * share_out：我共享出去的域名<br>
   */
  private String type = "mine";

  /**
   * 域名
   */
  private String domain;

  public DomainRequest(String tokenId, String tokenValue, String domain) {
    super(tokenId, tokenValue);
    this.domain = domain;
  }
  
  public Map<String,RequestBody> toPartMap(){
    Map<String,RequestBody> partMap=super.toPartMap();
    partMap.put("type", RequestBody.create(MediaType.parse("multipart/form-data"), type));
    partMap.put("domain", RequestBody.create(MediaType.parse("multipart/form-data"), domain));
    return partMap;
  }

}
