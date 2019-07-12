package cn.aijiamuyingfang.client.dnspod.domain;

import java.util.Map;

import cn.aijiamuyingfang.client.commons.constant.ClientRestConstants;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import okhttp3.RequestBody;

/**
 * [描述]:
 * <p>
 * 更新动态DNS记录的请求
 * </p>
 * 
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2019-06-10 06:47:57
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class UpdateRecordRequest extends DomainRequest {
  /**
   * 记录ID，必选
   */
  private String recordId;

  /**
   * 主机记录，如 www
   */
  private String subDomain;

  /**
   * 记录线路，通过API记录线路获得，中文，比如：默认，必选
   */
  private String recordLine;

  public UpdateRecordRequest(String tokenId, String tokenValue, String domain, String subDomain, String recordId,
      String recordLine) {
    super(tokenId, tokenValue, domain);
    this.subDomain = subDomain;
    this.recordId = recordId;
    this.recordLine = recordLine;
  }

  @Override
  public Map<String, RequestBody> toPartMap() {
    Map<String, RequestBody> partMap = super.toPartMap();
    partMap.put("record_id", RequestBody.create(ClientRestConstants.MEDIA_TYPE_MULTIPART, recordId));
    partMap.put("sub_domain", RequestBody.create(ClientRestConstants.MEDIA_TYPE_MULTIPART, subDomain));
    partMap.put("record_line", RequestBody.create(ClientRestConstants.MEDIA_TYPE_MULTIPART, recordLine));
    return partMap;
  }
}
