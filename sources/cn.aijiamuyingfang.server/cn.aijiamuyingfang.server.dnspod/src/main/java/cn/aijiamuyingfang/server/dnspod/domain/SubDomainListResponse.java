		package cn.aijiamuyingfang.server.dnspod.domain;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * [描述]:
 * <p>
 * 请求包含A记录的子域名的响应
 * </p>
 * @version 1.0.0
 * @author ShiWei
 * @email shiweideyouxiang@sina.cn
 * @date 2019-06-09 07:22:15
 */
@Data
@EqualsAndHashCode(callSuper=false)
@NoArgsConstructor
		public class SubDomainListResponse extends DomainResponse{
  private List<String> subdomain;
}
