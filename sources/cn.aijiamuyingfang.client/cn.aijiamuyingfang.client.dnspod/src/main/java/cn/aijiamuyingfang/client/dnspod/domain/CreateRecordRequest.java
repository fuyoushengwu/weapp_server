package cn.aijiamuyingfang.client.dnspod.domain;

import java.util.Map;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import okhttp3.MediaType;
import okhttp3.RequestBody;

@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
public class CreateRecordRequest extends DomainRequest {

  /**
   * 主机记录, 如 www，可选，如果不传，默认为www，可选
   */
  private String subDomain = "www";

  /**
   * 记录类型，通过API记录类型获得，大写英文，比如：A，可选
   */
  private String recordType = "A";

  /**
   * 记录线路，通过API记录线路获得，中文，可选
   */
  private String recordLine = "默认";

  /**
   * 记录值, 如 IP:200.200.200.200, CNAME: cname.dnspod.com., MX: mail.dnspod.com.,必选
   */
  private String value;

  /**
   * {1-20} MX优先级, 当记录类型是 MX 时有效，范围1-20，可选
   */
  private int mx = 1;

  /**
   * {1-604800} TTL，范围1-604800，不同等级域名最小值不同, 可选
   */
  private int ttl = 600;

  /**
   * [“enable”,“disable”]，记录初始状态，默认为”enable”，如果传入”disable”，解析不会生效，也不会验证负载均衡的限制，可选
   */
  private String status = "enable";

  /**
   * 权重信息，0到100的整数，可选。仅企业 VIP 域名可用，0 表示关闭，留空或者不传该参数，表示不设置权重信息，可选
   */
  private int weight = 0;

  public CreateRecordRequest(String tokenId, String tokenValue, String rootDomain, String ip) {
    super(tokenId, tokenValue, rootDomain);
    this.value = ip;
  }
  
  public Map<String,RequestBody> toPartMap(){
    Map<String,RequestBody> partMap=super.toPartMap();
    partMap.put("sub_domain", RequestBody.create(MediaType.parse("multipart/form-data"), subDomain));
    partMap.put("record_type", RequestBody.create(MediaType.parse("multipart/form-data"), recordType));
    partMap.put("record_line", RequestBody.create(MediaType.parse("multipart/form-data"), recordLine));
    partMap.put("value", RequestBody.create(MediaType.parse("multipart/form-data"), value));
    partMap.put("mx", RequestBody.create(MediaType.parse("multipart/form-data"), Integer.toString(mx)));
    partMap.put("ttl", RequestBody.create(MediaType.parse("multipart/form-data"), Integer.toString(ttl)));
    partMap.put("status", RequestBody.create(MediaType.parse("multipart/form-data"), status));
    partMap.put("weight", RequestBody.create(MediaType.parse("multipart/form-data"), Integer.toString(weight)));
    return partMap;
  }
}
